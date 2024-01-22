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

package org.ballcat.common.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * @author lingting 2022/11/28 10:12
 */
public final class LocalDateTimeUtils {

	private LocalDateTimeUtils() {
	}

	public static final ZoneOffset DEFAULT_ZONE_OFFSET = ZoneOffset.of("+8");

	public static final ZoneId DEFAULT_ZONE_ID = DEFAULT_ZONE_OFFSET.normalized();

	public static final String STRING_FORMATTER_YMD_HMS = "yyyy-MM-dd HH:mm:ss";

	public static final DateTimeFormatter FORMATTER_YMD_HMS = DateTimeFormatter.ofPattern(STRING_FORMATTER_YMD_HMS);

	public static final String STRING_FORMATTER_YMD = "yyyy-MM-dd";

	public static final DateTimeFormatter FORMATTER_YMD = DateTimeFormatter.ofPattern(STRING_FORMATTER_YMD);

	public static final String STRING_FORMATTER_HMS = "HH:mm:ss";

	public static final DateTimeFormatter FORMATTER_HMS = DateTimeFormatter.ofPattern(STRING_FORMATTER_HMS);

	// region LocalDateTime

	/**
	 * 字符串转时间
	 * @param str yyyy-MM-dd HH:mm:ss 格式字符串
	 * @return java.time.LocalDateTime 时间
	 */
	public static LocalDateTime parse(String str) {
		return LocalDateTime.parse(str, FORMATTER_YMD_HMS);
	}

	/**
	 * 时间戳转时间, 使用 GMT+8 时区
	 * @param timestamp 时间戳 - 毫秒
	 * @return java.time.LocalDateTime
	 */
	public static LocalDateTime parse(Long timestamp) {
		return parse(timestamp, DEFAULT_ZONE_ID);
	}

	/**
	 * 时间戳转时间
	 * @param timestamp 时间戳 - 毫秒
	 * @param zoneId 时区
	 * @return java.time.LocalDateTime
	 */
	public static LocalDateTime parse(Long timestamp, ZoneId zoneId) {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), zoneId);
	}

	public static Long toTimestamp(LocalDateTime dateTime) {
		return toTimestamp(dateTime, DEFAULT_ZONE_OFFSET);
	}

	public static Long toTimestamp(LocalDateTime dateTime, ZoneOffset offset) {
		return dateTime.toInstant(offset).toEpochMilli();
	}

	public static String format(LocalDateTime dateTime) {
		return format(dateTime, FORMATTER_YMD_HMS);
	}

	public static String format(LocalDateTime dateTime, String formatter) {
		return format(dateTime, DateTimeFormatter.ofPattern(formatter));
	}

	public static String format(LocalDateTime dateTime, DateTimeFormatter formatter) {
		return formatter.format(dateTime);
	}

	// endregion

	// region LocalDate
	public static LocalDate parseDate(String str) {
		return LocalDate.parse(str, FORMATTER_YMD);
	}

	public static String format(LocalDate date) {
		return format(date, FORMATTER_YMD);
	}

	public static String format(LocalDate date, String formatter) {
		return format(date, DateTimeFormatter.ofPattern(formatter));
	}

	public static String format(LocalDate date, DateTimeFormatter formatter) {
		return formatter.format(date);
	}

	// endregion

	// region LocalTime
	public static LocalTime parseTime(String str) {
		return LocalTime.parse(str, FORMATTER_HMS);
	}

	public static String format(LocalTime time) {
		return format(time, FORMATTER_HMS);
	}

	public static String format(LocalTime time, String formatter) {
		return format(time, DateTimeFormatter.ofPattern(formatter));
	}

	public static String format(LocalTime time, DateTimeFormatter formatter) {
		return formatter.format(time);
	}

	// endregion

}
