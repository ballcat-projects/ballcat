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

package org.ballcat.desensitize.logging.logback;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 过滤器持有者：供 Logback ConversionRule 读取。由 Starter 注入最新过滤器实例。
 *
 * @author Hccake
 */
public final class LoggerDesensitizationFilterHolder {

	private static final AtomicReference<LoggerDesensitizationFilter> REF = new AtomicReference<>();

	private LoggerDesensitizationFilterHolder() {
	}

	public static void set(LoggerDesensitizationFilter filter) {
		REF.set(filter);
	}

	public static LoggerDesensitizationFilter get() {
		return REF.get();
	}

}
