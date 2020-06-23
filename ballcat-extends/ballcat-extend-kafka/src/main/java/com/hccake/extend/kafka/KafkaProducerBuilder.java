package com.hccake.extend.kafka;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Collection;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import static com.hccake.extend.kafka.KafkaConstants.BOOTSTRAP_SERVERS_DELIMITER;

/**
 * 生产者
 * 具体的配置请参考 {@link ProducerConfig}
 * 这里只提供一些常用配置
 *
 * @author lingting  2020/5/19 20:56
 */
@Slf4j
public class KafkaProducerBuilder {
	private final Properties properties = new Properties();
	private final Set<String> bootstrapServers = new HashSet<>();

	public KafkaProducerBuilder keySerializer(Class<? extends Serializer<?>> c) {
		return keySerializer(c.getName());
	}

	public KafkaProducerBuilder keySerializer(String className) {
		return put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, className);
	}

	public KafkaProducerBuilder valueSerializer(Class<? extends Serializer<?>> c) {
		return valueSerializer(c.getName());
	}

	public KafkaProducerBuilder valueSerializer(String className) {
		return put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, className);
	}

	/**
	 * 添加 kafka 路径  host:port
	 *
	 * @author lingting  2020-06-19 16:30:03
	 */
	public KafkaProducerBuilder addBootstrapServers(String uri) {
		bootstrapServers.add(uri);
		return this;
	}

	public KafkaProducerBuilder addAllBootstrapServers(Collection<String> uris) {
		bootstrapServers.addAll(uris);
		return this;
	}

	/**
	 * 添加配置
	 *
	 * @author lingting  2020-06-19 16:30:50
	 */
	public KafkaProducerBuilder put(Object key, Object val) {
		properties.put(key, val);
		return this;
	}

	/**
	 * 添加配置
	 *
	 * @author lingting  2020-06-19 16:30:50
	 */
	public KafkaProducerBuilder putAll(Properties properties) {
		this.properties.putAll(properties);
		return this;
	}

	public <K, V> KafkaExtendProducer<K, V> build(Properties properties) {
		return putAll(properties).build();
	}

	public <K, V> KafkaExtendProducer<K, V> build() {
		bootstrapServers.addAll(ListUtil.toList(properties.getProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, StrUtil.EMPTY).split(BOOTSTRAP_SERVERS_DELIMITER)));
		properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, String.join(BOOTSTRAP_SERVERS_DELIMITER, bootstrapServers));
		return new KafkaExtendProducer<>(new org.apache.kafka.clients.producer.KafkaProducer<>(properties));
	}
}
