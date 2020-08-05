package com.hccake.starter.kafka;

import lombok.Data;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author lingting 2020/7/28 21:15
 */
@Data
@ConfigurationProperties(prefix = "ballcat.starter.kafka")
public class KafkaProperties {

	/**
	 * 所属服务
	 */
	private Set<String> bootstrapServers;

	/**
	 * key 序列化
	 */
	private Class<? extends Serializer<?>> keySerializer = StringSerializer.class;

	/**
	 * key 序列化 类名
	 */
	private String keySerializerClassName;

	/**
	 * value 序列化
	 */
	private Class<? extends Serializer<?>> valueSerializer = StringSerializer.class;

	/**
	 * value 序列化 类名
	 */
	private String valueSerializerClassName;

	/**
	 * 额外参数
	 */
	private Map<String, Object> extend = new HashMap<>();

}
