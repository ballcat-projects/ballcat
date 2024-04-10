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

package org.ballcat.web.accesslog;

import lombok.Builder;
import lombok.Getter;

/**
 * 访问日志的记录选项
 *
 * @author Hccake
 * @since 2.0.0
 */
@Getter
@Builder
public class AccessLogRecordOptions {

	/**
	 * 忽略记录
	 */
	private boolean ignored;

	/**
	 * 记录查询参数
	 */
	private boolean includeQueryString;

	/**
	 * 记录请求体
	 */
	private boolean includeRequestBody;

	/**
	 * 记录响应体
	 */
	private boolean includeResponseBody;

	/**
	 * 记录的最大的请求 body 长度
	 */
	private int maxRequestBodyLength;

	/**
	 * 记录的最大的响应 body 长度
	 */
	private int maxResponseBodyLength;

}
