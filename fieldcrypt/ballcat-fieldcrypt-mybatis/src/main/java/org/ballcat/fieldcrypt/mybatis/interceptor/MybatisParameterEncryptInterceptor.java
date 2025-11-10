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
import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.binding.MapperMethod;
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
import org.ballcat.fieldcrypt.core.ClassMetaData;
import org.ballcat.fieldcrypt.core.FieldMetaData;
import org.ballcat.fieldcrypt.core.cache.ClassMetaResolver;
import org.ballcat.fieldcrypt.core.exception.FieldCryptCryptoException;
import org.ballcat.fieldcrypt.core.runtime.FieldCryptRuntimeConfig;
import org.ballcat.fieldcrypt.crypto.CryptoContext;
import org.ballcat.fieldcrypt.crypto.CryptoEngine;
import org.ballcat.fieldcrypt.mybatis.internal.MutationRecorder;
import org.ballcat.fieldcrypt.mybatis.metadata.MybatisMethodMetadata;
import org.ballcat.fieldcrypt.mybatis.metadata.MybatisMethodMetadataCache;
import org.ballcat.fieldcrypt.mybatis.metadata.MybatisParameterMetadata;

/**
 * 参数加密拦截器：拦截 Executor 的 update 与 query 方法，对入参对象中标记字段进行加密。 当前支持：对象、集合、数组的递归扫描；Map 精细 key
 * 处理后续增强。 可选在执行后还原明文（restorePlaintext）。
 *
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

	private static final String PARAM_PREFIX = "param";

	private static final String WILDCARD_ALL = "*";

	private final ThreadLocal<Set<Object>> processed = ThreadLocal
		.withInitial(() -> Collections.newSetFromMap(new IdentityHashMap<>()));

	private final FieldCryptRuntimeConfig runtime; // 运行时快照

	private final MybatisMethodMetadataCache mybatisMethodMetadataCache;

	// 防重复处理：标记当前线程是否处于 4 参 query 的调用栈内，避免 6 参再次加密
	private final ThreadLocal<Integer> queryEntryDepth = ThreadLocal.withInitial(() -> 0);

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

		// 若是 6 参 query 且当前处于 4 参调用栈内，则跳过以防重复加密（外层已处理）
		if (isQuery && args.length == 6 && this.queryEntryDepth.get() > 0) {
			return invocation.proceed();
		}

		// 进入 4 参 query：增加深度计数，确保内部 6 参不会重复处理
		boolean enteredFourArgQuery = isQuery && args.length == 4;
		if (enteredFourArgQuery) {
			this.queryEntryDepth.set(this.queryEntryDepth.get() + 1);
		}

		MappedStatement ms = (MappedStatement) args[0];
		Object parameterObject = args[1];
		MybatisMethodMetadata mybatisMethodMetadata = this.mybatisMethodMetadataCache.getOrParse(ms.getId());
		MutationRecorder recorder = snap.restorePlaintext ? new MutationRecorder() : null;
		try {
			// 展开参数并逐一处理，可能返回替换后的根参数（例如单参数 String 加密后的密文）
			Object newParam = processRootParameter(parameterObject, mybatisMethodMetadata, recorder, snap);
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
				// 仅在最外层（或独立的 6 参兜底）调用结束时清理状态
				if (enteredFourArgQuery) {
					this.queryEntryDepth.set(this.queryEntryDepth.get() - 1);
				}
				// 非嵌套路径都需要清理 processed 集合
				if (!enteredFourArgQuery || this.queryEntryDepth.get() == 0) {
					this.processed.remove();
				}
			}
		}
	}

	private Object processRootParameter(Object root, MybatisMethodMetadata mybatisMethodMetadata,
			MutationRecorder recorder, FieldCryptRuntimeConfig.Snapshot snap) {
		if (root == null) {
			return null;
		}
		ProcessingCtx ctx = new ProcessingCtx(this.cryptoEngine, this.classMetaResolver, recorder,
				snap.restorePlaintext, snap.failFast, this.processed.get());
		if (root instanceof Map && isMyBatisParamMap(root)) {
			Map<?, ?> paramMap = (Map<?, ?>) root;
			MybatisParameterMetadata[] metas = mybatisMethodMetadata.getParameters();
			Map<Object, Group> groups = buildParamGroups(paramMap, mybatisMethodMetadata, metas);
			// 逐组处理
			for (Group g : groups.values()) {
				processGroup(recorder, snap, g, ctx, metas, (Map<String, Object>) paramMap);
			}
			return root;
		}
		else {
			MybatisParameterMetadata pmeta = mybatisMethodMetadata.size() == 1
					? mybatisMethodMetadata.getParameters()[0] : null;
			Object replaced = processValue(root, pmeta, ctx, NullBackRef.INSTANCE);
			return replaced == null ? root : replaced;
		}
	}

	private void processGroup(MutationRecorder recorder, FieldCryptRuntimeConfig.Snapshot snap, Group g,
			ProcessingCtx ctx, MybatisParameterMetadata[] metas, Map<String, Object> paramMap) {
		if (g.value == null) {
			return;
		}
		if (ctx.isProcessed(g.value)) {
			return;
		}
		// 选 meta：优先有注解; 分两步保证 chosen 最终有效 final
		final MybatisParameterMetadata chosen = selectParameterMeta(g.indices, metas);
		if (chosen != null && chosen.isAnnotated()) {
			// 参数级逻辑
			if (g.value instanceof String) {
				String original = (String) g.value;
				if (!original.isEmpty()) {
					String cipher = encryptStringOrFallback(original, chosen.getContext(), "param-map-group:" + g.keys,
							ctx);
					// 回写所有 key
					@SuppressWarnings("unchecked")
					Map<String, Object> pm = paramMap;
					for (String k : g.keys) {
						pm.put(k, cipher);
						if (ctx.shouldRecord()) {
							ctx.recordDirect(pm, k, original);
						}
					}
					ctx.markProcessed(g.value); // 标记
				}
			}
			else if (g.value instanceof Map) {
				Set<String> keySet = toKeySet(chosen.getMapKeys());
				if (!keySet.isEmpty()) {
					encryptMap((Map<?, ?>) g.value, chosen.getContext(), keySet, ctx);
				}
				ctx.markProcessed(g.value);
			}
			else if (g.value instanceof Collection) {
				ctx.markProcessed(g.value);
				if (g.value instanceof List) {
					List<?> list = (List<?>) g.value;
					@SuppressWarnings("unchecked")
					ListIterator<Object> it = (ListIterator<Object>) list.listIterator();
					while (it.hasNext()) {
						Object elem = it.next();
						BackRef backRef = (snap.restorePlaintext && recorder != null)
								? new ListBackRef(list, it.previousIndex()) : NullBackRef.INSTANCE;
						Object replaced = encryptParameterElement(elem, chosen, ctx, backRef);
						if (replaced != elem && replaced instanceof String) {
							it.set(replaced);
						}
					}
				}
				else {
					for (Object elem : (Collection<?>) g.value) {
						encryptParameterElement(elem, chosen, ctx, NullBackRef.INSTANCE);
					}
				}
			}
			else if (g.value.getClass().isArray()) {
				ctx.markProcessed(g.value);
				int len = java.lang.reflect.Array.getLength(g.value);
				for (int i = 0; i < len; i++) {
					Object elem = java.lang.reflect.Array.get(g.value, i);
					BackRef backRef = (snap.restorePlaintext && recorder != null) ? new ArrayBackRef(g.value, i)
							: NullBackRef.INSTANCE;
					Object replaced = encryptParameterElement(elem, chosen, ctx, backRef);
					if (replaced != elem) {
						java.lang.reflect.Array.set(g.value, i, replaced);
					}
				}
			}
			else {
				encryptEntityFields(g.value, ctx);
			}
		}
		else {
			// 按实体/集合常规逻辑
			processValue(g.value, null, ctx, NullBackRef.INSTANCE);
		}
	}

	// ---- unified value processing entry ----
	private Object processValue(Object value, MybatisParameterMetadata pmeta, ProcessingCtx ctx, BackRef backRef) {
		if (value == null) {
			return null;
		}
		// 参数级注解优先
		if (pmeta != null && pmeta.isAnnotated()) {
			if (value instanceof String) {
				if (log.isDebugEnabled()) {
					log.debug("Encrypt parameter-level String algo={}", pmeta.getAlgo());
				}
				String cipher = encryptStringOrFallback((String) value, pmeta.getContext(), "param-string", ctx);
				if (cipher != null && cipher != value) {
					backRef.record(ctx, (String) value);
					return cipher;
				}
				return value;
			}
			if (value instanceof Map) {
				Set<String> keySet = toKeySet(pmeta.getMapKeys());
				if (!keySet.isEmpty()) {
					encryptMap((Map<?, ?>) value, pmeta.getContext(), keySet, ctx);
				}
				return value;
			}
			if (value instanceof Collection) {
				processCollection((Collection<?>) value, pmeta, ctx);
				return value;
			}
			if (value.getClass().isArray()) {
				processArray(value, pmeta, ctx);
				return value;
			}
			// 实体字段
			encryptEntityFields(value, ctx);
			return value;
		}
		// 未注解：字段级
		if (value instanceof Collection) {
			processCollection((Collection<?>) value, null, ctx);
			return value;
		}
		if (value.getClass().isArray()) {
			processArray(value, null, ctx);
			return value;
		}
		if (value instanceof Map) {
			// 未加注解 Map 暂不处理（保持语义）
			return value;
		}
		encryptEntityFields(value, ctx);
		return value;
	}

	private void processCollection(Collection<?> c, MybatisParameterMetadata pmeta, ProcessingCtx ctx) {
		if (c == null) {
			return;
		}
		if (ctx.isProcessed(c)) {
			return;
		}
		ctx.markProcessed(c);
		if (c instanceof List) {
			List<?> list = (List<?>) c;
			@SuppressWarnings("unchecked")
			ListIterator<Object> it = (ListIterator<Object>) list.listIterator();
			while (it.hasNext()) {
				Object elem = it.next();
				BackRef br = ctx.shouldRecord() ? new ListBackRef(list, it.previousIndex()) : NullBackRef.INSTANCE;
				Object replaced = encryptParameterElement(elem, (pmeta), ctx, br);
				if (replaced != elem && replaced instanceof String) {
					it.set(replaced);
				}
			}
			return;
		}
		for (Object elem : c) {
			encryptParameterElement(elem, (pmeta), ctx, NullBackRef.INSTANCE);
		}
	}

	private void processArray(Object array, MybatisParameterMetadata pmeta, ProcessingCtx ctx) {
		if (array == null || !array.getClass().isArray()) {
			return;
		}
		if (ctx.isProcessed(array)) {
			return;
		}
		ctx.markProcessed(array);
		int len = java.lang.reflect.Array.getLength(array);
		for (int i = 0; i < len; i++) {
			Object elem = java.lang.reflect.Array.get(array, i);
			BackRef br = ctx.shouldRecord() ? new ArrayBackRef(array, i) : NullBackRef.INSTANCE;
			Object replaced = encryptParameterElement(elem, pmeta, ctx, br);
			if (replaced != elem) {
				java.lang.reflect.Array.set(array, i, replaced);
			}
		}
	}

	private boolean isMyBatisParamMap(Object obj) {
		return obj != null && MapperMethod.ParamMap.class.isAssignableFrom(obj.getClass());
	}

	// 判断字符串是否全部为数字字符（避免使用正则在热路径产生额外开销）
	private boolean isAllDigits(String s) {
		if (s == null || s.isEmpty()) {
			return false;
		}
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c < '0' || c > '9') {
				return false;
			}
		}
		return true;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void encryptMap(Map<?, ?> map, CryptoContext context, Set<String> keys, ProcessingCtx ctx) {
		if (map == null || map.isEmpty()) {
			return;
		}
		if (context == null) {
			return; // 无上下文不处理
		}
		if (keys == null || keys.isEmpty()) {
			return; // 空集合表示忽略
		}
		boolean all = isWildcardAll(keys);
		for (Map.Entry<?, ?> e : map.entrySet()) {
			Object k = e.getKey();
			Object v = e.getValue();
			// 仅当为全量或指定位字符串 key 时处理
			if (!all) {
				if (!(k instanceof String) || !keys.contains(k)) {
					continue;
				}
			}
			if (v instanceof String) {
				String val = (String) v;
				if (!val.isEmpty()) {
					String cipher = encryptStringOrFallback(val, context, "map-entry:" + k, ctx);
					if (cipher != null && !cipher.equals(val)) {
						if (ctx.shouldRecord()) {
							ctx.recordMapEntry((Map<Object, Object>) map, k, val);
						}
						((Map) map).put(k, cipher);
					}
				}
				continue;
			}
			if (v != null) {
				// 复用实体字段加密逻辑，包含回滚记录与 processed 标记
				encryptEntityFields(v, ctx);
			}
		}
	}

	private Set<String> toKeySet(String[] arr) {
		if (arr == null || arr.length == 0) {
			return Collections.emptySet();
		}
		Set<String> s = new java.util.HashSet<>(arr.length * 2);
		for (String k : arr) {
			if (k != null) {
				s.add(k);
			}
		}
		return s;
	}

	private Map<Object, Group> buildParamGroups(Map<?, ?> paramMap, MybatisMethodMetadata mybatisMethodMetadata,
			MybatisParameterMetadata[] metas) {
		Map<Object, Group> groups = new IdentityHashMap<>();
		// pass 1: paramN / numeric indexes
		for (Map.Entry<?, ?> e : paramMap.entrySet()) {
			Object k = e.getKey();
			if (!(k instanceof String)) {
				continue;
			}
			String key = (String) k;
			Object value = e.getValue();
			int index = parseIndexFromKey(key);
			if (index >= 0) {
				int physical = mybatisMethodMetadata.toPhysical(index);
				if (physical >= 0) {
					Group g = groups.computeIfAbsent(value, v -> {
						Group ng = new Group();
						ng.value = v;
						return ng;
					});
					g.keys.add(key);
					g.indices.add(physical);
				}
			}
		}
		// pass 2: named @Param
		for (int i = 0; i < metas.length; i++) {
			String pname = metas[i].getParamName();
			if (pname == null || pname.isEmpty()) {
				continue;
			}
			if (paramMap.containsKey(pname)) {
				Object value = paramMap.get(pname);
				Group g = groups.computeIfAbsent(value, v -> {
					Group ng = new Group();
					ng.value = v;
					return ng;
				});
				g.keys.add(pname);
				g.indices.add(i);
			}
		}
		return groups;
	}

	private int parseIndexFromKey(String key) {
		if (key == null) {
			return -1;
		}
		if (key.startsWith(PARAM_PREFIX)) {
			try {
				return Integer.parseInt(key.substring(PARAM_PREFIX.length())) - 1; // 1-based
																					// ->
																					// 0-based
			}
			catch (Exception ignore) {
			}
		}
		else if (isAllDigits(key)) {
			try {
				return Integer.parseInt(key);
			}
			catch (Exception ignore) {
			}
		}
		return -1;
	}

	private MybatisParameterMetadata selectParameterMeta(Set<Integer> indices, MybatisParameterMetadata[] metas) {
		MybatisParameterMetadata fallback = null;
		for (Integer idx : indices) {
			if (idx == null || idx < 0 || idx >= metas.length) {
				continue;
			}
			MybatisParameterMetadata pm = metas[idx];
			if (pm != null && pm.isAnnotated()) {
				return pm;
			}
			if (fallback == null) {
				fallback = pm;
			}
		}
		return fallback;
	}

	private boolean isWildcardAll(Set<String> keys) {
		return keys.size() == 1 && keys.contains(WILDCARD_ALL);
	}

	// 小型回写辅助（已在下方以 BackRef 形式实现）

	private Object encryptParameterElement(Object elem, MybatisParameterMetadata meta, ProcessingCtx ctx,
			BackRef backRef) {
		if (elem == null) {
			return null;
		}
		// 如果元素是字符串，则必须有 @Encrypt 注解才处理
		if (elem instanceof String && meta != null && meta.isAnnotated()) {
			String cipher = encryptStringOrFallback((String) elem, meta.getContext(), "param-element", ctx);
			if (cipher != null && cipher != elem) {
				backRef.record(ctx, (String) elem);
				return cipher;
			}
			return elem;
		}
		// 对实体等继续走字段级逻辑
		encryptEntityFields(elem, ctx);
		return elem;
	}

	// Overload using ProcessingCtx (new path)
	private void encryptEntityFields(Object obj, ProcessingCtx ctx) {
		if (obj == null) {
			return;
		}
		if (ctx.isProcessed(obj)) {
			return;
		}
		ctx.markProcessed(obj);
		ClassMetaData meta = ctx.resolver.resolve(obj.getClass());
		if (meta == null || !meta.shouldProcess()) {
			return;
		}
		for (FieldMetaData fm : meta.getEncryptedFieldMap().values()) {
			Field f = fm.getField();
			try {
				Object val = f.get(obj);
				if (val instanceof String) {
					String original = (String) val;
					if (original.isEmpty()) {
						continue;
					}
					String cipher = encryptStringOrFallback(original, fm.getContext(), "entity-field:" + f.getName(),
							ctx);
					if (cipher != null && !cipher.equals(original)) {
						ctx.recordField(obj, f, original);
						f.set(obj, cipher);
					}
				}
			}
			catch (Exception e) {
				if (ctx.failFast) {
					throw new FieldCryptCryptoException("字段加密失败: " + f, e);
				}
				log.warn("字段加密失败忽略 field={} algo={}", f.getName(), fm.getAlgo(), e);
			}
		}
	}

	private String encryptStringOrFallback(String plain, CryptoContext context, String scope, ProcessingCtx ctx) {
		if (plain == null || plain.isEmpty()) {
			return plain;
		}
		try {
			return ctx.crypto.encrypt(plain, context);
		}
		catch (Exception e) {
			if (ctx.failFast) {
				throw new FieldCryptCryptoException("加密失败:" + scope, e);
			}
			log.warn("加密失败忽略 scope= {}", scope, e);
			return plain;
		}
	}

	// ---------- internal lightweight context & helpers ----------

	// ------- helper types & methods for readability -------

	private static final class Group {

		Object value;

		Set<String> keys = new LinkedHashSet<>();

		Set<Integer> indices = new LinkedHashSet<>();

	}

	private static final class ProcessingCtx {

		final CryptoEngine crypto;

		final ClassMetaResolver resolver;

		final MutationRecorder recorder;

		final boolean restore;

		final boolean failFast;

		final Set<Object> processed;

		ProcessingCtx(CryptoEngine crypto, ClassMetaResolver resolver, MutationRecorder recorder, boolean restore,
				boolean failFast, Set<Object> processed) {
			this.crypto = crypto;
			this.resolver = resolver;
			this.recorder = recorder;
			this.restore = restore;
			this.failFast = failFast;
			this.processed = processed;
		}

		boolean shouldRecord() {
			return this.restore && this.recorder != null;
		}

		boolean isProcessed(Object o) {
			return this.processed.contains(o);
		}

		void markProcessed(Object o) {
			this.processed.add(o);
		}

		void recordField(Object obj, Field f, String original) {
			if (shouldRecord()) {
				this.recorder.recordField(obj, f, original);
			}
		}

		void recordMapEntry(Map<Object, Object> map, Object key, String original) {
			if (shouldRecord()) {
				this.recorder.recordMapEntry(map, key, original);
			}
		}

		void recordListIndex(List<?> list, int index, String original) {
			if (shouldRecord()) {
				this.recorder.recordListIndex(list, index, original);
			}
		}

		void recordArrayIndex(Object array, int index, String original) {
			if (shouldRecord()) {
				this.recorder.recordArrayIndex(array, index, original);
			}
		}

		void recordDirect(Map<String, Object> paramMap, String key, String original) {
			if (shouldRecord()) {
				this.recorder.recordDirect(new MutationRecorder.DirectParamMutation(paramMap, key, null, original));
			}
		}

	}

	private interface BackRef {

		void record(ProcessingCtx ctx, String original);

	}

	private static final class ListBackRef implements BackRef {

		final List<?> list;

		final int index;

		ListBackRef(List<?> list, int index) {
			this.list = list;
			this.index = index;
		}

		@Override
		public void record(ProcessingCtx ctx, String original) {
			ctx.recordListIndex(this.list, this.index, original);
		}

	}

	private static final class ArrayBackRef implements BackRef {

		final Object array;

		final int index;

		ArrayBackRef(Object array, int index) {
			this.array = array;
			this.index = index;
		}

		@Override
		public void record(ProcessingCtx ctx, String original) {
			ctx.recordArrayIndex(this.array, this.index, original);
		}

	}

	private enum NullBackRef implements BackRef {

		INSTANCE;

		@Override
		public void record(ProcessingCtx ctx, String original) {
		}

	}

}
