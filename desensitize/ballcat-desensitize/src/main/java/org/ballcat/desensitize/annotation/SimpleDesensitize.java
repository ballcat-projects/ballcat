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

import org.ballcat.desensitize.handler.SimpleDesensitizationHandler;

/**
 * 使用 Simple 脱敏处理器对值进行脱敏处理.
 *
 * @see SimpleDesensitizationHandler
 * @author Hccake 2021/1/22
 *
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SimpleDesensitize {

	/**
	 * 脱敏类型，用于指定脱敏处理器
	 * @return type
	 */
	Class<? extends SimpleDesensitizationHandler> handler();

}
