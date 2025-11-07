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

package org.ballcat.fieldcrypt.core.cache;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.ballcat.fieldcrypt.annotation.Encrypted;
import org.ballcat.fieldcrypt.annotation.EncryptedEntity;
import org.ballcat.fieldcrypt.core.ClassMetaData;
import org.ballcat.fieldcrypt.core.FieldMetaData;

/**
 * 解析与缓存 {@link ClassMetaData} 组件，仅缓存使用 {@link EncryptedEntity} 标注的类。 解析时会按继承规则收集父类加密字段。
 *
 * @author Hccake
 * @since 2.0.0
 */
public class ClassMetaResolver {

	private final Map<Class<?>, ClassMetaData> cache = new ConcurrentHashMap<>();

	public ClassMetaData resolve(Class<?> type) {
		if (type == null) {
			return null;
		}
		return this.cache.computeIfAbsent(type, this::build);
	}

	private ClassMetaData build(Class<?> type) {
		if (!type.isAnnotationPresent(EncryptedEntity.class)) {
			// 未标注的类型：标记为false，字段列表为空
			return new ClassMetaData(type, Collections.emptyList(), false);
		}
		List<FieldMetaData> metas = new ArrayList<>();
		collect(type, metas);
		return new ClassMetaData(type, metas, true);
	}

	private void collect(Class<?> type, List<FieldMetaData> out) {
		if (type == null || type == Object.class) {
			return;
		}

		// 只有当父类也标注了注解时才遍历父类（继承规则）
		Class<?> superType = type.getSuperclass();
		if (superType != null && superType.isAnnotationPresent(EncryptedEntity.class)) {
			collect(superType, out);
		}
		for (Field f : type.getDeclaredFields()) {
			Encrypted ds = f.getAnnotation(Encrypted.class);
			if (ds != null) {
				// 不在解析期回填默认算法，保留注解原样（空表示运行时由注册表默认算法决定）
				out.add(new FieldMetaData(f, ds.algo(), ds.params()));
			}
		}
	}

}
