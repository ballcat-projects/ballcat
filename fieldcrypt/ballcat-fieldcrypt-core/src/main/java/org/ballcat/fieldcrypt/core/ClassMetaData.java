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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import org.ballcat.fieldcrypt.annotation.EncryptedEntity;

/**
 * 被 {@link EncryptedEntity} 注解的类的元数据，包含其已标记需要加密的字段集合（包含满足继承规则的父类字段）。
 * <p>
 * 不可变结构，提升并发安全性。
 *
 * @author Hccake
 * @since 2.0.0
 */
@Getter
public final class ClassMetaData {

	private final Class<?> type;

	/**
	 * 已标记需要加密的字段映射（包含满足继承规则的父类字段）。 Key 为 Java 字段名（property name），Value 为对应字段的元数据。
	 * 为保证并发安全与只读语义，使用不可变 Map。
	 * <p>
	 * 获取按字段名索引的不可变映射。
	 * @see FieldMetaData
	 */
	private final Map<String, FieldMetaData> encryptedFieldMap;

	/**
	 * 类是否携带 @DataShieldEntity 注解。
	 * @see EncryptedEntity
	 */
	private final boolean entityAnnotated;

	public ClassMetaData(Class<?> type, Map<String, FieldMetaData> encryptedFieldMap, boolean entityAnnotated) {
		this.type = type;
		this.encryptedFieldMap = encryptedFieldMap == null ? Collections.unmodifiableMap(new HashMap<>())
				: Collections.unmodifiableMap(encryptedFieldMap);
		this.entityAnnotated = entityAnnotated;
	}

	public boolean hasEncryptedFields() {
		return !this.encryptedFieldMap.isEmpty();
	}

	/**
	 * 是否应该对此类进行加密/解密处理。当前规则：类被注解标记且至少有一个加密字段。
	 */
	public boolean shouldProcess() {
		return this.entityAnnotated && !this.encryptedFieldMap.isEmpty();
	}

}
