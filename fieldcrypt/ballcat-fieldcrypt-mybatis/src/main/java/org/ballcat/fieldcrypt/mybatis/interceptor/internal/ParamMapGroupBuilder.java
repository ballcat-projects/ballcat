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

package org.ballcat.fieldcrypt.mybatis.interceptor.internal;

import java.util.IdentityHashMap;
import java.util.Map;

import org.ballcat.fieldcrypt.mybatis.metadata.MybatisMethodMetadata;
import org.ballcat.fieldcrypt.mybatis.metadata.MybatisParameterMetadata;

/**
 * MyBatis ParamMap 参数分组构建器。
 * <p>
 * 负责将 MyBatis 生成的 ParamMap 中的参数按实际对象引用分组。由于 MyBatis 会为同一个参数生成多个 key （如 param1、0、@Param
 * 命名等），此构建器将指向同一对象的所有 key 聚合到一个 ParameterGroup 中， 以便后续加密处理时避免重复加密，并能正确地将加密结果回写到所有相关的 key。
 */
public final class ParamMapGroupBuilder {

	private final Map<?, ?> paramMap;

	private final MybatisMethodMetadata methodMetadata;

	private final MybatisParameterMetadata[] paramMetas;

	private final Map<Object, ParameterGroup> groups = new IdentityHashMap<>();

	private static final String PARAM_PREFIX = "param";

	public ParamMapGroupBuilder(Map<?, ?> paramMap, MybatisMethodMetadata methodMetadata,
			MybatisParameterMetadata[] paramMetas) {
		this.paramMap = paramMap;
		this.methodMetadata = methodMetadata;
		this.paramMetas = paramMetas;
	}

	/**
	 * 构建参数分组映射。
	 * @return key 为参数对象引用（使用 IdentityHashMap），value 为对应的 ParameterGroup
	 */
	public Map<Object, ParameterGroup> build() {
		collectIndexedParameters();
		collectNamedParameters();
		return this.groups;
	}

	/**
	 * Pass 1: 收集索引参数（param1, param2, 0, 1 等）。
	 * <p>
	 * MyBatis 会为方法参数生成：
	 * <ul>
	 * <li>param1/param2/... （基于方法形参顺序，1-based）</li>
	 * <li>0/1/2/... （部分实现细节，某些插件/用法会出现）</li>
	 * </ul>
	 * 这里解析出逻辑下标后，通过 methodMetadata.toPhysical() 映射到实际物理位置。
	 */
	private void collectIndexedParameters() {
		for (Map.Entry<?, ?> entry : this.paramMap.entrySet()) {
			if (!(entry.getKey() instanceof String)) {
				continue;
			}
			String key = (String) entry.getKey();
			int logicalIndex = parseIndexFromKey(key);
			if (logicalIndex < 0) {
				continue;
			}

			int physicalIndex = this.methodMetadata.toPhysical(logicalIndex);
			if (physicalIndex >= 0) {
				addToGroup(entry.getValue(), key, physicalIndex);
			}
		}
	}

	/**
	 * Pass 2: 收集命名参数（@Param 注解）。
	 * <p>
	 * 若方法参数使用 @Param("name") 指定名称，ParamMap 也会包含该键。 这里将同一 value（同一引用）的各个键聚合到一个 Group
	 * 里，避免重复加密。
	 */
	private void collectNamedParameters() {
		for (int i = 0; i < this.paramMetas.length; i++) {
			String paramName = this.paramMetas[i].getParamName();
			if (paramName == null || paramName.isEmpty()) {
				continue;
			}
			if (this.paramMap.containsKey(paramName)) {
				addToGroup(this.paramMap.get(paramName), paramName, i);
			}
		}
	}

	/**
	 * 将 key 和 index 添加到对应对象的分组中。
	 */
	private void addToGroup(Object value, String key, int index) {
		ParameterGroup group = this.groups.computeIfAbsent(value, v -> new ParameterGroup());
		group.value = value;
		group.keys.add(key);
		group.indices.add(index);
	}

	/**
	 * 从 key 解析参数索引。
	 * @param key ParamMap 中的 key（如 "param1", "0" 等）
	 * @return 解析出的 0-based 索引，解析失败返回 -1
	 */
	private int parseIndexFromKey(String key) {
		if (key == null) {
			return -1;
		}
		// 处理 param1, param2, ... 格式（MyBatis 1-based）
		if (key.startsWith(PARAM_PREFIX)) {
			try {
				return Integer.parseInt(key.substring(PARAM_PREFIX.length())) - 1;
			}
			catch (NumberFormatException ignore) {
			}
		}
		// 处理纯数字格式 0, 1, 2, ...
		else if (isAllDigits(key)) {
			try {
				return Integer.parseInt(key);
			}
			catch (NumberFormatException ignore) {
			}
		}
		return -1;
	}

	/**
	 * 判断字符串是否全部为数字字符（避免使用正则在热路径产生额外开销）。
	 */
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

}
