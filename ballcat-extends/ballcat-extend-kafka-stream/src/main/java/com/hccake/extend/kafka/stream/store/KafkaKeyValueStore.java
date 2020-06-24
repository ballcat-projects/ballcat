package com.hccake.extend.kafka.stream.store;

import cn.hutool.core.util.IdUtil;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.KeyValueStore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 使用 {@link KeyValueStore} 为具体实现的kafka数据缓存方法
 *
 * @author lingting 2020/6/22 10:24
 */
public class KafkaKeyValueStore<K, V> implements KafkaWindow<V, KeyValueStore<K, V>> {

	private KeyValueStore<K, V> store;

	private Supplier<K> supplier;

	private KafkaKeyValueStore() {
	}

	@SuppressWarnings("unchecked")
	public static <K, V> KafkaKeyValueStore<K, V> init(KeyValueStore<K, V> store) {
		return init(store, () -> (K) IdUtil.simpleUUID());
	}

	/**
	 * @param supplier 生成key的方式
	 * @author lingting 2020-06-22 10:43:34
	 */
	public static <K, V> KafkaKeyValueStore<K, V> init(KeyValueStore<K, V> store, Supplier<K> supplier) {
		KafkaKeyValueStore<K, V> keyValueStore = new KafkaKeyValueStore<>();
		keyValueStore.store = store;
		keyValueStore.supplier = supplier;
		return keyValueStore;
	}

	public KeyValueIterator<K, V> all() {
		return store.all();
	}

	public void forEachRemaining(Consumer<? super KeyValue<K, V>> action) {
		all().forEachRemaining(action);
	}

	public List<K> keys() {
		List<K> list = new ArrayList<>();
		all().forEachRemaining(kv -> {
			list.add(kv.key);
		});
		return list;
	}

	public List<V> values() {
		List<V> list = new ArrayList<>();
		all().forEachRemaining(kv -> {
			list.add(kv.value);
		});
		return list;
	}

	/**
	 * 获取插入数据的key
	 * @return 生成的key
	 * @author lingting 2020-06-22 10:14:15
	 */
	public K getKey() {
		return supplier.get();
	}

	public void put(V v) {
		pushValue(v, store);
	}

	public void put(K k, V v) {
		if (check(v)) {
			forkPush(k, v, store);
		}
	}

	public void put(KeyValue<K, V> kv) {
		put(kv.key, kv.value);
	}

	public void putAll(List<KeyValue<K, V>> kvList) {
		kvList.forEach(kv -> put(kv.key, kv.value));
	}

	public void putAll(Collection<V> vs) {
		vs.forEach(this::put);
	}

	/**
	 * 直接插入数据
	 * @param v 值
	 * @param kvKeyValueStore 目标
	 * @author lingting 2020-06-22 10:36:53
	 */
	@Override
	public void forkPush(V v, KeyValueStore<K, V> kvKeyValueStore) {
		forkPush(getKey(), v, kvKeyValueStore);
	}

	public void forkPush(K k, V v, KeyValueStore<K, V> kvKeyValueStore) {
		kvKeyValueStore.put(k, v);
	}

	public V delete(K key) {
		return store.delete(key);
	}

}
