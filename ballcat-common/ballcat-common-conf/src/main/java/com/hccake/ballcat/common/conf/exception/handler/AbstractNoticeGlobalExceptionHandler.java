package com.hccake.ballcat.common.conf.exception.handler;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.exceptions.ExceptionUtil;
import com.hccake.ballcat.common.conf.config.ExceptionHandleConfig;
import com.hccake.ballcat.common.conf.exception.domain.ExceptionMessage;
import com.hccake.ballcat.common.conf.exception.domain.ExceptionNoticeResponse;
import com.hccake.ballcat.common.core.exception.handler.GlobalExceptionHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 消息通知顶层类
 *
 * @author lingting 2020/6/12 0:35
 */
@Slf4j
public abstract class AbstractNoticeGlobalExceptionHandler extends Thread implements GlobalExceptionHandler {

	private static final String NULL_MESSAGE = "";

	protected final ExceptionHandleConfig config;

	/**
	 * 通知消息存放 e.message 堆栈信息
	 */
	private Map<String, ExceptionMessage> messages = new ConcurrentHashMap<>(10);

	/**
	 * 异常发生数
	 */
	private long number = 0;

	/**
	 * 用来当做锁
	 */
	private final String lock = "";

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

	private final String applicationName;

	public AbstractNoticeGlobalExceptionHandler(ExceptionHandleConfig config, String applicationName) {
		this.config = config;
		this.applicationName = applicationName;
		try {
			InetAddress ia = InetAddress.getLocalHost();
			hostname = ia.getHostName();
			ip = ia.getHostAddress();

			byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < mac.length; i++) {
				sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
			}
			this.mac = sb.toString();
		}
		catch (Exception e) {
			mac = "获取失败!";
		}

		this.start();
	}

	@Override
	public void handle(Throwable e) {
		synchronized (lock) {
			number++;
			String key = e.getMessage() != null ? e.getMessage() : NULL_MESSAGE;
			// 特殊处理 message 为 null 的情况
			ExceptionMessage message = messages.get(key);

			if (message == null) {
				message = new ExceptionMessage().setNumber(0).setMac(mac).setApplicationName(applicationName)
						.setHostname(hostname).setIp(ip);
			}

			message.setNumber(message.getNumber() + 1)
					.setStack(ExceptionUtil.stacktraceToString(e, config.getLength()).replaceAll("\\r", ""))
					.setTime(DateUtil.now()).setThreadId(Thread.currentThread().getId());
			messages.put(key, message);
		}
	}

	@Override
	public void run() {
		this.setName("exception-notice-thread-" + config.getType().name());
		log.debug("异常消息通知线程启动!");
		TimeInterval interval = new TimeInterval();
		while (true) {
			try {
				if (interval.intervalSecond() >= config.getTime() || number >= config.getMax()) {
					if (messages.size() == 0) {
						interval.restart();
						continue;
					}

					Map<String, ExceptionMessage> sendMessages;
					synchronized (lock) {
						sendMessages = messages;
						messages = new ConcurrentHashMap<>(10);
						number = 0;
						interval.restart();
					}
					sendMessages.forEach((k, v) -> {
						ExceptionNoticeResponse response = send(v);
						if (!response.isSuccess()) {
							log.error("消息通知发送失败! msg: {}", response.getErrMsg());
						}
					});
				}
			}
			catch (Exception e) {
				log.error("消息通知异常!", e);
			}
		}
	}

	/**
	 * 发送通知
	 * @param sendMessage 发送的消息
	 * @return 返回消息发送状态，如果发送失败需要设置失败信息
	 * @author lingting 2020-06-12 00:37:23
	 */
	public abstract ExceptionNoticeResponse send(ExceptionMessage sendMessage);

}
