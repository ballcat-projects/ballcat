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

package org.ballcat.desensitize.rule.regex;

/**
 * 【邮箱】脱敏，保留邮箱第一个字符和'@'之后的原文显示，中间的显示为4个* eg. 12@qq.com -> 1****@qq.com
 *
 * @author Hccake
 * @since 2.0.0
 */
public class EmailRegexDesensitizeRule implements RegexDesensitizeRule {

	@Override
	public String getRegex() {
		return "(^.)[^@]*(@.*$)";
	}

	@Override
	public String getReplacement() {
		return "$1****$2";
	}

}
