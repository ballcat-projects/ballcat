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

package org.ballcat.desensitize.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Hccake 2021/1/23
 *
 */
@Getter
@RequiredArgsConstructor
public enum RegexDesensitizationTypeEnum {

	/**
	 * 自定义类型
	 */
	CUSTOM("^[\\s\\S]*$", "******"),

	/**
	 * 【邮箱】脱敏，保留邮箱第一个字符和'@'之后的原文显示，中间的显示为4个* eg. 12@qq.com -> 1****@qq.com
	 */
	EMAIL("(^.)[^@]*(@.*$)", "$1****$2"),

	/**
	 * 【对称密文的密码】脱敏，前3后2，中间替换为 4个 *
	 */
	ENCRYPTED_PASSWORD("(.{3}).*(.{2}$)", "$1****$2");

	/**
	 * 匹配的正则表达式
	 */
	private final String regex;

	/**
	 * 替换规则
	 */
	private final String replacement;

}
