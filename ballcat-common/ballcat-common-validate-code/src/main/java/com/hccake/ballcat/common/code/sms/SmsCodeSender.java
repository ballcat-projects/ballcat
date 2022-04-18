package com.hccake.ballcat.common.code.sms;

import java.util.Map;

/**
 * SmsCodeSender
 *
 * @author xm.z
 */
public interface SmsCodeSender {

	/**
	 * Send SMS code
	 * @param mobile Recipient's mobile phone number
	 * @param code Verification code
	 * @param extra Extra parameters
	 */
	void send(String mobile, String code, Map<String, String> extra);

}