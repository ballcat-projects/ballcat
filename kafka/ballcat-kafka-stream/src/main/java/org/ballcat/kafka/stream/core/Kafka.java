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

package org.ballcat.kafka.stream.core;

import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueStore;
import org.ballcat.kafka.stream.store.KafkaKeyValueStore;

/**
 * 所有 kafka 流处理执行相关类的顶级父类
 *
 * @author lingting 2020/6/22 11:02
 */
public interface Kafka {

	/**
	 * 获取上下文
	 * @return content
	 */
	ProcessorContext getContext();

	/**
	 * 获取 KeyValueStore
	 * @param name store name
	 * @return java.lang.String
	 */
	@SuppressWarnings("unchecked")
	default <K, V> KafkaKeyValueStore<K, V> getStore(String name) {
		return KafkaKeyValueStore.init((KeyValueStore<K, V>) getContext().getStateStore(name));
	}

}
