package com.hccake.ballcat.common.util;

import lombok.experimental.UtilityClass;

/**
 * @author lingting 2023-05-06 14:16
 */
@UtilityClass
public class BooleanUtils {

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
