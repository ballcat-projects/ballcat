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

package org.ballcat.fieldcrypt.mybatis.testutil;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

/**
 * 测试用“分页样式”拦截器：在 4 参 query 中改为直调 6 参 query。 模拟 PageHelper 的内部改写行为。
 *
 * @author Hccake
 * @since 2.0.0
 */
@Intercepts({ @Signature(type = Executor.class, method = "query",
		args = { MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class }) })
public class PagingLikeInterceptor implements Interceptor {

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		Object[] args = invocation.getArgs();
		if ("query".equals(invocation.getMethod().getName()) && args.length == 4) {
			MappedStatement ms = (MappedStatement) args[0];
			Object parameter = args[1];
			RowBounds rowBounds = (RowBounds) args[2];
			ResultHandler<?> rh = (ResultHandler<?>) args[3];
			Executor executor = (Executor) invocation.getTarget();
			BoundSql boundSql = ms.getBoundSql(parameter);
			CacheKey cacheKey = executor.createCacheKey(ms, parameter, rowBounds, boundSql);
			// 关键：对“目标对象”直调 6 参，绕过后续插件链
			return executor.query(ms, parameter, rowBounds, rh, cacheKey, boundSql);
		}
		return invocation.proceed();
	}

}
