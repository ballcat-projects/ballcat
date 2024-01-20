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

import lombok.Getter;

/**
 * 访问日志规则
 *
 * @author Hccake
 */
@Getter
public class AccessLogRule {

	/**
	 * 当前设置匹配的 url 规则（Ant风格）
	 */
	private final String urlPattern;

	/**
	 * 日志记录选项
	 */
	private final AccessLogRecordOptions options;

	public AccessLogRule(String urlPattern, AccessLogRecordOptions options) {
		this.urlPattern = urlPattern;
		this.options = options;
	}

}
