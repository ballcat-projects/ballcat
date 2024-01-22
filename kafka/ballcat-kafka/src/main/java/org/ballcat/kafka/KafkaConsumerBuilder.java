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

package org.ballcat.kafka;

import java.util.Arrays;
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
	 */
	public KafkaConsumerBuilder addBootstrapServers(String uri) {
		this.bootstrapServers.add(uri);
		return this;
	}

	public KafkaConsumerBuilder addAllBootstrapServers(Collection<String> uris) {
		this.bootstrapServers.addAll(uris);
		return this;
	}

	/**
	 * 添加配置
	 */
	public KafkaConsumerBuilder put(Object key, Object val) {
		this.properties.put(key, val);
		return this;
	}

	/**
	 * 添加配置
	 */
	public KafkaConsumerBuilder putAll(Properties properties) {
		this.properties.putAll(properties);
		return this;
	}

	/**
	 * 组id
	 */
	public KafkaConsumerBuilder groupId(String groupId) {
		return put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
	}

	public KafkaConsumerBuilder addTopic(String topic) {
		this.topics.add(topic);
		return this;
	}

	public KafkaConsumerBuilder addAllTopic(Collection<String> topics) {
		this.topics.addAll(topics);
		return this;
	}

	public <K, V> KafkaConsumer<K, V> build(Function<Properties, KafkaConsumer<K, V>> function) {
		KafkaConsumer<K, V> consumer = function.apply(getProperties());
		consumer.subscribe(this.topics);
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
		return this.bootstrapServers;
	}

	public Properties getProperties() {
		String nowServes = this.properties.getProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "");
		if (nowServes.length() > 0) {
			// 仅在存在配置时才插入
			this.bootstrapServers.addAll(Arrays.asList(nowServes.split(KafkaConstants.BOOTSTRAP_SERVERS_DELIMITER)));
		}
		this.properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
				String.join(KafkaConstants.BOOTSTRAP_SERVERS_DELIMITER, this.bootstrapServers));
		return this.properties;
	}

}
