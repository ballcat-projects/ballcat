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
package org.ballcat.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serializer;

import java.util.*;
import java.util.function.Function;

import static org.ballcat.kafka.KafkaConstants.BOOTSTRAP_SERVERS_DELIMITER;

/**
 * 生产者 具体的配置请参考 {@link ProducerConfig} 这里只提供一些常用配置
 *
 * @author lingting 2020/5/19 20:56
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
	 * 添加 kafka 路径 host:port
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
	 */
	public KafkaProducerBuilder put(Object key, Object val) {
		properties.put(key, val);
		return this;
	}

	/**
	 * 添加配置
	 */
	public KafkaProducerBuilder putAll(Properties properties) {
		this.properties.putAll(properties);
		return this;
	}

	public KafkaProducerBuilder putAll(Map<?, ?> map) {
		this.properties.putAll(map);
		return this;
	}

	public <K, V> KafkaExtendProducer<K, V> build(Function<Properties, KafkaExtendProducer<K, V>> function) {
		return function.apply(getProperties());
	}

	public <K, V> KafkaExtendProducer<K, V> build(Properties properties) {
		return putAll(properties).build();
	}

	public <K, V> KafkaExtendProducer<K, V> build() {
		return build(p -> new KafkaExtendProducer<>(new org.apache.kafka.clients.producer.KafkaProducer<>(p)));
	}

	public Set<String> getBootstrapServers() {
		getProperties();
		return bootstrapServers;
	}

	public Properties getProperties() {
		bootstrapServers.addAll(Arrays.asList(properties.getProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "")
			.split(BOOTSTRAP_SERVERS_DELIMITER)));
		properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
				String.join(BOOTSTRAP_SERVERS_DELIMITER, bootstrapServers));
		return properties;
	}

}
