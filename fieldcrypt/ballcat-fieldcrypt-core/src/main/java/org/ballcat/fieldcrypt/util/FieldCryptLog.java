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

package org.ballcat.fieldcrypt.util;

/**
 * 简化工具：仅保留掩码函数，避免任何一次性日志设计与状态缓存。
 *
 * @author Hccake
 * @since 2.0.0
 */
public final class FieldCryptLog {

	private FieldCryptLog() {
	}

	public static String mask(String s, int keepPrefix, int keepSuffix) {
		if (s == null) {
			return null;
		}
		int len = s.length();
		if (len <= keepPrefix + keepSuffix) {
			return repeat('*', Math.max(1, len));
		}
		String pre = s.substring(0, Math.max(0, keepPrefix));
		String suf = s.substring(len - Math.max(0, keepSuffix));
		return pre + repeat('*', len - pre.length() - suf.length()) + suf;
	}

	private static String repeat(char ch, int count) {
		if (count <= 0) {
			return "";
		}
		char[] arr = new char[count];
		java.util.Arrays.fill(arr, ch);
		return new String(arr);
	}

}
