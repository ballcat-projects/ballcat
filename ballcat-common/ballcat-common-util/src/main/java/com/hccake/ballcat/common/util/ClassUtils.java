package com.hccake.ballcat.common.util;

import cn.hutool.core.util.ClassUtil;

/**
 * @author lingting 2021/2/25 21:17
 */
public class ClassUtils extends ClassUtil {

	/**
	 *
	 * 确定class是否可以被加载
	 * @param className 完整类名
	 * @param classLoader 类加载
	 * @author lingting 2021-02-25 21:17
	 */
	public static boolean isPresent(String className, ClassLoader classLoader) {
		try {
			Class.forName(className, true, classLoader);
			return true;
		}
		catch (Throwable ex) {
			return false;
		}
	}

}
