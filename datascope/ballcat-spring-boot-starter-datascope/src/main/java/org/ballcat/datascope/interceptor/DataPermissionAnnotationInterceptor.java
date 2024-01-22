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

package org.ballcat.datascope.interceptor;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.ballcat.datascope.annotation.DataPermission;
import org.ballcat.datascope.handler.DataPermissionRule;
import org.ballcat.datascope.holder.DataPermissionRuleHolder;

/**
 * DataPermission注解的拦截器，在执行方法前将当前方法的对应注解压栈，执行后弹出注解
 *
 * @author hccake
 */
public class DataPermissionAnnotationInterceptor implements MethodInterceptor {

	@Override
	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
		// 当前方法
		Method method = methodInvocation.getMethod();
		// 获取执行类
		Object invocationThis = methodInvocation.getThis();
		Class<?> clazz = invocationThis != null ? invocationThis.getClass() : method.getDeclaringClass();
		// 寻找对应的 DataPermission 注解属性
		DataPermission dataPermission = DataPermissionFinder.findDataPermission(method, clazz);
		// 理论上这里是不会为空的
		if (dataPermission == null) {
			return methodInvocation.proceed();
		}

		DataPermissionRuleHolder.push(new DataPermissionRule(dataPermission));
		try {
			return methodInvocation.proceed();
		}
		finally {
			DataPermissionRuleHolder.poll();
		}
	}

}
