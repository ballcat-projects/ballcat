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

import java.lang.reflect.Array;

/**
 * StringUtils 扩展
 *
 * @author <a href="mailto:cs.liaow@gmail.com">evil0th</a> Create on 2023/3/6
 *
 */
public final class StringUtils {

	private StringUtils() {
	}

	/**
	 * 左补位操作
	 * @param origin 原始字符串
	 * @param length 输出总长度
	 * @param pad 补位字符
	 * @return string
	 */
	public static String leftPad(String origin, int length, char pad) {
		return pad("", origin, length, pad);
	}

	/**
	 * 右补位操作
	 * @param origin 原始字符串
	 * @param length 输出总长度
	 * @param pad 补位字符
	 * @return string
	 */
	public static String rightPad(String origin, int length, char pad) {
		return pad("-", origin, length, pad);
	}

	/**
	 * 补位操作
	 * @param type 类型 left: "" 或 right: "-"
	 * @param origin 原始字符串
	 * @param length 输出总长度
	 * @param pad 补位字符
	 * @return string
	 */
	public static String pad(String type, String origin, int length, char pad) {
		if (null == type) {
			return origin;
		}
		return String.format("%1$" + type + length + "s", origin).replace(' ', pad);
	}

	/**
	 * 是否包含非空字符
	 * @param str 字符
	 * @return 是否
	 */
	public static boolean containsText(CharSequence str) {
		int strLen = str.length();
		for (int i = 0; i < strLen; ++i) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断多个串是否为空（包含空白串）
	 * @param css 输入参数
	 * @return string
	 */
	public static boolean isAnyBlank(String... css) {
		if (null == css || Array.getLength(css) == 0) {
			return true;
		}
		else {
			for (String cs : css) {
				if (cs == null || cs.isEmpty() && containsText(cs)) {
					return true;
				}
			}
			return false;
		}
	}

	/**
	 * 判断多个串是否同时不为空（包含空白串）
	 * @param css 输入参数
	 * @return string
	 */
	public static boolean isNoneBlank(String... css) {
		return !isAnyBlank(css);
	}

	/**
	 * 去掉不间断空格(non-breaking space),ASCII码值160和32
	 * @param str 原始
	 * @return 替换后的
	 */
	public static String trimNbsp(String str) {
		return str == null ? null : str.replaceAll("\\u00A0+", "");
	}

	/**
	 * @param text 原始
	 * @return 下划线
	 */
	public static String toUnderlineCase(String text) {
		return text == null ? null : text.replaceAll("(.)(\\p{Lu})", "$1_$2").toLowerCase();
	}

}
