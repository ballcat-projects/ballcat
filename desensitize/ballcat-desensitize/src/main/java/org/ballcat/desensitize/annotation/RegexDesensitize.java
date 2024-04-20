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

package org.ballcat.desensitize.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.ballcat.desensitize.handler.RegexDesensitizationHandler;
import org.ballcat.desensitize.rule.regex.NoneRegexDesensitizeRule;
import org.ballcat.desensitize.rule.regex.RegexDesensitizeRule;

/**
 * 使用正则脱敏处理器对值进行脱敏处理.
 *
 * @see RegexDesensitizationHandler
 * @author Hccake 2021/1/22
 *
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RegexDesensitize {

	/**
	 * 脱敏类型，用于指定正则处理方式。 只有当值为 NoneRegexDesensitizeRule.class 时，以下两个参数才有效
	 * @see NoneRegexDesensitizeRule
	 * @return type
	 */
	Class<? extends RegexDesensitizeRule> rule() default NoneRegexDesensitizeRule.class;

	/**
	 * 匹配的正则表达式，只有当type值为 CUSTOM 时，才生效
	 */
	String regex() default "^[\\s\\S]*$";

	/**
	 * 替换规则，只有当type值为 CUSTOM 时，才生效
	 */
	String replacement() default "******";

}
