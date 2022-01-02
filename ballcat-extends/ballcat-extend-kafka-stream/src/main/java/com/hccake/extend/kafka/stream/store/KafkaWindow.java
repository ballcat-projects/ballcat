package com.hccake.extend.kafka.stream.store;

import cn.hutool.core.util.StrUtil;

/**
 * kafka 数据缓存类的接口
 *
 * @author lingting 2020/6/22 10:32
 */
public interface KafkaWindow<V, Values> {

	/**
	 * 数据通过校验才插入
	 * @param values 目标
	 * @param value 值
	 * @author lingting 2020-06-19 10:27:30
	 */
	default void pushValue(V value, Values values) {
		if (check(value)) {
			forkPush(value, values);
		}
	}

	/**
	 * 插入多个数据
	 * @param values 插入目标
	 * @param iterable 需要插入的多个值
	 * @author lingting 2020-06-19 11:05:00
	 */
	default void pushAll(Iterable<V> iterable, Values values) {
		for (V v : iterable) {
			pushValue(v, values);
		}
	}

	/**
	 * 直接插入数据
	 * @param value 数据
	 * @param values 存放所有数据的对象
	 * @author lingting 2020-06-19 10:25:24
	 */
	void forkPush(V value, Values values);

	/**
	 * 校验 value 是否可以插入
	 * @param value 数据
	 * @return boolean true 可以插入
	 * @author lingting 2020-06-19 10:27:17
	 */
	default boolean check(V value) {
		if (!isInsertNull()) {
			// 不能插入空值，进行校验
			if (value instanceof String && StrUtil.isEmpty((String) value)) {
				// 空值, 结束方法
				return false;
			}
			return value != null;
		}
		return true;
	}

	/**
	 * 是否可以插入空值
	 * @return true 可以插入空值
	 * @author lingting 2020-06-19 10:28:52
	 */
	default boolean isInsertNull() {
		return false;
	}

}
