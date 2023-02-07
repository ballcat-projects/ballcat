package com.hccake.ballcat.extend.ntp;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;

import java.net.InetAddress;

/**
 * ntp 校时服务
 *
 * @author lingting 2022/11/18 13:40
 */
@Slf4j
public class Ntp {

	private final long diff;

	/**
	 * 计算时差
	 */
	public static long diff(String host) {
		try {
			NTPUDPClient client = new NTPUDPClient();
			TimeInfo time = client.getTime(InetAddress.getByName(host));
			long systemMillis = System.currentTimeMillis();
			long ntpMillis = time.getMessage().getTransmitTimeStamp().getTime();
			return ntpMillis - systemMillis;
		}
		catch (Exception e) {
			throw new NtpException("ntp初始化异常!", e);
		}
	}

	public Ntp(String host) {
		try {
			diff = diff(host);
			log.warn("授时中心时间与系统时间差为 {} 毫秒", diff);
		}
		catch (Exception e) {
			throw new NtpException("ntp初始化异常!", e);
		}
	}

	public long currentMillis() {
		return System.currentTimeMillis() + diff;
	}

	/**
	 * 切换授时中心
	 * @return 返回新的ntp实例
	 */
	public Ntp use(String host) {
		return new Ntp(host);
	}

}
