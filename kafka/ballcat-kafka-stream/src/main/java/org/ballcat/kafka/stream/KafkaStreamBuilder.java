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

package org.ballcat.kafka.stream;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.regex.Pattern;

import lombok.Getter;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.processor.ProcessorSupplier;
import org.apache.kafka.streams.processor.StreamPartitioner;
import org.apache.kafka.streams.processor.TimestampExtractor;
import org.apache.kafka.streams.processor.TopicNameExtractor;
import org.apache.kafka.streams.state.StoreBuilder;

/**
 * kafka Stream 流构建方法
 *
 * @author lingting 2020/6/23 19:31
 */
public class KafkaStreamBuilder {

	private static final String BOOTSTRAP_SERVERS_DELIMITER = ",";

	@Getter
	private Topology topology = new Topology();

	private final Properties properties = new Properties();

	private final Set<String> bootstrapServers = new HashSet<>();

	public KafkaStreamBuilder keySerde(Class<? extends Serde<?>> c) {
		return keySerde(c.getName());
	}

	public KafkaStreamBuilder keySerde(String className) {
		return put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, className);
	}

	public KafkaStreamBuilder valueSerde(Class<? extends Serde<?>> c) {
		return valueSerde(c.getName());
	}

	public KafkaStreamBuilder valueSerde(String className) {
		return put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, className);
	}

	/**
	 * 添加 kafka 路径 host:port
	 *
	 */
	public KafkaStreamBuilder addBootstrapServers(String uri) {
		this.bootstrapServers.add(uri);
		return this;
	}

	public KafkaStreamBuilder addAllBootstrapServers(Collection<String> uris) {
		this.bootstrapServers.addAll(uris);
		return this;
	}

	/**
	 * 添加配置
	 */
	public KafkaStreamBuilder put(Object key, Object val) {
		this.properties.put(key, val);
		return this;
	}

	/**
	 * 添加配置
	 */
	public KafkaStreamBuilder putAll(Properties properties) {
		this.properties.putAll(properties);
		return this;
	}

	public KafkaStreamBuilder putAll(Map<?, ?> map) {
		this.properties.putAll(map);
		return this;
	}

	public KafkaStreamBuilder applicationId(String aId) {
		this.properties.put(StreamsConfig.APPLICATION_ID_CONFIG, aId);
		return this;
	}

	public synchronized KafkaStreamBuilder addSource(String name, String... topics) {
		this.topology.addSource(name, topics);
		return this;
	}

	public synchronized KafkaStreamBuilder addSource(String name, Pattern topicPattern) {
		this.topology.addSource(name, topicPattern);
		return this;
	}

	public synchronized KafkaStreamBuilder addSource(Topology.AutoOffsetReset offsetReset, String name,
			String... topics) {
		this.topology.addSource(offsetReset, name, topics);
		return this;
	}

	public synchronized KafkaStreamBuilder addSource(Topology.AutoOffsetReset offsetReset, String name,
			Pattern topicPattern) {
		this.topology.addSource(offsetReset, name, topicPattern);
		return this;
	}

	public synchronized KafkaStreamBuilder addSource(TimestampExtractor timestampExtractor, String name,
			String... topics) {
		this.topology.addSource(timestampExtractor, name, topics);
		return this;
	}

	public synchronized KafkaStreamBuilder addSource(TimestampExtractor timestampExtractor, String name,
			Pattern topicPattern) {
		this.topology.addSource(timestampExtractor, name, topicPattern);
		return this;
	}

	public synchronized KafkaStreamBuilder addSource(Topology.AutoOffsetReset offsetReset,
			TimestampExtractor timestampExtractor, String name, String... topics) {
		this.topology.addSource(offsetReset, timestampExtractor, name, topics);
		return this;
	}

	public synchronized KafkaStreamBuilder addSource(Topology.AutoOffsetReset offsetReset,
			TimestampExtractor timestampExtractor, String name, Pattern topicPattern) {
		this.topology.addSource(offsetReset, timestampExtractor, name, topicPattern);
		return this;
	}

	public synchronized KafkaStreamBuilder addSource(String name, Deserializer<?> keyDeserializer,
			Deserializer<?> valueDeserializer, String... topics) {
		this.topology.addSource(name, keyDeserializer, valueDeserializer, topics);
		return this;
	}

	public synchronized KafkaStreamBuilder addSource(String name, Deserializer<?> keyDeserializer,
			Deserializer<?> valueDeserializer, Pattern topicPattern) {
		this.topology.addSource(name, keyDeserializer, valueDeserializer, topicPattern);
		return this;
	}

	public synchronized KafkaStreamBuilder addSource(Topology.AutoOffsetReset offsetReset, String name,
			Deserializer<?> keyDeserializer, Deserializer<?> valueDeserializer, String... topics) {
		this.topology.addSource(offsetReset, name, keyDeserializer, valueDeserializer, topics);
		return this;
	}

	public synchronized KafkaStreamBuilder addSource(Topology.AutoOffsetReset offsetReset, String name,
			Deserializer<?> keyDeserializer, Deserializer<?> valueDeserializer, Pattern topicPattern) {
		this.topology.addSource(offsetReset, name, keyDeserializer, valueDeserializer, topicPattern);
		return this;
	}

	public synchronized KafkaStreamBuilder addSource(Topology.AutoOffsetReset offsetReset, String name,
			TimestampExtractor timestampExtractor, Deserializer<?> keyDeserializer, Deserializer<?> valueDeserializer,
			String... topics) {
		this.topology.addSource(offsetReset, name, timestampExtractor, keyDeserializer, valueDeserializer, topics);
		return this;
	}

	public synchronized KafkaStreamBuilder addSource(Topology.AutoOffsetReset offsetReset, String name,
			TimestampExtractor timestampExtractor, Deserializer<?> keyDeserializer, Deserializer<?> valueDeserializer,
			Pattern topicPattern) {
		this.topology.addSource(offsetReset, name, timestampExtractor, keyDeserializer, valueDeserializer,
				topicPattern);
		return this;
	}

	public synchronized KafkaStreamBuilder addSink(String name, String topic, String... parentNames) {
		this.topology.addSink(name, topic, parentNames);
		return this;
	}

	public synchronized <K, V> KafkaStreamBuilder addSink(String name, String topic,
			StreamPartitioner<? super K, ? super V> partitioner, String... parentNames) {
		this.topology.addSink(name, topic, partitioner, parentNames);
		return this;
	}

	public synchronized <K, V> KafkaStreamBuilder addSink(String name, String topic, Serializer<K> keySerializer,
			Serializer<V> valueSerializer, String... parentNames) {
		this.topology.addSink(name, topic, keySerializer, valueSerializer, parentNames);
		return this;
	}

	public synchronized <K, V> KafkaStreamBuilder addSink(String name, String topic, Serializer<K> keySerializer,
			Serializer<V> valueSerializer, StreamPartitioner<? super K, ? super V> partitioner, String... parentNames) {
		this.topology.addSink(name, topic, keySerializer, valueSerializer, partitioner, parentNames);
		return this;
	}

	public synchronized <K, V> KafkaStreamBuilder addSink(String name, TopicNameExtractor<K, V> topicExtractor,
			String... parentNames) {
		this.topology.addSink(name, topicExtractor, parentNames);
		return this;
	}

	public synchronized <K, V> KafkaStreamBuilder addSink(String name, TopicNameExtractor<K, V> topicExtractor,
			StreamPartitioner<? super K, ? super V> partitioner, String... parentNames) {
		this.topology.addSink(name, topicExtractor, partitioner, parentNames);
		return this;
	}

	public synchronized <K, V> KafkaStreamBuilder addSink(String name, TopicNameExtractor<K, V> topicExtractor,
			Serializer<K> keySerializer, Serializer<V> valueSerializer, String... parentNames) {
		this.topology.addSink(name, topicExtractor, keySerializer, valueSerializer, parentNames);
		return this;
	}

	public synchronized <K, V> KafkaStreamBuilder addSink(String name, TopicNameExtractor<K, V> topicExtractor,
			Serializer<K> keySerializer, Serializer<V> valueSerializer,
			StreamPartitioner<? super K, ? super V> partitioner, String... parentNames) {
		this.topology.addSink(name, topicExtractor, keySerializer, valueSerializer, partitioner, parentNames);
		return this;
	}

	public synchronized KafkaStreamBuilder addProcessor(String name, ProcessorSupplier<?, ?> supplier,
			String... parentNames) {
		this.topology.addProcessor(name, supplier, parentNames);
		return this;
	}

	public synchronized KafkaStreamBuilder addStateStore(StoreBuilder<?> storeBuilder, String... processorNames) {
		this.topology.addStateStore(storeBuilder, processorNames);
		return this;
	}

	public synchronized <K, V> KafkaStreamBuilder addGlobalStore(StoreBuilder<?> storeBuilder, String sourceName,
			Deserializer<K> keyDeserializer, Deserializer<V> valueDeserializer, String topic, String processorName,
			ProcessorSupplier<K, V> stateUpdateSupplier) {
		this.topology.addGlobalStore(storeBuilder, sourceName, keyDeserializer, valueDeserializer, topic, processorName,
				stateUpdateSupplier);
		return this;
	}

	/**
	 * 与kafka原方法保持一致. 减低迁移成本
	 */
	@SuppressWarnings("java:S107")
	public synchronized <K, V> KafkaStreamBuilder addGlobalStore(StoreBuilder<?> storeBuilder, String sourceName,
			TimestampExtractor timestampExtractor, Deserializer<K> keyDeserializer, Deserializer<V> valueDeserializer,
			String topic, String processorName, ProcessorSupplier<K, V> stateUpdateSupplier) {
		this.topology.addGlobalStore(storeBuilder, sourceName, timestampExtractor, keyDeserializer, valueDeserializer,
				topic, processorName, stateUpdateSupplier);
		return this;
	}

	public synchronized KafkaStreamBuilder connectProcessorAndStateStores(String processorName,
			String... stateStoreNames) {
		this.topology.connectProcessorAndStateStores(processorName, stateStoreNames);
		return this;
	}

	/**
	 * 自定义的构筑方法， 传入 topology 和属性
	 */
	public KafkaStreams build(BiFunction<Topology, Properties, KafkaStreams> biFunction) {
		return biFunction.apply(this.topology, getProperties());
	}

	public KafkaStreams build(Properties properties) {
		return putAll(properties).build();
	}

	public KafkaStreams build(Topology topology) {
		this.topology = topology;
		return build();
	}

	public KafkaStreams build() {
		return build(KafkaStreams::new);
	}

	public Set<String> getBootstrapServers() {
		getProperties();
		return this.bootstrapServers;
	}

	public Properties getProperties() {
		this.bootstrapServers
			.addAll(Arrays.asList(this.properties.getProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "")
				.split(BOOTSTRAP_SERVERS_DELIMITER)));
		this.properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
				String.join(BOOTSTRAP_SERVERS_DELIMITER, this.bootstrapServers));
		return this.properties;
	}

}
