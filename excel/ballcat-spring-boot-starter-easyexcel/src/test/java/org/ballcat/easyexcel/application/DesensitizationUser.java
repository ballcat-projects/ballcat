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

package org.ballcat.easyexcel.application;

import lombok.Data;
import lombok.experimental.Accessors;
import org.ballcat.desensitize.annotation.RegexDesensitize;
import org.ballcat.desensitize.rule.regex.EncryptedPasswordRegexDesensitizeRule;

/**
 * @author Hccake 2021/1/23
 *
 */
@Data
@Accessors(chain = true)
public class DesensitizationUser {

	/**
	 * 用户名，不脱敏
	 */
	private String username;

	/**
	 * 密码脱敏
	 */
	@RegexDesensitize(rule = EncryptedPasswordRegexDesensitizeRule.class)
	private String password;

}
