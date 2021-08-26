package com.hccake.ballcat.common.datascope.interceptor;

import com.hccake.ballcat.common.datascope.annotation.DataPermission;
import org.springframework.core.MethodClassKey;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * {@link DataPermission} 注解的查找者。用于查询当前方法对应的 DataPermission 注解环境，当方法上没有找到时，会去类上寻找。
 *
 * @author hccake
 */
@DataPermission
public final class DataPermissionFinder {

	private DataPermissionFinder() {

	}

	private static final Map<Object, DataPermission> DATA_PERMISSION_CACHE = new ConcurrentHashMap<>(1024);

	/**
	 * 提供一个默认的空值注解，用于缓存空值占位使用
	 */
	private static final DataPermission EMPTY_DATA_PERMISSION = DataPermissionFinder.class
			.getAnnotation(DataPermission.class);

	/**
	 * 缓存的 key 值
	 * @param method 方法
	 * @return key
	 */
	private static Object getCacheKey(Method method) {
		return new MethodClassKey(method, method.getDeclaringClass());
	}

	/**
	 * 从缓存中获取数据权限注解 优先获取方法上的注解，再获取类上的注解
	 * @param method 当前方法
	 * @return 当前方法有效的数据权限注解
	 */
	public static DataPermission findDataPermission(Method method) {
		Object methodKey = getCacheKey(method);

		if (DATA_PERMISSION_CACHE.containsKey(methodKey)) {
			DataPermission dataPermission = DATA_PERMISSION_CACHE.get(methodKey);
			// 判断是否和缓存的空注解是同一个对象
			return EMPTY_DATA_PERMISSION == dataPermission ? null : dataPermission;
		}

		// 先查方法，如果方法上没有，则使用类上
		DataPermission dataPermission = AnnotatedElementUtils.findMergedAnnotation(method, DataPermission.class);
		if (dataPermission == null) {
			dataPermission = AnnotatedElementUtils.findMergedAnnotation(method.getDeclaringClass(),
					DataPermission.class);
		}
		DATA_PERMISSION_CACHE.put(methodKey, dataPermission == null ? EMPTY_DATA_PERMISSION : dataPermission);
		return dataPermission;
	}

}
