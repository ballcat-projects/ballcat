package com.hccake.ballcat.extend.ntp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author lingting 2022/11/18 11:51
 */
class NtpTest {

	@Test
	void ntp() {
		Ntp ntp = new Ntp(NtpCn.DEFAULT_TIME_SERVER_SH_IP);
		System.out.println("服务时间: " + ntp.currentMillis());
		System.out.println("系统时间: " + System.currentTimeMillis());
		Assertions.assertNotNull(ntp);
	}

}
