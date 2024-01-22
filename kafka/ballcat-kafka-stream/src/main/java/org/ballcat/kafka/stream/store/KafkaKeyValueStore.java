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

package org.ballcat.kafka.stream.store;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.KeyValueStore;
import org.bson.types.ObjectId;

/**
 * 使用 {@link KeyValueStore} 为具体实现的kafka数据缓存方法
 *
 * @author lingting 2020/6/22 10:24
 */
public final class KafkaKeyValueStore<K, V> implements KafkaWindow<V, KeyValueStore<K, V>> {

	private KeyValueStore<K, V> store;

	private Supplier<K> supplier;

	private KafkaKeyValueStore() {
	}

	@SuppressWarnings("unchecked")
	public static <K, V> KafkaKeyValueStore<K, V> init(KeyValueStore<K, V> store) {
		return init(store, () -> (K) ObjectId.get().toString());
	}

	/**
	 * @param supplier 生成key的方式
	 */
	public static <K, V> KafkaKeyValueStore<K, V> init(KeyValueStore<K, V> store, Supplier<K> supplier) {
		KafkaKeyValueStore<K, V> keyValueStore = new KafkaKeyValueStore<>();
		keyValueStore.store = store;
		keyValueStore.supplier = supplier;
		return keyValueStore;
	}

	public KeyValueIterator<K, V> all() {
		return this.store.all();
	}

	public void forEachRemaining(Consumer<? super KeyValue<K, V>> action) {
		all().forEachRemaining(action);
	}

	public List<K> keys() {
		List<K> list = new ArrayList<>();
		all().forEachRemaining(kv -> list.add(kv.key));
		return list;
	}

	public List<V> values() {
		List<V> list = new ArrayList<>();
		all().forEachRemaining(kv -> list.add(kv.value));
		return list;
	}

	/**
	 * 获取插入数据的key
	 * @return 生成的key
	 */
	public K getKey() {
		return this.supplier.get();
	}

	public void put(V v) {
		pushValue(v, this.store);
	}

	public void put(K k, V v) {
		if (check(v)) {
			forkPush(k, v, this.store);
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
	 */
	@Override
	public void forkPush(V v, KeyValueStore<K, V> kvKeyValueStore) {
		forkPush(getKey(), v, kvKeyValueStore);
	}

	public void forkPush(K k, V v, KeyValueStore<K, V> kvKeyValueStore) {
		kvKeyValueStore.put(k, v);
	}

	public V delete(K key) {
		return this.store.delete(key);
	}

}
