package com.hccake.ballcat.common.datascope.interceptor;

import com.hccake.ballcat.common.datascope.annotation.DataPermission;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.MethodClassKey;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.ClassUtils;

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
	 * @param clazz 类
	 * @return key
	 */
	private static Object getCacheKey(Method method, Class<?> clazz) {
		return new MethodClassKey(method, clazz);
	}

	/**
	 * 从缓存中获取数据权限注解 优先获取方法上的注解，再获取类上的注解
	 * @param method 当前方法
	 * @param targetClass 当前类
	 * @return 当前方法有效的数据权限注解
	 */
	public static DataPermission findDataPermission(Method method, Class<?> targetClass) {
		if (method.getDeclaringClass() == Object.class) {
			return null;
		}

		// 先查找缓存中是否存在
		Object methodKey = getCacheKey(method, targetClass);
		if (DATA_PERMISSION_CACHE.containsKey(methodKey)) {
			DataPermission dataPermission = DATA_PERMISSION_CACHE.get(methodKey);
			// 判断是否和缓存的空注解是同一个对象
			return EMPTY_DATA_PERMISSION == dataPermission ? null : dataPermission;
		}

		// 先查方法，如果方法上没有，则使用类上
		DataPermission dataPermission = computeDataPermissionAnnotation(method, targetClass);
		// 添加进缓存
		DATA_PERMISSION_CACHE.put(methodKey, dataPermission == null ? EMPTY_DATA_PERMISSION : dataPermission);
		return dataPermission;
	}

	/**
	 * 计算当前应用的 dataPermission 注解
	 * @see org.springframework.transaction.interceptor.AbstractFallbackTransactionAttributeSource#computeTransactionAttribute(java.lang.reflect.Method,
	 * java.lang.Class)
	 * @param method 当前方法
	 * @param targetClass 当前类
	 * @return DataPermission
	 */
	private static DataPermission computeDataPermissionAnnotation(Method method, Class<?> targetClass) {
		// 方法可能在接口上，但是我们需要找到对应的实现类的方法
		// 如果 target class 是 null, 则 method 不变
		Method specificMethod = AopUtils.getMostSpecificMethod(method, targetClass);

		// 首先，从目标类的方法上进行查找 dataPermission 注解
		DataPermission dataPermission = findDataPermissionAnnotation(specificMethod);
		if (dataPermission != null) {
			return dataPermission;
		}

		// 第二次，则从目标类上的查找 dataPermission 注解
		dataPermission = findDataPermissionAnnotation(specificMethod.getDeclaringClass());
		if (dataPermission != null && ClassUtils.isUserLevelMethod(method)) {
			return dataPermission;
		}

		if (specificMethod != method) {
			// 回退，查找原先传入的 method
			dataPermission = findDataPermissionAnnotation(method);
			if (dataPermission != null) {
				return dataPermission;
			}
			// 最后，尝试从原始的 method 的所有类上查找
			dataPermission = findDataPermissionAnnotation(method.getDeclaringClass());
			if (dataPermission != null && ClassUtils.isUserLevelMethod(method)) {
				return dataPermission;
			}
		}

		return null;
	}

	private static DataPermission findDataPermissionAnnotation(Method method) {
		return AnnotatedElementUtils.findMergedAnnotation(method, DataPermission.class);
	}

	private static DataPermission findDataPermissionAnnotation(Class<?> targetClass) {
		return AnnotatedElementUtils.findMergedAnnotation(targetClass, DataPermission.class);
	}

}
