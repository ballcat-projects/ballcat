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

package org.ballcat.log.operation.annotation;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.aop.support.AopUtils;
import org.springframework.core.MethodClassKey;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.ClassUtils;

/**
 * {@link OperationLog} 注解的查找者。用于查询当前方法对应的 OperationLog 注解环境，当方法上没有找到时，会去类上寻找。
 *
 * @author hccake
 * @since 2.0.0
 */
public final class OperationLogFinder {

	private OperationLogFinder() {

	}

	private static final Map<Object, OperationLog> CACHE = new ConcurrentHashMap<>(1024);

	/**
	 * 提供一个默认的空值注解，用于缓存空值占位使用
	 */
	private static final OperationLog EMPTY_OPERATION_LOG;

	static {
		try {
			EMPTY_OPERATION_LOG = OperationLogFinder.class.getDeclaredMethod("empty").getAnnotation(OperationLog.class);
		}
		catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}

	@OperationLog(bizType = "empty")
	private void empty() {
		// do nothing
	}

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
	public static OperationLog findOperationLog(Method method, Class<?> targetClass) {
		if (method.getDeclaringClass() == Object.class) {
			return null;
		}

		// 先查找缓存中是否存在
		Object methodKey = getCacheKey(method, targetClass);
		if (CACHE.containsKey(methodKey)) {
			OperationLog operationLog = CACHE.get(methodKey);
			// 判断是否和缓存的空注解是同一个对象
			return EMPTY_OPERATION_LOG == operationLog ? null : operationLog;
		}

		// 先查方法，如果方法上没有，则使用类上
		OperationLog operationLog = computeOperationLogAnnotation(method, targetClass);
		// 添加进缓存
		CACHE.put(methodKey, operationLog == null ? EMPTY_OPERATION_LOG : operationLog);
		return operationLog;
	}

	/**
	 * 计算当前应用的 OperationLog 注解
	 * @see org.springframework.transaction.interceptor.AbstractFallbackTransactionAttributeSource#computeTransactionAttribute(Method,
	 * Class)
	 * @param method 当前方法
	 * @param targetClass 当前类
	 * @return OperationLog
	 */
	private static OperationLog computeOperationLogAnnotation(Method method, Class<?> targetClass) {
		// 方法可能在接口上，但是我们需要找到对应的实现类的方法
		// 如果 target class 是 null, 则 method 不变
		Method specificMethod = AopUtils.getMostSpecificMethod(method, targetClass);

		// 首先，从目标类的方法上进行查找 OperationLog 注解
		OperationLog operationLog = findOperationLogAnnotation(specificMethod);
		if (operationLog != null) {
			return operationLog;
		}

		// 第二次，则从目标类上的查找 OperationLog 注解
		operationLog = findOperationLogAnnotation(specificMethod.getDeclaringClass());
		if (operationLog != null && ClassUtils.isUserLevelMethod(method)) {
			return operationLog;
		}

		if (specificMethod != method) {
			// 回退，查找原先传入的 method
			operationLog = findOperationLogAnnotation(method);
			if (operationLog != null) {
				return operationLog;
			}
			// 最后，尝试从原始的 method 的所有类上查找
			operationLog = findOperationLogAnnotation(method.getDeclaringClass());
			if (operationLog != null && ClassUtils.isUserLevelMethod(method)) {
				return operationLog;
			}
		}

		return null;
	}

	private static OperationLog findOperationLogAnnotation(Method method) {
		return AnnotatedElementUtils.findMergedAnnotation(method, OperationLog.class);
	}

	private static OperationLog findOperationLogAnnotation(Class<?> targetClass) {
		return AnnotatedElementUtils.findMergedAnnotation(targetClass, OperationLog.class);
	}

}
