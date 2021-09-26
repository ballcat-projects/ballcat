package com.hccake.ballcat.common.datascope.interceptor;

import com.hccake.ballcat.common.datascope.DataScope;
import com.hccake.ballcat.common.datascope.handler.DataPermissionHandler;
import com.hccake.ballcat.common.datascope.holder.DataScopeMatchNumHolder;
import com.hccake.ballcat.common.datascope.holder.MappedStatementIdsWithoutDataScope;
import com.hccake.ballcat.common.datascope.processor.DataScopeSqlProcessor;
import com.hccake.ballcat.common.datascope.util.PluginUtils;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;

import java.sql.Connection;
import java.util.List;

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
		String mappedStatementId = ms.getId();

		// 根据用户权限判断是否需要拦截，例如管理员可以查看所有，则直接放行
		if (dataPermissionHandler.ignorePermissionControl(mappedStatementId)) {
			return invocation.proceed();
		}

		List<DataScope> dataScopes = dataPermissionHandler.filterDataScopes(mappedStatementId);
		if (dataScopes == null || dataScopes.isEmpty()) {
			return invocation.proceed();
		}

		try {
			// 创建 matchNumTreadLocal
			DataScopeMatchNumHolder.create();
			// 根据 DataScopes 进行数据权限的 sql 处理
			if (sct == SqlCommandType.SELECT) {
				mpBs.sql(dataScopeSqlProcessor.parserSingle(mpBs.sql(), dataScopes));
			}
			else if (sct == SqlCommandType.INSERT || sct == SqlCommandType.UPDATE || sct == SqlCommandType.DELETE) {
				mpBs.sql(dataScopeSqlProcessor.parserMulti(mpBs.sql(), dataScopes));
			}
			// 如果解析后发现当前 mappedStatementId 对应的 sql，没有任何数据权限匹配，则记录下来，后续可以直接跳过不解析
			if (DataScopeMatchNumHolder.getMatchNum() == 0) {
				MappedStatementIdsWithoutDataScope.addStatementId(mappedStatementId);
			}
		}
		finally {
			DataScopeMatchNumHolder.remove();
		}

		// 执行 sql
		return invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {
		if (target instanceof StatementHandler) {
			return Plugin.wrap(target, this);
		}
		return target;
	}

}
