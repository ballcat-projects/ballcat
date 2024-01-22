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

import java.util.Objects;

/**
 * @author lingting
 */
public final class ArrayUtils {

	private ArrayUtils() {
	}

	public static final int NOT_FOUNT = -1;

	public static <T> boolean isEmpty(T[] array) {
		return array == null || array.length == 0;
	}

	public static <T> int indexOf(T[] array, T val) {
		if (!isEmpty(array)) {
			for (int i = 0; i < array.length; i++) {
				T t = array[i];
				if (Objects.equals(t, val)) {
					return i;
				}
			}
		}
		return NOT_FOUNT;
	}

	public static <T> boolean contains(T[] array, T val) {
		return indexOf(array, val) > NOT_FOUNT;
	}

}
