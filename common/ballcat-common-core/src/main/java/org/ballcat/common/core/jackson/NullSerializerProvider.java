/*
 * Copyright 2023 the original author or authors.
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
package org.ballcat.common.core.jackson;

import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;

import java.util.Collection;
import java.util.Map;

/**
 * <p>
 * 修改了 Null 值的序列化器提供者
 * </p>
 *
 * <ul>
 * <li>String 类型，null 值转为 '' 输出
 * <li>集合、数组，null 值转为 [] 输出
 * <li>Map 类型，null 值转为 {} 输出
 * <ul/>
 *
 * @author hccake
 */
public class NullSerializerProvider extends DefaultSerializerProvider {

	private static final long serialVersionUID = 1L;

	public NullSerializerProvider() {
		super();
	}

	public NullSerializerProvider(NullSerializerProvider src) {
		super(src);
	}

	protected NullSerializerProvider(SerializerProvider src, SerializationConfig config, SerializerFactory f) {
		super(src, config, f);
	}

	/**
	 * null array 或 list，set 则转 '[]'
	 */
	private final JsonSerializer<Object> nullArrayJsonSerializer = new NullArrayJsonSerializer();

	/**
	 * null Map 转 '{}'
	 */
	private final JsonSerializer<Object> nullMapJsonSerializer = new NullMapJsonSerializer();

	/**
	 * null 字符串转 ''
	 */
	private final JsonSerializer<Object> nullStringJsonSerializer = new NullStringJsonSerializer();

	@Override
	public DefaultSerializerProvider copy() {
		if (getClass() != NullSerializerProvider.class) {
			return super.copy();
		}
		return new NullSerializerProvider(this);
	}

	@Override
	public NullSerializerProvider createInstance(SerializationConfig config, SerializerFactory jsf) {
		return new NullSerializerProvider(this, config, jsf);
	}

	@Override
	public JsonSerializer<Object> findNullValueSerializer(BeanProperty property) throws JsonMappingException {
		JavaType propertyType = property.getType();
		if (isStringType(propertyType)) {
			return this.nullStringJsonSerializer;
		}
		else if (isArrayOrCollectionTrype(propertyType)) {
			return this.nullArrayJsonSerializer;
		}
		else if (isMapType(propertyType)) {
			return this.nullMapJsonSerializer;
		}
		else {
			return super.findNullValueSerializer(property);
		}
	}

	/**
	 * 是否是 String 类型
	 * @param type JavaType
	 * @return boolean
	 */
	private boolean isStringType(JavaType type) {
		Class<?> clazz = type.getRawClass();
		return String.class.isAssignableFrom(clazz);
	}

	/**
	 * 是否是Map类型
	 * @param type JavaType
	 * @return boolean
	 */
	private boolean isMapType(JavaType type) {
		Class<?> clazz = type.getRawClass();
		return Map.class.isAssignableFrom(clazz);
	}

	/**
	 * 是否是集合类型或者数组
	 * @param type JavaType
	 * @return boolean
	 */
	private boolean isArrayOrCollectionTrype(JavaType type) {
		Class<?> clazz = type.getRawClass();
		return clazz.isArray() || Collection.class.isAssignableFrom(clazz);

	}

}
