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

package org.ballcat.desensite;

import lombok.Data;
import lombok.experimental.Accessors;
import org.ballcat.desensite.custom.CustomerDesensitize;
import org.ballcat.desensitize.annotation.IndexDesensitize;
import org.ballcat.desensitize.annotation.RegexDesensitize;
import org.ballcat.desensitize.annotation.SimpleDesensitize;
import org.ballcat.desensitize.handler.PhoneNumberDesensitizationHandler;
import org.ballcat.desensitize.rule.regex.EmailRegexDesensitizeRule;

/**
 * @author Hccake 2021/1/23
 */
@Data
@Accessors(chain = true)
public class DesensitizationUser {

	/**
	 * 用户名，不脱敏
	 */
	private String username;

	/**
	 * 密码脱敏, 前3后2明文，中间无论多少位，都显示 4 个 *，已混淆位数
	 */
	@RegexDesensitize(regex = "(.{3}).*(.{2}$)", replacement = "$1****$2")
	private String password;

	/**
	 * 邮件
	 */
	@RegexDesensitize(rule = EmailRegexDesensitizeRule.class)
	private String email;

	/**
	 * 手机号
	 */
	@SimpleDesensitize(handler = PhoneNumberDesensitizationHandler.class)
	private String phoneNumber;

	/**
	 * 测试自定义脱敏
	 */
	@SimpleDesensitize(handler = TestDesensitizationHandler.class)
	private String testField;

	/**
	 * 测试自定义注解脱敏
	 */
	@CustomerDesensitize(type = "自定义注解示例")
	private String customDesensitize;

	/**
	 * 测试规则脱敏
	 */
	@IndexDesensitize(rule = { "1", "4-6", "9-" })
	private String ruleDesensitize;

	/**
	 * 测试规则脱敏（反转）
	 */
	@IndexDesensitize(rule = { "1", "4-6", "9-" }, reverse = true)
	private String ruleReverseDesensitize;

}
