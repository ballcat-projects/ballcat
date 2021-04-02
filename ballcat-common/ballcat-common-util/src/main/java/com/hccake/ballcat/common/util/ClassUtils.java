package com.hccake.ballcat.common.util;

import cn.hutool.core.util.ClassUtil;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lingting 2021/2/25 21:17
 */
public class ClassUtils extends ClassUtil {

	private static final Map<String, Boolean> CACHE = new ConcurrentHashMap<>(8);

	/**
	 * 确定class是否可以被加载
	 * @param className 完整类名
	 * @param classLoader 类加载
	 * @author lingting 2021-02-25 21:17
	 */
	public static boolean isPresent(String className, ClassLoader classLoader) {
		if (CACHE.containsKey(className)) {
			return CACHE.get(className);
		}
		try {
			Class.forName(className, true, classLoader);
			CACHE.put(className, true);
			return true;
		}
		catch (Exception ex) {
			CACHE.put(className, false);
			return false;
		}
	}

}
