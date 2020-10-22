package com.hccake.starter.sms.exception;

/**
 * @author lingting 2020/4/26 10:16
 */
public class SmsException extends RuntimeException {

	public SmsException(String message) {
		super(message);
	}

	public SmsException(String message, Throwable cause) {
		super(message, cause);
	}

}
