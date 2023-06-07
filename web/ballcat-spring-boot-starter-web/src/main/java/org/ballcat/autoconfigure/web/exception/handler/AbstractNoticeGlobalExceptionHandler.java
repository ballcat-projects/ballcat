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
package org.ballcat.autoconfigure.web.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.ballcat.autoconfigure.web.exception.ExceptionHandleProperties;
import org.ballcat.autoconfigure.web.exception.domain.ExceptionMessage;
import org.ballcat.autoconfigure.web.exception.domain.ExceptionNoticeResponse;
import org.ballcat.common.core.exception.handler.GlobalExceptionHandler;
import org.ballcat.common.core.util.WebUtils;
import org.ballcat.common.util.LocalDateTimeUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.FastByteArrayOutputStream;

import java.io.PrintStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author lingting 2020-09-03 20:09
 */
@Slf4j
public abstract class AbstractNoticeGlobalExceptionHandler extends Thread
		implements GlobalExceptionHandler, InitializingBean {

	private final BlockingQueue<Throwable> queue = new LinkedBlockingQueue<>();

	private static final String NULL_MESSAGE_KEY = "";

	protected final ExceptionHandleProperties config;

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

	private final String applicationName;

	protected AbstractNoticeGlobalExceptionHandler(ExceptionHandleProperties config, String applicationName) {
		this.config = config;
		messages = new ConcurrentHashMap<>(config.getMax() * 2);
		this.applicationName = applicationName;
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
	@SuppressWarnings("all")
	public void run() {
		String key;
		StopWatch watch = StopWatch.createStarted();
		long threadId = Thread.currentThread().getId();
		// 未被中断则一直运行
		while (!isInterrupted()) {
			int i = 0;
			while (i < config.getMax() && watch.getTime(TimeUnit.SECONDS) < config.getTime()) {
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
				messages.forEach((k, v) -> {
					try {
						ExceptionNoticeResponse response = send(v);
						if (!response.isSuccess()) {
							log.error("消息通知发送失败! msg: {}", response.getErrMsg());
						}
					}
					catch (Exception e) {
						log.error("消息通知时发生异常", e);
					}
				});
				messages.clear();
			}
			watch.reset();
			watch.start();
		}
		if (watch.isStarted()) {
			watch.stop();
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
			.setStack((e.length() > config.getLength() ? e.substring(0, config.getLength()) : e).replace("\\r", ""))
			.setTime(LocalDateTime.now().format(LocalDateTimeUtils.FORMATTER_YMD_HMS));
	}

	/**
	 * 发送通知
	 * @param sendMessage 发送的消息
	 * @return 返回消息发送状态，如果发送失败需要设置失败信息
	 */
	public abstract ExceptionNoticeResponse send(ExceptionMessage sendMessage);

	@Override
	public void handle(Throwable throwable) {
		try {
			this.requestUri = WebUtils.getRequest().getRequestURI();
			// 是否忽略该异常
			boolean ignore = false;

			// 只有不是忽略的异常类才会插入异常消息队列
			if (Boolean.FALSE.equals(config.getIgnoreChild())) {
				// 不忽略子类
				ignore = config.getIgnoreExceptions().contains(throwable.getClass());
			}
			else {
				// 忽略子类
				for (Class<? extends Throwable> ignoreException : config.getIgnoreExceptions()) {
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
