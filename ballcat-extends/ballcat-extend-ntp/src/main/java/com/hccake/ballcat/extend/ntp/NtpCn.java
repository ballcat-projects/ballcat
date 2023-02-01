package com.hccake.ballcat.extend.ntp;

import lombok.experimental.UtilityClass;

/**
 * 中国 ntp 类
 *
 * @author lingting 2023/2/1 14:10
 */
@UtilityClass
public class NtpCn {

	/**
	 * time.7x24s.com 中国国家授时中心, 使用域名请求超时的话, 解析IP,然后直接请IP
	 */
	public static final String DEFAULT_TIME_SERVER = "time.7x24s.com";

	/**
	 * 上海解析出来的ip
	 */
	public static final String DEFAULT_TIME_SERVER_SH_IP = "203.107.6.88";

	private static final Ntp INSTANCE = new Ntp(DEFAULT_TIME_SERVER_SH_IP);

	public static long currentTimeMillis() {
		return INSTANCE.currentMillis();
	}

	/**
	 * 几秒后的毫秒级时间戳
	 * @param seconds 秒
	 * @return 时间戳
	 */
	public static long plusSeconds(long seconds) {
		return currentTimeMillis() + (seconds * 1000);
	}

}
