package com.hccake.ballcat.common.util;

import lombok.experimental.UtilityClass;

import java.util.Objects;

/**
 * @author lingting
 */
@UtilityClass
public class ArrayUtils {

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
