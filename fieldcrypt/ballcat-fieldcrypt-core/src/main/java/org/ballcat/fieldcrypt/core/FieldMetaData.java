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

package org.ballcat.fieldcrypt.core;

import java.lang.reflect.Field;

import lombok.Data;
import org.ballcat.fieldcrypt.crypto.CryptoContext;

/**
 * 单个需要加密字段的轻量级不可变元数据。 包含字段引用、解析后的算法标识以及透传参数。
 *
 * @author Hccake
 * @since 2.0.0
 */
@Data
public final class FieldMetaData {

	private final Field field;

	/**
	 * 加密算法标识符
	 * <p>
	 * 已解析的算法名称，当注解中未指定算法时会回退使用默认算法
	 * </p>
	 */
	private final String algo;

	/**
	 * 原始参数字符串
	 * <p>
	 * 从注解中获取的原始参数字符串，用于传递给加密算法的额外配置参数
	 * </p>
	 */
	private final String params;

	/**
	 * 预计算的加密上下文对象
	 * <p>
	 * 为了避免每次字段加密时都创建新的上下文对象，在初始化时预创建好CryptoContext实例
	 * </p>
	 */
	private final CryptoContext context;

	public FieldMetaData(Field field, String algo, String params) {
		this.field = field;
		this.algo = algo;
		this.params = params;
		this.field.setAccessible(true);
		this.context = new CryptoContext(algo, params);
	}

}
