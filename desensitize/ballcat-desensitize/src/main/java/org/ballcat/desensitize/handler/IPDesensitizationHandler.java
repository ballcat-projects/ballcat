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

package org.ballcat.desensitize.handler;

/**
 * 【IP】IPv4返回10.*.*.*，IPv6返回2001:*:*:*:*:*:*:*
 *
 * @author evil0th Create on 2024/4/12
 */
public class IPDesensitizationHandler implements SimpleDesensitizationHandler {

	/**
	 * 脱敏处理
	 * @param origin 原始IP
	 * @return 脱敏处理后的IP
	 */
	@Override
	public String mask(String origin) {
		if (null == origin) {
			return null;
		}
		int index = origin.indexOf(".");
		if (index > 0) {
			return origin.substring(0, index) + ".*.*.*";
		}
		index = origin.indexOf(":");
		if (index > 0) {
			return origin.substring(0, index) + ":*:*:*:*:*:*:*";
		}
		return origin;
	}

}
