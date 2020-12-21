package com.hccake.ballcat.common.datascope.interceptor;

import com.hccake.ballcat.common.datascope.DataScope;
import com.hccake.ballcat.common.datascope.annotation.DataPermission;
import com.hccake.ballcat.common.datascope.handler.DataPermissionHandler;
import com.hccake.ballcat.common.datascope.processor.DataScopeSqlProcessor;
import com.hccake.ballcat.common.datascope.util.PluginUtils;
import com.sun.tools.javac.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.mapstruct.ap.shaded.freemarker.template.utility.StringUtil;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.List;
import java.util.Properties;

/**
 * 数据权限拦截器
 *
 * @author Hccake 2020/9/28
 * @version 1.0
 */
@RequiredArgsConstructor
@Intercepts({
		@Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class, Integer.class }) })
public class DataPermissionInterceptor implements Interceptor {

	private final DataScopeSqlProcessor dataScopeSqlProcessor;

	private final DataPermissionHandler dataPermissionHandler;

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		// 第一版，测试用
		Object target = invocation.getTarget();
		StatementHandler sh = (StatementHandler) target;
		PluginUtils.MPStatementHandler mpSh = PluginUtils.mpStatementHandler(sh);
		MappedStatement ms = mpSh.mappedStatement();
		SqlCommandType sct = ms.getSqlCommandType();
		PluginUtils.MPBoundSql mpBs = mpSh.mPBoundSql();

		// TODO 根据注解进行一些此次 sql 执行中需要忽略的点
		DataPermission annotation = hasNoNeedOfficeAnnotation(ms.getId());

		if (annotation != null && !annotation.enabled()) {
			return invocation.proceed();
		}

		// 根据用户权限判断是否需要拦截，例如管理员可以查看所有，则直接放行
		if (dataPermissionHandler.ignorePermissionControl()) {
			return invocation.proceed();
		}
		List<DataScope> dataScopes = dataPermissionHandler.dataScopes();
		if (dataScopes == null || dataScopes.size() == 0) {
			return invocation.proceed();
		}

		// 根据 DataScopes 进行数据权限的 sql 处理
		if (sct == SqlCommandType.SELECT) {
			mpBs.sql(dataScopeSqlProcessor.parserSingle(mpBs.sql(), dataScopes));
		}
		else if (sct == SqlCommandType.INSERT || sct == SqlCommandType.UPDATE || sct == SqlCommandType.DELETE) {
			mpBs.sql(dataScopeSqlProcessor.parserMulti(mpBs.sql(), dataScopes));
		}

		return invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {
		if (target instanceof StatementHandler) {
			return Plugin.wrap(target, this);
		}
		return target;
	}

	@Override
	public void setProperties(Properties properties) {

	}

	private DataPermission hasNoNeedOfficeAnnotation(String sqlId) {
		if (sqlId == null || "".equals(sqlId)) {
			return null;
		}
		// 1.得到类路径和方法路径
		int lastIndexOfDot = sqlId.lastIndexOf(".");
		if (lastIndexOfDot < 0) {
			return null;
		}
		String className = sqlId.substring(0, lastIndexOfDot);
		String methodName = sqlId.substring(lastIndexOfDot + 1);
		if ("".equals(className) || "".equals(methodName)) {
			return null;
		}

		// 2.字节码
		Class<?> clazz = null;
		try {
			clazz = Class.forName(className);
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		if (clazz == null) {
			return null;
		}
		DataPermission annotation = null;

		// 3.得到方法上的注解
		Method[] methods = clazz.getMethods();
		for (Method method : methods) {
			String name = method.getName();
			if (methodName.equals(name)) {
				annotation = method.getAnnotation(DataPermission.class);
				break;
			}
		}
		if (annotation == null) {
			annotation = clazz.getAnnotation(DataPermission.class);
		}
		return annotation;
	}

}
