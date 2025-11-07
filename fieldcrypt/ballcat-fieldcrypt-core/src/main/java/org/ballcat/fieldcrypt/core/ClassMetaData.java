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
import java.util.List;

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
	 * 已标记需要加密的字段集合（包含满足继承规则的父类字段）。
	 * @see FieldMetaData
	 */
	private final List<FieldMetaData> encryptedFields;

	/**
	 * 类是否携带 @DataShieldEntity 注解。
	 * @see EncryptedEntity
	 */
	private final boolean entityAnnotated;

	public ClassMetaData(Class<?> type, List<FieldMetaData> encryptedFields, boolean entityAnnotated) {
		this.type = type;
		this.encryptedFields = Collections.unmodifiableList(encryptedFields);
		this.entityAnnotated = entityAnnotated;
	}

	public boolean hasEncryptedFields() {
		return !this.encryptedFields.isEmpty();
	}

	/**
	 * 是否应该对此类进行加密/解密处理。当前规则：类被注解标记且至少有一个加密字段。
	 */
	public boolean shouldProcess() {
		return this.entityAnnotated && !this.encryptedFields.isEmpty();
	}

}
