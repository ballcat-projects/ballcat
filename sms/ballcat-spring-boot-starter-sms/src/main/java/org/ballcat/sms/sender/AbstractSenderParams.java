/*
 * Copyright 2023-2024 the original author or authors.
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

package org.ballcat.sms.sender;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

/**
 * @author lingting 2023-10-16 19:34
 */
@Getter
@Setter
public abstract class AbstractSenderParams<P extends AbstractSenderParams<P>> {

	/**
	 * 下发手机号码，采用 e.164 标准，格式为+[国家或地区码][手机号]，单次请求最多支持200个手机号且要求全为境内手机号或全为境外手机号。
	 * 例如：+8613711112222， 其中前面有一个+号 ，86为国家码，13711112222为手机号。
	 */
	private Set<String> phoneNumbers = new HashSet<>();

	/**
	 * 短信内容
	 */
	private String content;

	/**
	 * 国家代码 比如 CN
	 */
	private String country;

	/**
	 * 添加手机号
	 */
	public P addPhone(String phone) {
		this.phoneNumbers.add(phone);
		return (P) this;
	}

}
