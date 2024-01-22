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

package org.ballcat.common.util.json;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author lingting 2021/2/25 21:46
 */
public abstract class TypeReference<T> implements Comparable<TypeReference<T>> {

	protected final Type type;

	protected TypeReference() {
		Type superClass = getClass().getGenericSuperclass();
		if (superClass instanceof Class<?>) {
			throw new IllegalArgumentException(
					"Internal error: TypeReference constructed without actual type information");
		}
		this.type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
	}

	public Type getType() {
		return this.type;
	}

	@Override
	public int compareTo(TypeReference<T> o) {
		return 0;
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

}
