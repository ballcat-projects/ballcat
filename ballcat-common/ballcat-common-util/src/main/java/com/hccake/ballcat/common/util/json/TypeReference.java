package com.hccake.ballcat.common.util.json;

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
		type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
	}

	public Type getType() {
		return type;
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
