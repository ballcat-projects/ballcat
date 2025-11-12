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

package org.ballcat.fieldcrypt.mybatis.interceptor;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
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
import org.ballcat.fieldcrypt.core.cache.ClassMetaResolver;
import org.ballcat.fieldcrypt.core.runtime.FieldCryptRuntimeConfig;
import org.ballcat.fieldcrypt.crypto.CryptoEngine;
import org.ballcat.fieldcrypt.mybatis.interceptor.internal.EncryptionContext;
import org.ballcat.fieldcrypt.mybatis.interceptor.internal.MutationRecorder;
import org.ballcat.fieldcrypt.mybatis.metadata.MybatisMethodMetadata;
import org.ballcat.fieldcrypt.mybatis.metadata.MybatisMethodMetadataCache;

/**
 * 参数加密拦截器。
 *
 * <p>
 * 拦截 {@link Executor} 的 update 与 query 方法，在入参进入 MyBatis 执行链之前，对被标记需要加密的字段/元素进行加密， 以保证：
 * <ul>
 * <li>写操作：入库为密文；</li>
 * <li>读操作：查询条件按密文对齐存储；</li>
 * </ul>
 *
 * <p>
 * 特性与约束：
 * <ul>
 * <li>支持对象、集合、数组的递归扫描；</li>
 * <li>Map 支持按 key 精细化控制以及通配符（"*" 表示全部）；</li>
 * <li>可选执行后恢复明文（restorePlaintext），用于保障调用侧感知到的对象内容不被持久改变；</li>
 * <li>为避免重复加密，内部使用 processed 集合与 4 参/6 参 query 链路深度标记；</li>
 * <li>本类仅进行“字符串值”的加密替换，实体对象仅做字段级别的替换，不会整体替换对象引用。</li>
 * </ul>
 **
 * @author Hccake
 * @since 2.0.0
 */
@Slf4j
@Intercepts({ @Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }),
		@Signature(type = Executor.class, method = "query",
				args = { MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class }),
		// 兼容部分插件（如分页插件）在 4 参链路中改为直调 6 参 query 的场景
		@Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
				RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class }) })
public class MybatisParameterEncryptInterceptor implements Interceptor {

	private final CryptoEngine cryptoEngine;

	private final ClassMetaResolver classMetaResolver;

	private final FieldCryptRuntimeConfig runtime; // 运行时配置入口

	private final MybatisMethodMetadataCache mybatisMethodMetadataCache;

	// 单线程拦截状态封装（避免多个 ThreadLocal 分散）
	private final ThreadLocal<InvocationState> stateHolder = ThreadLocal.withInitial(InvocationState::new);

	public MybatisParameterEncryptInterceptor(CryptoEngine cryptoEngine, ClassMetaResolver classMetaResolver,
			FieldCryptRuntimeConfig runtime, MybatisMethodMetadataCache mybatisMethodMetadataCache) {
		this.cryptoEngine = cryptoEngine;
		this.classMetaResolver = classMetaResolver;
		this.runtime = runtime;
		this.mybatisMethodMetadataCache = mybatisMethodMetadataCache;
	}

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		FieldCryptRuntimeConfig.Snapshot snap = this.runtime.get();
		if (snap == null || !snap.enabled || !snap.enableParameter) {
			return invocation.proceed();
		}
		Object[] args = invocation.getArgs();
		boolean isQuery = "query".equals(invocation.getMethod().getName());
		InvocationState state = this.stateHolder.get();

		// 6 参 query 且已在 4 参链路内部 -> 跳过（外层已处理过加密），保持原有防重复语义
		if (isQuery && args.length == 6 && state.getQueryDepth() > 0) {
			return invocation.proceed();
		}

		boolean fourArgQuery = isQuery && args.length == 4;
		if (fourArgQuery) {
			state.increaseQueryDepth();
		}

		MappedStatement ms = (MappedStatement) args[0];
		Object parameterObject = args[1];
		MybatisMethodMetadata methodMetadata = this.mybatisMethodMetadataCache.getOrParse(ms.getId());
		MutationRecorder recorder = snap.restorePlaintext ? new MutationRecorder() : null;
		EncryptionContext ctx = new EncryptionContext(recorder, snap.failFast, state.getProcessed());

		ParameterEncryptor parameterEncryptor = new ParameterEncryptor(this.cryptoEngine, this.classMetaResolver);
		try {
			Object newParam = parameterEncryptor.handleRootParameter(parameterObject, methodMetadata, ctx);
			if (newParam != null && newParam != parameterObject) {
				args[1] = newParam;
			}
			return invocation.proceed();
		}
		finally {
			try {
				if (snap.restorePlaintext && !recorder.isEmpty()) {
					recorder.restoreAll();
				}
			}
			finally {
				if (fourArgQuery) {
					state.decreaseQueryDepth();
				}
				// 清理 processed：仅当脱离最外层或当前不是嵌套 4 参链路
				if (!fourArgQuery || state.getQueryDepth() == 0) {
					state.getProcessed().clear();
				}
			}
		}
	}

	/**
	 * 每个线程/调用在拦截器中的状态：包含递归去重集合与 query 深度。
	 */
	@Getter
	static final class InvocationState {

		final Set<Object> processed = Collections.newSetFromMap(new IdentityHashMap<>());

		int queryDepth = 0; // 4 参 query 递归深度

		/**
		 * 增加 query 递归深度。
		 */
		public void increaseQueryDepth() {
			this.queryDepth++;
		}

		/**
		 * 减少 query 递归深度。
		 */
		public void decreaseQueryDepth() {
			this.queryDepth--;
		}

	}

}
