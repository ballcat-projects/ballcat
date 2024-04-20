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

import org.ballcat.desensitize.handler.SlideDesensitizationHandler;
import org.ballcat.desensitize.rule.slide.NoneSlideDesensitizeRule;
import org.ballcat.desensitize.rule.slide.SlideDesensitizeRule;

/**
 * 使用滑动脱敏处理器对值进行脱敏处理.
 *
 * @see SlideDesensitizationHandler
 * @author Hccake 2021/1/22
 *
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SlideDesensitize {

	/**
	 * 脱敏类型，只有当值为 NoneSlideDesensitizeRule 时，以下三个参数才有效
	 * @see SlideDesensitizeRule
	 * @return type
	 */
	Class<? extends SlideDesensitizeRule> rule() default NoneSlideDesensitizeRule.class;

	/**
	 * 左边的明文数，只有当type值为 CUSTOM 时，才生效
	 */
	int leftPlainTextLen() default 0;

	/**
	 * 右边的明文数，只有当type值为 CUSTOM 时，才生效
	 */
	int rightPlainTextLen() default 0;

	/**
	 * 剩余部分字符逐个替换的字符串，只有当type值为 CUSTOM 时，才生效
	 */
	String maskString() default "*";

	/**
	 * 是否反转
	 */
	boolean reverse() default false;

}
