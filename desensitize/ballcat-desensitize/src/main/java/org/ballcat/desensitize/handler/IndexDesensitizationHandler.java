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

import org.ballcat.desensitize.rule.index.IndexDesensitizeRule;

/**
 * 基于规则的替换脱敏处理器，如指定"3-6，8，10-"表示第4，5，6，7，9，11以及11之后的位替换处理
 *
 * @author evil0th Create on 2024/4/12
 */
public class IndexDesensitizationHandler implements DesensitizationHandler {

	/**
	 * 基于规则的替换字符串
	 *
	 * <pre>
	 *     handle("43012319990101432X", "1", "4-6", "9-")) = "4*01***99*********"
	 * </pre>
	 * @param input 输入字符串
	 * @param rule 规则
	 * @return 脱敏字符串
	 */
	public String mask(String input, String... rule) {
		return mask(input, false, rule);
	}

	/**
	 * 基于规则的替换字符串(支持反转)
	 *
	 * <pre>
	 *     handle("43012319990101432X", true, "1", "4-6", "9-")) = "4*01***99*********"
	 * </pre>
	 * @param input 输入字符串
	 * @param rule 规则
	 * @param reverse 是否反转规则
	 * @return 脱敏字符串
	 */
	public String mask(String input, boolean reverse, String... rule) {
		return mask(input, '*', reverse, rule);
	}

	/**
	 * 基于规则的替换字符串
	 *
	 * <pre>
	 *     handle("43012319990101432X", '-', false, "1", "4-6", "9-")) = "4-01---99---------"
	 *     handle("43012319990101432X", '-', true, "1", "4-6", "9-")) = "-3--231--90101432X"
	 * </pre>
	 * @param input 输入字符串
	 * @param rule 规则。{@link IndexDesensitizeRule}
	 * @param symbol 符号，默认*
	 * @param reverse 是否反转规则
	 * @return 脱敏字符串
	 */
	public String mask(String input, char symbol, boolean reverse, String... rule) {
		return mask(input, symbol, reverse, IndexDesensitizeRule.analysis(rule));
	}

	/**
	 * 基于规则的替换字符串
	 * @param origin 输入字符串
	 * @param rule 规则。{@link IndexDesensitizeRule}
	 * @param symbol 符号，默认*
	 * @param reverse 是否反转规则
	 * @return 脱敏字符串
	 */
	private String mask(String origin, char symbol, boolean reverse, IndexDesensitizeRule rule) {
		if (origin == null) {
			return null;
		}
		char[] clearChars = origin.toCharArray();
		for (int i = 0; i < clearChars.length; ++i) {
			if (reverse ^ rule.isIn(i)) {
				clearChars[i] = symbol;
			}
		}
		return new String(clearChars);
	}

}
