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

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;
import org.ballcat.fieldcrypt.core.ClassMetaData;
import org.ballcat.fieldcrypt.core.FieldMetaData;
import org.ballcat.fieldcrypt.core.cache.ClassMetaResolver;
import org.ballcat.fieldcrypt.core.exception.FieldCryptCryptoException;
import org.ballcat.fieldcrypt.crypto.CryptoContext;
import org.ballcat.fieldcrypt.crypto.CryptoEngine;
import org.ballcat.fieldcrypt.mybatis.interceptor.internal.EncryptionContext;
import org.ballcat.fieldcrypt.mybatis.interceptor.internal.MutationRecorder;
import org.ballcat.fieldcrypt.mybatis.interceptor.internal.ParamMapGroupBuilder;
import org.ballcat.fieldcrypt.mybatis.interceptor.internal.ParameterGroup;
import org.ballcat.fieldcrypt.mybatis.metadata.MybatisMethodMetadata;
import org.ballcat.fieldcrypt.mybatis.metadata.MybatisParameterMetadata;

/**
 * 参数加密器，负责处理 MyBatis 参数的加密逻辑。
 *
 * @author Hccake
 * @since 2.0.0
 */
@Slf4j
class ParameterEncryptor {

	private final CryptoEngine cryptoEngine;

	private final ClassMetaResolver classMetaResolver;

	ParameterEncryptor(CryptoEngine cryptoEngine, ClassMetaResolver classMetaResolver) {
		this.cryptoEngine = cryptoEngine;
		this.classMetaResolver = classMetaResolver;
	}

	/**
	 * 处理根参数：根据参数类型采用不同的加密策略。
	 */
	Object handleRootParameter(Object root, MybatisMethodMetadata mybatisMethodMetadata, EncryptionContext ctx) {
		if (root == null) {
			return null;
		}

		// ParamMap 场景：需要分组处理，避免同一对象重复加密
		if (root instanceof Map && isMyBatisParamMap(root)) {
			Map<?, ?> paramMap = (Map<?, ?>) root;
			MybatisParameterMetadata[] metas = mybatisMethodMetadata.getParameters();
			ParamMapGroupBuilder builder = new ParamMapGroupBuilder(paramMap, mybatisMethodMetadata, metas);
			Map<Object, ParameterGroup> groups = builder.build();

			// 逐组处理：同一组内的所有 key 共享同一个加密结果
			for (ParameterGroup g : groups.values()) {
				encryptGroupedParameter(g, ctx, metas, paramMap);
			}
			return root;
		}

		// 单参数场景：直接加密，可能需要替换根对象
		MybatisParameterMetadata pmeta = mybatisMethodMetadata.size() == 1 ? mybatisMethodMetadata.getParameters()[0]
				: null;
		Object replaced = encryptSingleParameter(root, pmeta, ctx);
		return replaced == null ? root : replaced;
	}

	/**
	 * 针对 ParamMap 中同值（同一引用）的参数组进行集中处理，避免重复加密并保障回滚一致性。
	 */
	private void encryptGroupedParameter(ParameterGroup g, EncryptionContext ctx, MybatisParameterMetadata[] metas,
			Map<?, ?> paramMap) {
		if (g.getValue() == null || ctx.isProcessed(g.getValue())) {
			return;
		}

		// 选 meta：优先选择带注解的参数，否则回退到首个匹配参数
		MybatisParameterMetadata chosen = chooseFirstAnnotatedOrFirst(g.getIndices(), metas);

		// 统一调用单参数加密逻辑
		Object encrypted = encryptSingleParameter(g.getValue(), chosen, ctx);

		// 标记为已处理，避免重复加密
		ctx.markProcessed(g.getValue());

		// String 特殊处理：需要回写到 ParamMap 的所有相关 key
		if (g.getValue() instanceof String && encrypted != g.getValue()) {
			@SuppressWarnings("unchecked")
			Map<String, Object> pm = (Map<String, Object>) paramMap;
			String original = (String) g.getValue();
			String cipher = (String) encrypted;

			for (String k : g.getKeys()) {
				pm.put(k, cipher);
				@SuppressWarnings("unchecked")
				Map<Object, Object> recordMap = (Map<Object, Object>) (Map<?, ?>) pm;
				recordIfNeeded(ctx, MutationRecorder.mapEntryMutation(recordMap, k, original));
			}
		}
	}

	/**
	 * 加密单个参数：处理非 ParamMap 场景的参数对象，或 ParamMap 中无注解的参数值。 支持参数级注解和字段级加密。
	 */
	private Object encryptSingleParameter(Object value, MybatisParameterMetadata pmeta, EncryptionContext ctx) {
		if (value == null) {
			return null;
		}

		// String：只有带注解时才处理
		if (value instanceof String) {
			if (pmeta != null && pmeta.isAnnotated()) {
				if (log.isDebugEnabled()) {
					log.debug("Encrypt parameter-level String algo={}", pmeta.getAlgo());
				}
				return encryptStringOrFallback((String) value, pmeta.getContext(), "param-string", ctx);
			}
			return value;
		}

		// Map：只有带注解时才处理
		if (value instanceof Map) {
			encryptMap((Map<?, ?>) value, pmeta, ctx);
			return value;
		}

		// Collection：传入 pmeta（可能为 null）
		if (value instanceof Collection) {
			encryptCollection((Collection<?>) value, pmeta, ctx);
			return value;
		}

		// Array：传入 pmeta（可能为 null）
		if (value.getClass().isArray()) {
			encryptArray(value, pmeta, ctx);
			return value;
		}

		// 实体对象：走字段级加密
		encryptEntityFields(value, ctx);
		return value;
	}

	/**
	 * 加密 Map：根据参数元数据中指定的 key 集合进行加密。
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void encryptMap(Map<?, ?> map, MybatisParameterMetadata pmeta, EncryptionContext ctx) {
		if (map == null || map.isEmpty()) {
			return;
		}
		if (ctx.isProcessed(map)) {
			return;
		}
		if (pmeta == null || !pmeta.isAnnotated()) {
			return; // 无注解不处理
		}

		CryptoContext context = pmeta.getContext();
		if (context == null) {
			return; // 无上下文不处理
		}

		Set<String> keys = toKeySet(pmeta.getMapKeys());
		if (keys.isEmpty()) {
			return; // 空集合表示忽略
		}

		// 标记已处理
		ctx.markProcessed(map);

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
					String cipher = encryptStringOrFallback(val, context, "map-entry" + ":" + k, ctx);
					if (cipher != null && !cipher.equals(val)) {
						recordIfNeeded(ctx, MutationRecorder.mapEntryMutation((Map<Object, Object>) map, k, val));
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

	private Object encryptParameterElement(Object elem, MybatisParameterMetadata meta, EncryptionContext ctx,
			Object container, int index) {
		if (elem == null) {
			return null;
		}
		// 如果元素是字符串，则必须有 @Encrypt 注解才处理
		if (elem instanceof String && meta != null && meta.isAnnotated()) {
			String cipher = encryptStringOrFallback((String) elem, meta.getContext(), "param-element", ctx);
			if (cipher != null && cipher != elem) {
				// 记录回滚信息
				if (container instanceof List) {
					recordIfNeeded(ctx,
							MutationRecorder.listIndexMutation((List<Object>) container, index, (String) elem));
				}
				else if (container != null && container.getClass().isArray()) {
					recordIfNeeded(ctx, MutationRecorder.arrayIndexMutation(container, index, (String) elem));
				}
				return cipher;
			}
			return elem;
		}
		// 对实体等继续走字段级逻辑
		encryptEntityFields(elem, ctx);
		return elem;
	}

	/**
	 * 使用 ProcessingCtx 的实体字段加密路径
	 */
	private void encryptEntityFields(Object obj, EncryptionContext ctx) {
		if (obj == null) {
			return;
		}
		if (ctx.isProcessed(obj)) {
			return;
		}
		ctx.markProcessed(obj);
		ClassMetaData meta = this.classMetaResolver.resolve(obj.getClass());
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
					String cipher = encryptStringOrFallback(original, fm.getContext(),
							"entity-field" + ":" + f.getName(), ctx);
					if (cipher != null && !cipher.equals(original)) {
						recordIfNeeded(ctx, MutationRecorder.fieldMutation(obj, f, original));
						f.set(obj, cipher);
					}
				}
			}
			catch (Exception e) {
				if (ctx.isFailFast()) {
					throw new FieldCryptCryptoException("字段加密失败: " + f, e);
				}
				log.warn("字段加密失败忽略 field={} algo={}", f.getName(), fm.getAlgo(), e);
			}
		}
	}

	private String encryptStringOrFallback(String plain, CryptoContext context, String scope, EncryptionContext ctx) {
		if (plain == null || plain.isEmpty()) {
			return plain;
		}
		try {
			return this.cryptoEngine.encrypt(plain, context);
		}
		catch (Exception e) {
			if (ctx.isFailFast()) {
				throw new FieldCryptCryptoException("加密失败:" + scope, e);
			}
			log.warn("加密失败忽略 scope= {}", scope, e);
			return plain;
		}
	}

	/**
	 * 加密集合：根据集合类型分派到不同的处理方法。
	 */
	private void encryptCollection(Collection<?> c, MybatisParameterMetadata pmeta, EncryptionContext ctx) {
		if (c == null || c.isEmpty()) {
			return;
		}
		if (ctx.isProcessed(c)) {
			return;
		}
		ctx.markProcessed(c);

		if (c instanceof List) {
			encryptListElements((List<?>) c, pmeta, ctx);
		}
		else {
			encryptNonListCollection(c, pmeta, ctx);
		}
	}

	/**
	 * 加密 List 元素：支持索引访问，逐个元素原地修改。
	 */
	private void encryptListElements(List<?> list, MybatisParameterMetadata pmeta, EncryptionContext ctx) {
		@SuppressWarnings("unchecked")
		ListIterator<Object> it = (ListIterator<Object>) list.listIterator();
		while (it.hasNext()) {
			Object elem = it.next();
			int index = it.previousIndex();
			Object replaced = encryptParameterElement(elem, pmeta, ctx, list, index);
			if (replaced != elem && replaced instanceof String) {
				it.set(replaced);
			}
		}
	}

	/**
	 * 加密非 List 集合（Set、Queue 等）：不支持索引访问，需要批量替换。
	 */
	private void encryptNonListCollection(Collection<?> c, MybatisParameterMetadata pmeta, EncryptionContext ctx) {
		List<Object> originalElements = new ArrayList<>();
		List<Object> replacedElements = new ArrayList<>();
		boolean hasReplacement = false;

		for (Object elem : c) {
			if (elem == null) {
				originalElements.add(null);
				replacedElements.add(null);
				continue;
			}

			// 如果元素是字符串且有注解，则加密
			if (elem instanceof String && pmeta != null && pmeta.isAnnotated()) {
				String original = (String) elem;
				String cipher = encryptStringOrFallback(original, pmeta.getContext(), "param-element", ctx);
				if (cipher != null && !cipher.equals(original)) {
					originalElements.add(original);
					replacedElements.add(cipher);
					hasReplacement = true;
				}
				else {
					originalElements.add(original);
					replacedElements.add(original);
				}
			}
			else {
				// 非字符串元素，走字段级加密
				encryptEntityFields(elem, ctx);
				originalElements.add(elem);
				replacedElements.add(elem);
			}
		}

		// 如果有元素被替换，需要重建集合并记录回滚
		if (hasReplacement) {
			@SuppressWarnings("unchecked")
			Collection<Object> mutableC = (Collection<Object>) c;

			// 记录回滚信息（在修改之前）
			recordIfNeeded(ctx, MutationRecorder.collectionReplaceMutation(mutableC, originalElements));

			// 执行替换
			mutableC.clear();
			mutableC.addAll(replacedElements);
		}
	}

	/**
	 * 加密数组：遍历数组元素进行加密。
	 */
	private void encryptArray(Object array, MybatisParameterMetadata pmeta, EncryptionContext ctx) {
		if (array == null || !array.getClass().isArray()) {
			return;
		}
		int len = Array.getLength(array);
		if (len == 0) {
			return;
		}
		if (ctx.isProcessed(array)) {
			return;
		}
		ctx.markProcessed(array);

		for (int i = 0; i < len; i++) {
			Object elem = Array.get(array, i);
			Object replaced = encryptParameterElement(elem, pmeta, ctx, array, i);
			if (replaced != elem) {
				Array.set(array, i, replaced);
			}
		}
	}

	/**
	 * 从候选下标中选择参数元信息：优先返回带注解的参数，否则返回第一个候选（按传入 Set 的迭代顺序）。
	 */
	private MybatisParameterMetadata chooseFirstAnnotatedOrFirst(Set<Integer> indices,
			MybatisParameterMetadata[] metas) {
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

	// ========== 辅助方法 ==========

	/**
	 * 统一的记录辅助方法
	 */
	private void recordIfNeeded(EncryptionContext ctx, MutationRecorder.Mutation mutation) {
		if (ctx.getRecorder() != null) {
			ctx.getRecorder().record(mutation);
		}
	}

	private boolean isMyBatisParamMap(Object obj) {
		return obj != null && org.apache.ibatis.binding.MapperMethod.ParamMap.class.isAssignableFrom(obj.getClass());
	}

	private Set<String> toKeySet(String[] arr) {
		if (arr == null || arr.length == 0) {
			return Collections.emptySet();
		}
		Set<String> s = new HashSet<>(arr.length * 2);
		for (String k : arr) {
			if (k != null) {
				s.add(k);
			}
		}
		return s;
	}

	private boolean isWildcardAll(Set<String> keys) {
		return keys.size() == 1 && keys.contains("*");
	}

}
