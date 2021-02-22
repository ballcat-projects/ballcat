package com.hccake.extend.kafka;

import static com.hccake.extend.kafka.KafkaConstants.BOOTSTRAP_SERVERS_DELIMITER;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import java.util.Collection;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.function.Function;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.Deserializer;

/**
 * 消费者 具体的配置请参考 {@link ConsumerConfig} 这里只提供一些常用配置
 *
 * @author lingting 2020/5/19 20:56
 */
@Slf4j
public class KafkaConsumerBuilder {

	private final Properties properties = new Properties();

	private final Set<String> bootstrapServers = new HashSet<>();

	@Getter
	private final Set<String> topics = new HashSet<>();

	public KafkaConsumerBuilder keyDeserializer(Class<? extends Deserializer<?>> c) {
		return keyDeserializer(c.getName());
	}

	public KafkaConsumerBuilder keyDeserializer(String className) {
		return put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, className);
	}

	public KafkaConsumerBuilder valueDeserializer(Class<? extends Deserializer<?>> c) {
		return valueDeserializer(c.getName());
	}

	public KafkaConsumerBuilder valueDeserializer(String className) {
		return put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, className);
	}

	/**
	 * 添加 kafka 路径 host:port
	 *
	 * @author lingting 2020-06-19 16:30:03
	 */
	public KafkaConsumerBuilder addBootstrapServers(String uri) {
		bootstrapServers.add(uri);
		return this;
	}

	public KafkaConsumerBuilder addAllBootstrapServers(Collection<String> uris) {
		bootstrapServers.addAll(uris);
		return this;
	}

	/**
	 * 添加配置
	 *
	 * @author lingting 2020-06-19 16:30:50
	 */
	public KafkaConsumerBuilder put(Object key, Object val) {
		properties.put(key, val);
		return this;
	}

	/**
	 * 添加配置
	 *
	 * @author lingting 2020-06-19 16:30:50
	 */
	public KafkaConsumerBuilder putAll(Properties properties) {
		this.properties.putAll(properties);
		return this;
	}

	/**
	 * 组id
	 *
	 * @author lingting 2020-06-19 16:46:32
	 */
	public KafkaConsumerBuilder groupId(String groupId) {
		return put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
	}

	public KafkaConsumerBuilder addTopic(String topic) {
		topics.add(topic);
		return this;
	}

	public KafkaConsumerBuilder addAllTopic(Collection<String> topics) {
		this.topics.addAll(topics);
		return this;
	}

	public <K, V> KafkaConsumer<K, V> build(Function<Properties, KafkaConsumer<K, V>> function) {
		KafkaConsumer<K, V> consumer = function.apply(getProperties());
		consumer.subscribe(topics);
		return consumer;
	}

	public <K, V> KafkaConsumer<K, V> build(Properties properties) {
		return putAll(properties).build();
	}

	public <K, V> KafkaConsumer<K, V> build() {
		return build(KafkaConsumer::new);
	}

	public Set<String> getBootstrapServers() {
		getProperties();
		return bootstrapServers;
	}

	public Properties getProperties() {
		String nowServes = properties.getProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, StrUtil.EMPTY);
		if (StrUtil.isNotBlank(nowServes)) {
			// 仅在存在配置时才插入
			bootstrapServers.addAll(ListUtil.toList(nowServes.split(BOOTSTRAP_SERVERS_DELIMITER)));
		}
		properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
				String.join(BOOTSTRAP_SERVERS_DELIMITER, bootstrapServers));
		return properties;
	}

}
