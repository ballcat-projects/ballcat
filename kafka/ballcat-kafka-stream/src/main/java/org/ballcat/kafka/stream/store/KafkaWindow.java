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
	 */
	void forkPush(V value, Values values);

	/**
	 * 校验 value 是否可以插入
	 * @param value 数据
	 * @return boolean true 可以插入
	 */
	default boolean check(V value) {
		if (!isInsertNull()) {
			// 不能插入空值，进行校验
			if (null == value) {
				return false;
			}
			if (value instanceof String) {
				// 空值, 结束方法
				return ((String) value).length() > 0;
			}
			return true;
		}
		return true;
	}

	/**
	 * 是否可以插入空值
	 * @return true 可以插入空值
	 */
	default boolean isInsertNull() {
		return false;
	}

}
