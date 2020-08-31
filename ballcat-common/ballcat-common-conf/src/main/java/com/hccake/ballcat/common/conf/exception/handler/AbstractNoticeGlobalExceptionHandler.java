package com.hccake.ballcat.common.conf.exception.handler;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import com.hccake.ballcat.common.conf.config.ExceptionHandleConfig;
import com.hccake.ballcat.common.conf.exception.domain.ExceptionMessage;
import com.hccake.ballcat.common.conf.exception.domain.ExceptionNoticeResponse;
import com.hccake.ballcat.common.core.exception.handler.GlobalExceptionHandler;
import com.hccake.ballcat.common.core.thread.AbstractQueueThread;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 消息通知顶层类
 *
 * @author lingting 2020/6/12 0:35
 */
@Slf4j
public abstract class AbstractNoticeGlobalExceptionHandler extends AbstractQueueThread<ExceptionMessage>
		implements GlobalExceptionHandler {

	private static final String NULL_MESSAGE_KEY = "";

	protected final ExceptionHandleConfig config;

	/**
	 * 通知消息存放 e.message 堆栈信息
	 */
	private final Map<String, ExceptionMessage> messages = new ConcurrentHashMap<>(50);

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
	}

	@Override
	public long getBatchSize() {
		return config.getMax();
	}

	@Override
	public long getBatchTimeout() {
		// 秒转毫秒
		return config.getTime() * 1000;
	}

	@Override
	public void startLog() {
		log.debug("异常通知线程启动");
		setName("exception-notice-thread-" + config.getType().name());
	}

	@Override
	public void errorLog(Throwable e, List<ExceptionMessage> list) {
		log.error("异常通知发生异常", e);
	}

	@Override
	public void save(List<ExceptionMessage> list) throws Exception {
		messages.clear();
		for (ExceptionMessage msg : list) {
			// 已存在相同key
			if (messages.containsKey(msg.getKey())) {
				messages.put(msg.getKey(), msg.setNumber(messages.get(msg.getKey()).getNumber() + 1));
			}
			else {
				messages.put(msg.getKey(), msg);
			}
		}
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
	}

	@Override
	public void handle(Throwable e) {
		putObject(new ExceptionMessage().setNumber(1).setKey(e.getMessage() == null ? NULL_MESSAGE_KEY : e.getMessage())
				.setMac(mac).setApplicationName(applicationName).setHostname(hostname).setIp(ip)
				.setStack(ExceptionUtil.stacktraceToString(e, config.getLength()).replaceAll("\\r", ""))
				.setTime(DateUtil.now()).setThreadId(Thread.currentThread().getId()));
	}

	/**
	 * 发送通知
	 * @param sendMessage 发送的消息
	 * @return 返回消息发送状态，如果发送失败需要设置失败信息
	 * @author lingting 2020-06-12 00:37:23
	 */
	public abstract ExceptionNoticeResponse send(ExceptionMessage sendMessage);

}
