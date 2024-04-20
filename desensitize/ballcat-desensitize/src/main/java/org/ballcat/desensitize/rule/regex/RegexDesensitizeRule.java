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
 * 正则脱敏规则。 使用正则匹配原始数据，然后根据替换规则进行脱敏处理，
 *
 * @author Hccake
 */
public interface RegexDesensitizeRule {

	/**
	 * 匹配的正则表达式
	 */
	String getRegex();

	/**
	 * 替换规则
	 */
	String getReplacement();

}
