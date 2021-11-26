package com.hccake.ballcat.common.datascope.util;

import java.util.Collection;

/**
 * Collection 工具类
 *
 * @author hccake
 */
public final class CollectionUtils {

	private CollectionUtils() {
	}

	/**
	 * 校验集合是否为空
	 * @param collection 集合
	 * @return boolean
	 */
	public static boolean isEmpty(Collection<?> collection) {
		return collection == null || collection.isEmpty();
	}

	/**
	 * 校验集合是否不为空
	 * @param collection 集合
	 * @return boolean
	 */
	public static boolean isNotEmpty(Collection<?> collection) {
		return !isEmpty(collection);
	}

}
