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

package org.ballcat.redis.serialize;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

/**
 * @author Hccake 2019/9/9 11:09
 */
public interface CacheSerializer {

	/**
	 * 序列化方法
	 * @param cacheData 缓存数据
	 * @return String 序列化后的字符串
	 * @throws IOException 序列化异常
	 */
	String serialize(Object cacheData) throws IOException;

	/**
	 * 反序列化方法
	 * @param cacheData 缓存数据
	 * @param type Java 对象类型
	 * @return java 对象实例
	 * @throws IOException 序列化异常
	 */
	Object deserialize(String cacheData, Type type) throws IOException;

	/**
	 * Type转JavaType
	 * @param type Java 对象类型
	 * @return jackson 中的对象类型抽象
	 */
	@SuppressWarnings("java:S3878")
	static JavaType getJavaType(Type type) {
		// 判断是否带有泛型
		if (type instanceof ParameterizedType) {
			Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
			// 获取泛型类型
			Class<?> rowClass = (Class<?>) ((ParameterizedType) type).getRawType();

			JavaType[] javaTypes = new JavaType[actualTypeArguments.length];

			for (int i = 0; i < actualTypeArguments.length; i++) {
				// 泛型也可能带有泛型，递归获取
				javaTypes[i] = getJavaType(actualTypeArguments[i]);
			}
			return TypeFactory.defaultInstance().constructParametricType(rowClass, javaTypes);
		}
		else {
			// 简单类型直接用该类构建JavaType
			Class<?> cla = (Class<?>) type;
			return TypeFactory.defaultInstance().constructParametricType(cla, new JavaType[0]);
		}
	}

}
