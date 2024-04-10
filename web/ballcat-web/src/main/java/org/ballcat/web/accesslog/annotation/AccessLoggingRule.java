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

package org.ballcat.web.accesslog.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.ballcat.web.accesslog.AbstractAccessLogFilter;

/**
 * @author Alickx 2023/11/23 17:48
 * @since 2.0.0
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AccessLoggingRule {

	/**
	 * 忽略记录
	 */
	boolean ignored() default false;

	/**
	 * 记录查询参数
	 */
	boolean includeQueryString() default false;

	/**
	 * 记录请求体
	 */
	boolean includeRequestBody() default false;

	/**
	 * 记录响应体
	 */
	boolean includeResponseBody() default false;

	/**
	 * 记录的最大的请求 body 长度
	 */
	int maxRequestBodyLength() default AbstractAccessLogFilter.DEFAULT_MAX_BODY_LENGTH;

	/**
	 * 记录的最大的响应 body 长度
	 */
	int maxResponseBodyLength() default AbstractAccessLogFilter.DEFAULT_MAX_BODY_LENGTH;

}
