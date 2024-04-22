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
 * 【密文】脱敏，前3后2，中间替换为 4个 * eg. 3950587458326514452641976780061 -> 395****61
 *
 * @author evil0th
 * @since 2.0.0
 */
public class KeyRegexDesensitizeRule implements RegexDesensitizeRule {

	@Override
	public String getRegex() {
		return "(.{3}).*(.{2}$)";
	}

	@Override
	public String getReplacement() {
		return "$1****$2";
	}

}
