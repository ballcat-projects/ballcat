/*
 * Copyright 2023 the original author or authors.
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

/**
 * 访问日志记录规则
 *
 * @author Hccake
 */
@Builder
public class AccessLogSettings {

	public static final AccessLogSettings IGNORED = new AccessLogSettings(false, false, false, false);

	/**
	 * 开启日志记录
	 */
	private final boolean enabled;

	/**
	 * 记录 QueryString
	 */
	private final boolean includeQueryString;

	/**
	 * 记录请求体
	 */
	private final boolean includeRequestPayload;

	/**
	 * 记录响应体
	 */
	private final boolean includeResponsePayload;

	public boolean enabled() {
		return this.enabled;
	}

	public boolean shouldRecordQueryString() {
		return this.includeQueryString;
	}

	public boolean shouldRecordRequestPayload() {
		return this.includeRequestPayload;
	}

	public boolean shouldRecordResponsePayload() {
		return this.includeResponsePayload;
	}

}
