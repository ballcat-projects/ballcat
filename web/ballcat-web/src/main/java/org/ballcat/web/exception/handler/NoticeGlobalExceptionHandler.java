/*
 * Copyright 2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ballcat.web.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.ballcat.web.exception.domain.ExceptionMessage;
import org.ballcat.web.exception.notice.ExceptionNoticeResponse;
import org.ballcat.common.core.exception.handler.GlobalExceptionHandler;
import org.ballcat.common.core.util.WebUtils;
import org.ballcat.common.util.LocalDateTimeUtils;
import org.ballcat.web.exception.notice.ExceptionNotifier;
import org.ballcat.web.exception.notice.ExceptionNoticeConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.util.FastByteArrayOutputStream;

import java.io.PrintStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 对外发送通知的异常处理类
 *
 * @author lingting 2020-09-03 20:09
 * @author hccake
 */
@Slf4j
public class NoticeGlobalExceptionHandler extends Thread implements GlobalExceptionHandler, InitializingBean {

	private final BlockingQueue<Throwable> queue = new LinkedBlockingQueue<>();

	private static final String NULL_MESSAGE_KEY = "";

	private final String applicationName;

	private final ExceptionNoticeConfig noticeConfig;

	private final List<ExceptionNotifier> exceptionNotifiers;

	/**
	 * 通知消息存放 e.message 堆栈信息
	 */
	private final Map<String, ExceptionMessage> messages;

	/**
	 * 本地物理地址
	 */
	private String mac;

	/**
	 * 本地hostname
	 */
	private String hostname;

	/**
	 * 本地ip
	 */
	private String ip;

	/**
	 * 请求地址
	 */
	private String requestUri;

	public NoticeGlobalExceptionHandler(String applicationName, ExceptionNoticeConfig noticeConfig,
			List<ExceptionNotifier> exceptionNotifiers) {
		Assert.notEmpty(exceptionNotifiers, "exceptionNotifiers 不能为空!");

		this.applicationName = applicationName;
		this.noticeConfig = noticeConfig;
		this.exceptionNotifiers = exceptionNotifiers;
		this.messages = new ConcurrentHashMap<>(noticeConfig.getMax() * 2);

		try {
			InetAddress ia = InetAddress.getLocalHost();
			hostname = ia.getHostName();
			ip = ia.getHostAddress();

			byte[] macByte = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < macByte.length; i++) {
				sb.append(String.format("%02X%s", macByte[i], (i < macByte.length - 1) ? "-" : ""));
			}
			this.mac = sb.toString();
		}
		catch (Exception e) {
			mac = "获取失败!";
		}
	}

	@Override
	public void run() {
		String key;
		StopWatch watch = StopWatch.createStarted();
		long threadId = Thread.currentThread().getId();
		// 未被中断则一直运行
		while (!isInterrupted()) {
			int i = 0;
			while (i < noticeConfig.getMax() && watch.getTime(TimeUnit.SECONDS) < noticeConfig.getTime()) {
				Throwable t = null;
				try {
					// 如果 i=0,即 当前未处理异常，则等待超时时间为 1 小时， 否则为 10 秒
					t = queue.poll(i == 0 ? TimeUnit.HOURS.toSeconds(1) : 10, TimeUnit.SECONDS);
				}
				catch (InterruptedException e) {
					interrupt();
				}
				if (t != null) {
					key = t.getMessage() == null ? NULL_MESSAGE_KEY : t.getMessage();
					// i++
					if (i++ == 0) {
						// 第一次收到数据, 重置计时
						watch.reset();
						watch.start();
						messages.put(key, toMessage(t).setKey(key).setThreadId(threadId));
					}
					else {
						if (messages.containsKey(key)) {
							messages.put(key, messages.get(key).increment());
						}
						else {
							messages.put(key, toMessage(t).setKey(key).setThreadId(threadId));
						}
					}
				}
			}
			// 一次处理结束
			if (messages.size() > 0) {
				// 如果需要发送的消息不为空
				for (ExceptionMessage exceptionMessage : messages.values()) {
					notifyExceptionMessage(exceptionMessage);
				}
				messages.clear();
			}
			watch.reset();
			watch.start();
		}
		if (watch.isStarted()) {
			watch.stop();
		}
	}

	private void notifyExceptionMessage(ExceptionMessage exceptionMessage) {
		for (ExceptionNotifier exceptionNotifier : this.exceptionNotifiers) {
			try {
				ExceptionNoticeResponse response = exceptionNotifier.notify(exceptionMessage);
				if (!response.isSuccess()) {
					log.error("异常消息通知发送失败! notifier:{}, msg: {}", exceptionNotifier.getClass(), response.getErrMsg());
				}
			}
			catch (Exception e) {
				log.error("异常消息通知时发生异常", e);
			}
		}
	}

	public ExceptionMessage toMessage(Throwable t) {
		final FastByteArrayOutputStream stream = new FastByteArrayOutputStream();
		t.printStackTrace(new PrintStream(stream));
		final String e = stream.toString();
		return new ExceptionMessage().setNumber(1)
			.setMac(mac)
			.setApplicationName(applicationName)
			.setHostname(hostname)
			.setIp(ip)
			.setRequestUri(requestUri)
			.setStack((e.length() > noticeConfig.getLength() ? e.substring(0, noticeConfig.getLength()) : e)
				.replace("\\r", ""))
			.setTime(LocalDateTime.now().format(LocalDateTimeUtils.FORMATTER_YMD_HMS));
	}

	@Override
	public void handle(Throwable throwable) {
		try {
			this.requestUri = WebUtils.getRequest().getRequestURI();
			// 是否忽略该异常
			boolean ignore = false;

			// 只有不是忽略的异常类才会插入异常消息队列
			if (Boolean.FALSE.equals(noticeConfig.getIgnoreChild())) {
				// 不忽略子类
				ignore = noticeConfig.getIgnoreExceptions().contains(throwable.getClass());
			}
			else {
				// 忽略子类
				for (Class<? extends Throwable> ignoreException : noticeConfig.getIgnoreExceptions()) {
					// 属于子类
					if (ignoreException.isAssignableFrom(throwable.getClass())) {
						ignore = true;
						break;
					}
				}
			}

			// 不忽略则插入队列
			if (!ignore) {
				queue.put(throwable);
			}
		}
		catch (InterruptedException e) {
			interrupt();
		}
		catch (Exception e) {
			log.error("往异常消息队列插入新异常时出错", e);
		}
	}

	@Override
	public void afterPropertiesSet() {
		this.setName("exception-notice");
		this.start();
	}

}
