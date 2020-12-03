package com.hccake.starter.kafka;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
		if (StrUtil.isNotEmpty(keyDeserializerClassName)) {
			return keyDeserializerClassName;
		}
		return getKeyDeserializer().getName();
	}

	public String getValueDeserializerClassName() {
		if (StrUtil.isNotEmpty(valueDeserializerClassName)) {
			return valueDeserializerClassName;
		}
		return getValueDeserializer().getName();
	}

	public String getKeySerializerClassName() {
		if (StrUtil.isNotEmpty(keySerializerClassName)) {
			return keySerializerClassName;
		}
		return getKeySerializer().getName();
	}

	public String getValueSerializerClassName() {
		if (StrUtil.isNotEmpty(valueSerializerClassName)) {
			return valueSerializerClassName;
		}
		return getValueSerializer().getName();
	}

}
