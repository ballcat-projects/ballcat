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

package org.ballcat.autoconfigure.kafka;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import lombok.Data;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

/**
 * @author lingting 2020/7/28 21:15
 */
@Data
@ConfigurationProperties(prefix = "ballcat.kafka")
public class KafkaProperties {

	/**
	 * 用于指定分组
	 */
	private String groupId;

	/**
	 * 所属服务
	 */
	private Set<String> bootstrapServers;

	/**
	 * key 序列化
	 */
	private Class<? extends Serializer<?>> keySerializer = StringSerializer.class;

	/**
	 * key 序列化 类名, 如果填写本值则 valueSerializer 无效
	 */
	private String keySerializerClassName;

	/**
	 * value 序列化
	 */
	private Class<? extends Serializer<?>> valueSerializer = StringSerializer.class;

	/**
	 * value 序列化 类名, 如果填写本值则 valueSerializer 无效
	 */
	private String valueSerializerClassName;

	/**
	 * key 反序列化
	 */
	private Class<? extends Deserializer<?>> keyDeserializer = StringDeserializer.class;

	/**
	 * key 反序列化 类名, 如果填写本值则 keyDeserializer 无效
	 */
	private String keyDeserializerClassName;

	/**
	 * value 反序列化
	 */
	private Class<? extends Deserializer<?>> valueDeserializer = StringDeserializer.class;

	/**
	 * value 反序列化 类名, 如果填写本值则 valueDeserializer 无效
	 */
	private String valueDeserializerClassName;

	/**
	 * 额外参数
	 */
	private Map<String, Object> extend = new HashMap<>();

	public String getKeyDeserializerClassName() {
		if (StringUtils.hasText(this.keyDeserializerClassName)) {
			return this.keyDeserializerClassName;
		}
		return getKeyDeserializer().getName();
	}

	public String getValueDeserializerClassName() {
		if (StringUtils.hasText(this.valueDeserializerClassName)) {
			return this.valueDeserializerClassName;
		}
		return getValueDeserializer().getName();
	}

	public String getKeySerializerClassName() {
		if (StringUtils.hasText(this.keySerializerClassName)) {
			return this.keySerializerClassName;
		}
		return getKeySerializer().getName();
	}

	public String getValueSerializerClassName() {
		if (StringUtils.hasText(this.valueSerializerClassName)) {
			return this.valueSerializerClassName;
		}
		return getValueSerializer().getName();
	}

}
