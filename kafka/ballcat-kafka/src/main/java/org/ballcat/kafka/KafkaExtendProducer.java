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

import java.util.concurrent.Future;

import lombok.Getter;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.header.Header;

/**
 * 生产者扩展 提供了一些更加方便使用 生产者的方法
 * <p>
 * 忽略sonar异常. record 方法名需要与 kafka默认提供的方法名保持同名. 避免误导
 *
 * @author lingting 2020/6/23 17:11
 */
@SuppressWarnings("java:S6213")
public class KafkaExtendProducer<K, V> {

	@Getter
	private final KafkaProducer<K, V> producer;

	public KafkaExtendProducer(KafkaProducer<K, V> producer) {
		this.producer = producer;
	}

	public ProducerRecord<K, V> record(String topic, Integer partition, Long timestamp, K key, V value,
			Iterable<Header> headers) {
		return new ProducerRecord<>(topic, partition, timestamp, key, value, headers);
	}

	public ProducerRecord<K, V> record(String topic, Integer partition, Long timestamp, K key, V value) {
		return record(topic, partition, timestamp, key, value, null);
	}

	public ProducerRecord<K, V> record(String topic, Integer partition, K key, V value, Iterable<Header> headers) {
		return record(topic, partition, null, key, value, headers);
	}

	public ProducerRecord<K, V> record(String topic, Integer partition, K key, V value) {
		return record(topic, partition, null, key, value, null);
	}

	public ProducerRecord<K, V> record(String topic, K key, V value) {
		return record(topic, null, null, key, value, null);
	}

	public ProducerRecord<K, V> record(String topic, V value) {
		return record(topic, null, null, null, value, null);
	}

	public Future<RecordMetadata> send(ProducerRecord<K, V> record, Callback callback) {
		return this.producer.send(record, callback);
	}

	public Future<RecordMetadata> send(String topic, Integer partition, Long timestamp, K key, V value,
			Iterable<Header> headers) {
		return send(record(topic, partition, timestamp, key, value, headers), null);
	}

	public Future<RecordMetadata> send(String topic, Integer partition, Long timestamp, K key, V value) {
		return send(topic, partition, timestamp, key, value, null);
	}

	public Future<RecordMetadata> send(String topic, Integer partition, K key, V value, Iterable<Header> headers) {
		return send(topic, partition, null, key, value, headers);
	}

	public Future<RecordMetadata> send(String topic, Integer partition, K key, V value) {
		return send(topic, partition, null, key, value, null);
	}

	public Future<RecordMetadata> send(String topic, K key, V value) {
		return send(topic, null, null, key, value, null);
	}

	public Future<RecordMetadata> send(String topic, V value) {
		return send(topic, null, null, null, value, null);
	}

	public Future<RecordMetadata> send(String topic, V value, Callback callback) {
		return send(record(topic, value), callback);
	}

}
