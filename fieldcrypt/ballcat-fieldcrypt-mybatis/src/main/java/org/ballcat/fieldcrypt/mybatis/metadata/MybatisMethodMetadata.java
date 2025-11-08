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

package org.ballcat.fieldcrypt.mybatis.metadata;

import lombok.Getter;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

/**
 * 方法级元数据：保存每个参数的 ParameterMeta 顺序数组。
 *
 * @author Hccake
 * @since 2.0.0
 */
public class MybatisMethodMetadata {

	@Getter
	private final MybatisParameterMetadata[] parameters;

	// 结果集解密元数据（方法级注解）
	@Getter
	private final MybatisResultMetadata result;

	// logicalToPhysical: 逻辑索引 -> 物理索引（跳过 RowBounds/ResultHandler）
	private final int[] logicalToPhysical;

	public MybatisMethodMetadata(MybatisParameterMetadata[] parameters) {
		this(parameters, MybatisResultMetadata.ABSENT);
	}

	public MybatisMethodMetadata(MybatisParameterMetadata[] parameters, MybatisResultMetadata result) {
		this.parameters = parameters == null ? new MybatisParameterMetadata[0] : parameters;
		// 预计算逻辑到物理的映射
		java.util.List<Integer> list = new java.util.ArrayList<>();
		for (int i = 0; i < this.parameters.length; i++) {
			Class<?> t = this.parameters[i].getType();
			if (isSpecialParameter(t)) {
				continue;
			}
			list.add(i);
		}
		this.logicalToPhysical = new int[list.size()];
		for (int i = 0; i < list.size(); i++) {
			this.logicalToPhysical[i] = list.get(i);
		}
		this.result = (result == null ? MybatisResultMetadata.ABSENT : result);
	}

	public int size() {
		return this.parameters.length;
	}

	public int logicalSize() {
		return this.logicalToPhysical.length;
	}

	public int toPhysical(int logicalIndex) {
		if (logicalIndex < 0 || logicalIndex >= this.logicalToPhysical.length) {
			return -1;
		}
		return this.logicalToPhysical[logicalIndex];
	}

	private boolean isSpecialParameter(Class<?> clazz) {
		if (clazz == null) {
			return false;
		}
		// 与 MyBatis ParamNameResolver.isSpecialParameter 一致
		return RowBounds.class.isAssignableFrom(clazz) || ResultHandler.class.isAssignableFrom(clazz);
	}

}
