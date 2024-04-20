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
 * 手机号脱敏处理器。 脱敏时保留特殊字符，如空格、-、（）等。 对于手机号纯数字位数小于等于 8 的号码，保留第一个数字和最后两个数字，其他用 * 替换。
 * 对于手机号纯数字位数大于 8 的号码，保留前三位和后四位，其他用 * 替换。 eg. - 13845351234 => 138****1234 - 138-4535-1234
 * => 138-****-123 - 1-4535-34 => 1-****-*23
 *
 * @author Hccake
 * @since 2.0.0
 */
public class PhoneNumberDesensitizationHandler implements SimpleDesensitizationHandler {

	@Override
	public String mask(String origin) {
		if (origin == null) {
			return null;
		}

		StringBuilder result = new StringBuilder();
		int numberCount = 0;

		for (char c : origin.toCharArray()) {
			if (Character.isDigit(c)) {
				numberCount++;
			}
		}

		int firstDigitsToKeep;
		int lastDigitsToKeep;
		if (numberCount <= 8) {
			firstDigitsToKeep = 1;
			lastDigitsToKeep = 2;
		}
		else {
			firstDigitsToKeep = 3;
			lastDigitsToKeep = 4;
		}

		int digitCount = 0;
		for (char c : origin.toCharArray()) {
			if (Character.isDigit(c)) {
				digitCount++;
				if (digitCount <= firstDigitsToKeep || digitCount > numberCount - lastDigitsToKeep) {
					result.append(c);
				}
				else {
					result.append("*");
				}
			}
			else {
				result.append(c);
			}
		}

		return result.toString();
	}

}
