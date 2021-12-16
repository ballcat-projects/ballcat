package com.hccake.starter.sms.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lingting 2020/5/6 21:41
 */
public class SmsSendConstants {

	@Getter
	@AllArgsConstructor
	public enum state {

		/**
		 * 状态值 说明
		 */
		NOT_SEND(0, "未发送"),

		SENDING(1, "已在发送队列"),

		SUCCESS(2, "发送成功"),

		FAILED(3, "发送失败"),

		;

		private final int val;

		private final String text;

	}

	@Getter
	@AllArgsConstructor
	public enum type {

		/**
		 * 状态值 说明
		 */
		SMS(0, "短信"),

		MMS(1, "彩信"),

		;

		private final int val;

		private final String text;

	}

}
