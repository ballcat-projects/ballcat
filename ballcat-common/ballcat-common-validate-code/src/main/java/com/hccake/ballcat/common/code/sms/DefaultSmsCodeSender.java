package com.hccake.ballcat.common.code.sms;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * The default SMS verification code sender
 *
 * @author xm.z
 */
@Slf4j
public class DefaultSmsCodeSender implements SmsCodeSender {

	@Override
	public void send(String mobile, String code, Map<String, String> extra) {
		log.warn("请配置真实的短信验证码发送器[SmsCodeSender]");
		log.info("向手机:[{}] 发送的短信验证码为：[{}]", mobile, code);
	}

}
