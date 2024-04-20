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

import org.ballcat.desensitize.rule.regex.RegexDesensitizeRule;

/**
 * 正则替换脱敏处理器，使用正则匹配替换处理原数据
 *
 * @author Hccake 2021/1/23
 *
 */
public class RegexDesensitizationHandler implements DesensitizationHandler {

	/**
	 * 正则脱敏处理
	 * @param origin 原文
	 * @param regex 正则匹配规则
	 * @param replacement 替换模板
	 * @return 脱敏后的字符串
	 */
	public String mask(String origin, String regex, String replacement) {
		return origin.replaceAll(regex, replacement);
	}

	/**
	 * 正则脱敏处理
	 * @param origin 原文
	 * @param regexDesensitizeRule 正则脱敏规则
	 * @return 脱敏后的字符串
	 */
	public String mask(String origin, RegexDesensitizeRule regexDesensitizeRule) {
		return origin.replaceAll(regexDesensitizeRule.getRegex(), regexDesensitizeRule.getReplacement());
	}

}
