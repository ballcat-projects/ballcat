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
package org.ballcat.sms.constant;

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
