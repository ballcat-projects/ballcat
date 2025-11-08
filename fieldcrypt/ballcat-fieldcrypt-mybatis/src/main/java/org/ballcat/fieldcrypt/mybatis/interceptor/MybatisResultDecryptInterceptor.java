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

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.sql.Statement;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Queue;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.ballcat.fieldcrypt.core.ClassMetaData;
import org.ballcat.fieldcrypt.core.FieldMetaData;
import org.ballcat.fieldcrypt.core.cache.ClassMetaResolver;
import org.ballcat.fieldcrypt.core.exception.FieldCryptCryptoException;
import org.ballcat.fieldcrypt.core.exception.FieldCryptErrorCode;
import org.ballcat.fieldcrypt.core.runtime.FieldCryptRuntimeConfig;
import org.ballcat.fieldcrypt.crypto.CryptoContext;
import org.ballcat.fieldcrypt.crypto.CryptoEngine;
import org.ballcat.fieldcrypt.mybatis.metadata.MybatisMethodMetadata;
import org.ballcat.fieldcrypt.mybatis.metadata.MybatisMethodMetadataCache;
import org.ballcat.fieldcrypt.mybatis.metadata.MybatisResultMetadata;
import org.ballcat.fieldcrypt.util.FieldCryptLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 结果集解密拦截器：在结果映射完成后，对返回集合/对象中的加密字段执行解密。 支持集合、数组递归处理，使用身份去重集合避免循环引用。
 *
 * @author Hccake
 * @since 2.0.0
 */
@Intercepts({ @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = { Statement.class }) })
public class MybatisResultDecryptInterceptor implements Interceptor {

	private static final Logger log = LoggerFactory.getLogger(MybatisResultDecryptInterceptor.class);

	private final CryptoEngine cryptoEngine;

	private final ClassMetaResolver classMetaResolver;

	private final MybatisMethodMetadataCache mybatisMethodMetadataCache;

	private final FieldCryptRuntimeConfig runtime;

	private final ThreadLocal<Set<Object>> processed = ThreadLocal
		.withInitial(() -> Collections.newSetFromMap(new IdentityHashMap<>()));

	public MybatisResultDecryptInterceptor(CryptoEngine cryptoEngine, ClassMetaResolver classMetaResolver,
			FieldCryptRuntimeConfig runtime, MybatisMethodMetadataCache mybatisMethodMetadataCache) {
		this.cryptoEngine = cryptoEngine;
		this.classMetaResolver = classMetaResolver;
		this.runtime = runtime;
		this.mybatisMethodMetadataCache = mybatisMethodMetadataCache;
	}

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		FieldCryptRuntimeConfig.Snapshot snap = this.runtime.get();
		if (snap == null || !snap.enabled || !snap.enableResult) {
			return invocation.proceed();
		}
		// 从目标对象中提取当前的 MappedStatement.id（用于定位方法注解）
		String msId = extractMappedStatementId(invocation.getTarget());
		MybatisMethodMetadata mm = msId == null ? null : this.mybatisMethodMetadataCache.getOrParse(msId);
		MybatisResultMetadata rmeta = (mm == null) ? MybatisResultMetadata.ABSENT : mm.getResult();

		Object result = invocation.proceed();
		try {
			if (isTopLevelStringLike(result, rmeta)) {
				// 顶层字符串/字符串集合：仅按方法注解进行解密，完成后直接返回
				return decryptTopLevelStrings(result, rmeta, snap);
			}
			// 否则按实体字段递归解密
			decryptEntityGraph(result, snap);
			return result;
		}
		finally {
			this.processed.remove();
		}
	}

	private String extractMappedStatementId(Object target) {
		try {
			Object real = realTarget(target);
			Field f = real.getClass().getDeclaredField("mappedStatement");
			f.setAccessible(true);
			MappedStatement ms = (MappedStatement) f.get(real);
			return ms == null ? null : ms.getId();
		}
		catch (Throwable e) {
			if (log.isDebugEnabled()) {
				log.debug("FieldCrypt | module=mybatis | code={} where={} targetClass={}",
						FieldCryptErrorCode.REFLECT_ACCESS_ERROR, "extractMappedStatementId",
						target == null ? "null" : target.getClass().getName(), e);
			}
			return null;
		}
	}

	/**
	 * 获得真正的处理对象,可能多层代理.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T realTarget(Object target) {
		if (Proxy.isProxyClass(target.getClass())) {
			MetaObject metaObject = SystemMetaObject.forObject(target);
			return realTarget(metaObject.getValue("h.target"));
		}
		return (T) target;
	}

	// 规则判定：是否属于“顶层字符串返回形态”，且方法标注启用
	private boolean isTopLevelStringLike(Object result, MybatisResultMetadata rmeta) {
		if (result == null || rmeta == null || !rmeta.isAnnotated()) {
			return false;
		}
		if (result instanceof String) {
			return true;
		}
		Class<?> cls = result.getClass();
		if (cls.isArray() && cls.getComponentType() == String.class) {
			return true;
		}
		if (result instanceof Collection) {
			Collection<?> c = (Collection<?>) result;
			if (c.isEmpty()) {
				// 空集合：按顶层字符串集合对待（不会有副作用）
				return true;
			}
			for (Object item : c) {
				if (item != null) {
					return item instanceof String;
				}
			}
			// 全部为 null 的集合，也视为字符串集合（无元素可处理）
			return true;
		}
		return false;
	}

	// 顶层字符串/集合解密：仅依赖 @DecryptResult 的 algo/params
	private Object decryptTopLevelStrings(Object result, MybatisResultMetadata rmeta,
			FieldCryptRuntimeConfig.Snapshot snap) {
		if (result == null) {
			return null;
		}
		if (result instanceof String) {
			return decryptTopString((String) result, rmeta, snap);
		}
		Class<?> cls = result.getClass();
		if (cls.isArray() && cls.getComponentType() == String.class) {
			int len = java.lang.reflect.Array.getLength(result);
			for (int i = 0; i < len; i++) {
				Object v = java.lang.reflect.Array.get(result, i);
				if (v instanceof String) {
					java.lang.reflect.Array.set(result, i, decryptTopString((String) v, rmeta, snap));
				}
			}
			return result;
		}
		if (result instanceof List) {
			@SuppressWarnings("unchecked")
			List<Object> list = (List<Object>) result;
			ListIterator<Object> it = list.listIterator();
			while (it.hasNext()) {
				Object item = it.next();
				if (item instanceof String) {
					it.set(decryptTopString((String) item, rmeta, snap));
				}
			}
			return result;
		}
		if (result instanceof Collection) {
			// 其它集合类型（如 Set/Queue/Deque）：构造一个“近似同类型”的新集合，填充解密后的字符串
			Collection<?> src = (Collection<?>) result;
			Collection<Object> dst = newEmptyLike(src, src.size());
			for (Object item : src) {
				if (item instanceof String) {
					dst.add(decryptTopString((String) item, rmeta, snap));
				}
				else {
					// 理论上不会出现（已由 isTopLevelStringLike 保障），但仍做兼容
					dst.add(item);
				}
			}
			return dst;
		}
		return result;
	}

	private String decryptTopString(String s, MybatisResultMetadata rmeta, FieldCryptRuntimeConfig.Snapshot snap) {
		if (s == null) {
			return null;
		}
		String useAlgo = (rmeta.getAlgo() == null || rmeta.getAlgo().isEmpty()) ? this.cryptoEngine.getDefaultAlgo()
				: rmeta.getAlgo();
		if (useAlgo == null || useAlgo.isEmpty()) {
			return s;
		}
		try {
			return this.cryptoEngine.decrypt(s,
					new CryptoContext(useAlgo, rmeta.getParams() == null ? "" : rmeta.getParams()));
		}
		catch (RuntimeException ex) {
			if (snap.failFast) {
				throw ex;
			}
			if (log.isWarnEnabled()) {
				String maskedParams = FieldCryptLog.mask(rmeta.getParams(), 2, 2);
				log.warn("顶层字符串解密失败，按原值返回 algo={} params={}", useAlgo, maskedParams, ex);
			}
			return s;
		}
	}

	// 实体图递归解密：仅处理被标注的实体字段
	private void decryptEntityGraph(Object node, FieldCryptRuntimeConfig.Snapshot snap) {
		if (node == null) {
			return;
		}
		if (node instanceof Collection) {
			for (Object item : (Collection<?>) node) {
				decryptEntityGraph(item, snap);
			}
			return;
		}
		Class<?> cls = node.getClass();
		if (cls.isArray()) {
			int len = java.lang.reflect.Array.getLength(node);
			for (int i = 0; i < len; i++) {
				Object v = java.lang.reflect.Array.get(node, i);
				decryptEntityGraph(v, snap);
			}
			return;
		}
		if (node instanceof String) {
			// 非顶层字符串不处理
			return;
		}
		Set<Object> seen = this.processed.get();
		if (seen.contains(node)) {
			return;
		}
		seen.add(node);
		ClassMetaData meta = this.classMetaResolver.resolve(cls);
		if (meta != null && meta.shouldProcess()) {
			for (FieldMetaData fm : meta.getEncryptedFields()) {
				Field f = fm.getField();
				try {
					Object val = f.get(node);
					if (val instanceof String) {
						String plain = decryptFieldString((String) val, fm, snap);
						f.set(node, plain);
					}
				}
				catch (IllegalAccessException e) {
					throw FieldCryptCryptoException
						.of(FieldCryptErrorCode.REFLECT_ACCESS_ERROR, "decryptEntityGraph", e)
						.withDetail("field", String.valueOf(f));
				}
			}
		}
	}

	private String decryptFieldString(String s, FieldMetaData fm, FieldCryptRuntimeConfig.Snapshot snap) {
		if (s == null) {
			return null;
		}
		try {
			return this.cryptoEngine.decrypt(s, new CryptoContext(fm.getAlgo(), fm.getParams()));
		}
		catch (RuntimeException ex) {
			if (snap.failFast) {
				throw ex;
			}
			if (log.isWarnEnabled()) {
				log.warn("实体字段解密失败，按原值返回 field={} algo={}", fm.getField().getName(), fm.getAlgo(), ex);
			}
			return s;
		}
	}

	/**
	 * 基于源集合创建一个“近似同类型”的空集合，用于承载解密后的元素。 仅面向顶层字符串集合的场景，优先维持语义与迭代顺序： - SortedSet：使用相同
	 * Comparator 的 TreeSet - Set：使用 LinkedHashSet 以保序 - Deque/Queue：使用 ArrayDeque - 其它：使用
	 * ArrayList（兜底，不会用于 List 分支）
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Collection<Object> newEmptyLike(Collection<?> src, int sizeHint) {
		if (src instanceof SortedSet) {
			Comparator cmp = ((SortedSet) src).comparator();
			return (cmp == null) ? new TreeSet<>() : new TreeSet<>(cmp);
		}
		if (src instanceof Set) {
			return new LinkedHashSet<>(Math.max(16, sizeHint));
		}
		if (src instanceof Queue) {
			return new ArrayDeque<>(Math.max(16, sizeHint));
		}
		return new ArrayList<>(Math.max(16, sizeHint));
	}

}
