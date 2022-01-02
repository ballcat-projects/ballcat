package com.hccake.extend.kafka;

import java.util.concurrent.Future;
import lombok.Getter;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.header.Header;

/**
 * 生产者扩展 提供了一些更加方便使用 生产者的方法
 *
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
		return producer.send(record, callback);
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
