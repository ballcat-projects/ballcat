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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * 切面工具类
 *
 * @author lingting 2022/10/28 15:03
 */
public final class AspectUtils {

	private AspectUtils() {
	}

	/**
	 * 获取切入的方法
	 * @param point 切面
	 * @return java.lang.reflect.Method
	 */
	public static Method getMethod(ProceedingJoinPoint point) {
		Signature signature = point.getSignature();
		if (signature instanceof MethodSignature) {
			MethodSignature ms = (MethodSignature) signature;
			return ms.getMethod();
		}
		return null;
	}

	/**
	 * 获取切入点方法上的注解, 找不到则往类上找
	 * @param point 切面
	 * @param cls 注解类型
	 * @return T 注解类型
	 */
	public static <T extends Annotation> T getAnnotation(ProceedingJoinPoint point, Class<T> cls) {
		Method method = getMethod(point);
		T t = null;
		if (method != null) {
			t = method.getAnnotation(cls);
		}

		if (t == null) {
			t = point.getTarget().getClass().getAnnotation(cls);
		}
		return t;
	}

}
