package com.hccake.ballcat.common.datascope.interceptor;

import com.hccake.ballcat.common.datascope.annotation.DataPermission;
import com.hccake.ballcat.common.datascope.holder.DataPermissionAnnotationHolder;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;

/**
 * DataPermission注解的拦截器，在执行方法前将当前方法的对应注解压栈，执行后弹出注解
 *
 * @author hccake
 */
public class DataPermissionAnnotationInterceptor implements MethodInterceptor {

	@Override
	public Object invoke(MethodInvocation methodInvocation) throws Throwable {

		Method method = methodInvocation.getMethod();
		DataPermission dataPermission = DataPermissionFinder.findDataPermission(method);
		DataPermissionAnnotationHolder.push(dataPermission);
		try {
			return methodInvocation.proceed();
		}
		finally {
			DataPermissionAnnotationHolder.poll();
		}
	}

}
