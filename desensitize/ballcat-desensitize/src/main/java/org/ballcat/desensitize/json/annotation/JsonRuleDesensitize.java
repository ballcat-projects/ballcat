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

package org.ballcat.desensitize.json.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.ballcat.desensitize.handler.RuleDesensitizationHandler;
import org.ballcat.desensitize.rule.DesensitizeRule;

/**
 * Jackson Filed 序列化脱敏注解, 对应基于规则脱敏处理器对值进行脱敏处理
 *
 * @author evil0th Create on 2024/4/12
 * @see RuleDesensitizationHandler
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JsonRuleDesensitize {

	/**
	 * 脱敏规则
	 * @return type
	 * @see DesensitizeRule
	 */
	String[] rule();

	/**
	 * 是否反转规则
	 */
	boolean reverse() default false;

	/**
	 * 替换的字符串
	 */
	char maskChar() default '*';

}
