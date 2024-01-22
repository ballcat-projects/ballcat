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

package org.ballcat.pay.wx.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lingting 2021/1/29 18:14
 */
@Getter
@AllArgsConstructor
public enum SignType {

	/**
	 * 一般用于沙箱环境
	 */
	MD5("MD5"),
	/**
	 * 一般用于正式环境
	 */
	HMAC_SHA256("HMAC-SHA256");

	private final String str;

	public static SignType of(String str) {
		for (SignType e : values()) {
			if (e.str.equals(str)) {
				return e;
			}
		}
		return null;
	}

}
