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

package org.ballcat.fieldcrypt.mybatisplus.encrypt;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.ballcat.fieldcrypt.core.FieldMetaData;
import org.ballcat.fieldcrypt.crypto.CryptoContext;
import org.ballcat.fieldcrypt.crypto.CryptoEngine;

/**
 * 值加密器，用于对字段值进行加密处理
 * <p>
 * 该类提供了对不同类型值的加密支持，包括：
 * <ul>
 * <li>基本类型值（String、Number）的加密</li>
 * <li>数组类型值的加密</li>
 * <li>集合类型值的加密（List、Set等）</li>
 * </ul>
 * 加密过程会根据字段元数据和注册的加密引擎来执行具体的加密操作。
 * </p>
 *
 * @author Hccake
 * @since 2.0.0
 */
final class ValueEncryptor {

	private ValueEncryptor() {
	}

	static Object encryptValue(Object value, FieldMetaData fieldMeta, CryptoEngine registry) {
		if (value == null) {
			return null;
		}
		if (fieldMeta == null) {
			return value;
		}
		if (value instanceof String || value instanceof Number) {
			return encryptScalar(String.valueOf(value), fieldMeta, registry);
		}
		if (value.getClass().isArray()) {
			int len = Array.getLength(value);
			if (len == 0) {
				return value;
			}
			Object newArr = Array.newInstance(String.class, len);
			for (int i = 0; i < len; i++) {
				Object el = Array.get(value, i);
				Object enc = encryptElement(el, fieldMeta, registry);
				Array.set(newArr, i, enc);
			}
			return newArr;
		}
		if (value instanceof Collection) {
			Collection<?> c = (Collection<?>) value;
			if (c.isEmpty()) {
				return value;
			}
			if (value instanceof Set) {
				Set<Object> out = new LinkedHashSet<>(c.size());
				for (Object el : c) {
					out.add(encryptElement(el, fieldMeta, registry));
				}
				return out;
			}
			// 剩下的都当 List 处理
			List<Object> out = new ArrayList<>(c.size());
			for (Object el : c) {
				out.add(encryptElement(el, fieldMeta, registry));
			}
			return out;
		}
		return encryptElement(value, fieldMeta, registry);
	}

	private static Object encryptElement(Object el, FieldMetaData fieldMeta, CryptoEngine registry) {
		if (el == null) {
			return null;
		}
		if (el instanceof String || el instanceof Number) {
			return encryptScalar(String.valueOf(el), fieldMeta, registry);
		}
		return el;
	}

	private static String encryptScalar(String plain, FieldMetaData fieldMeta, CryptoEngine registry) {
		CryptoContext ctx = fieldMeta.getContext();
		return registry.encrypt(plain, ctx);
	}

}
