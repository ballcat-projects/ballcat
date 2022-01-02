package com.hccake.extend.kafka.stream.core;

import com.hccake.extend.kafka.stream.store.KafkaKeyValueStore;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueStore;

/**
 * 所有 kafka 流处理执行相关类的顶级父类
 *
 * @author lingting 2020/6/22 11:02
 */
public interface Kafka {

	/**
	 * 获取上下文
	 * @return content
	 * @author lingting 2020-06-22 11:03:23
	 */
	ProcessorContext getContext();

	/**
	 * 获取 KeyValueStore
	 * @param name store name
	 * @return java.lang.String
	 * @author lingting 2020-06-22 09:57:37
	 */
	@SuppressWarnings("unchecked")
	default <K, V> KafkaKeyValueStore<K, V> getStore(String name) {
		return KafkaKeyValueStore.init((KeyValueStore<K, V>) getContext().getStateStore(name));
	}

}
