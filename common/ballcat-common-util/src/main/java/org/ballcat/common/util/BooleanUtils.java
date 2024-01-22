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

/**
 * @author lingting 2023-05-06 14:16
 */
public final class BooleanUtils {

	private BooleanUtils() {
	}

	private static final String[] STR_TRUE = { "1", "true", "yes", "ok", "y" };

	private static final String[] STR_FALSE = { "0", "false", "no", "n" };

	public static boolean isTrue(Object obj) {
		if (obj instanceof String) {
			return ArrayUtils.contains(STR_TRUE, obj);
		}
		if (obj instanceof Number) {
			return ((Number) obj).doubleValue() > 0;
		}
		if (obj instanceof Boolean) {
			return Boolean.TRUE.equals(obj);
		}
		return false;
	}

	public static boolean isFalse(Object obj) {
		if (obj instanceof String) {
			return ArrayUtils.contains(STR_FALSE, obj);
		}
		if (obj instanceof Number) {
			return ((Number) obj).doubleValue() <= 0;
		}
		if (obj instanceof Boolean) {
			return Boolean.FALSE.equals(obj);
		}
		return false;
	}

}
