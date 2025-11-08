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

import lombok.Data;
import org.ballcat.fieldcrypt.crypto.CryptoContext;

/**
 * 方法参数元数据：记录是否有 {@link org.ballcat.fieldcrypt.annotation.Encrypted} 及其配置属性。 空实现策略：如未标注
 * annotated=false，其它字段忽略。
 *
 * @author Hccake
 * @since 2.0.0
 */
@Data
public class MybatisParameterMetadata {

	private final boolean annotated;

	private final String algo;

	private final String params;

	private final String[] mapKeys; // 原样保存

	private final Class<?> type;

	private final String paramName; // @Param 注解名称（可为空）

	// 预计算的上下文，避免热路径频繁 new
	private final CryptoContext context;

	public MybatisParameterMetadata(boolean annotated, String algo, String params, String[] mapKeys, Class<?> type,
			String paramName) {
		this.annotated = annotated;
		this.algo = algo;
		this.params = params;
		this.mapKeys = mapKeys == null ? new String[0] : mapKeys;
		this.type = type;
		this.paramName = paramName;
		this.context = annotated ? new CryptoContext(algo, params) : null;
	}

}
