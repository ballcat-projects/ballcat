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

package org.ballcat.fastexcel.processor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Excel 名称解析 Root 对象，用于提供一些常用方法。
 *
 * @author Hccake
 */
public final class ExcelNameParseRoot {

	private ExcelNameParseRoot() {
	}

	public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

	public static String currentDate() {
		return LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE);
	}

	public static String currentDateTime() {
		return LocalDateTime.now().format(DATE_TIME_FORMATTER);
	}

	public static String currentTimeMillis() {
		return String.valueOf(System.currentTimeMillis());
	}

}
