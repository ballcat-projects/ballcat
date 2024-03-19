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

package org.ballcat.common.value.step;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.SneakyThrows;
import org.ballcat.common.value.StepValue;

/**
 * @author lingting 2024-01-23 15:30
 */
public class IteratorStepValue<T> extends AbstractConcurrentStepValue<T> {

	private final Map<BigInteger, T> map;

	private final Iterator<T> iterator;

	public IteratorStepValue(Iterator<T> iterator) {
		this(new ConcurrentHashMap<>(), iterator);
	}

	protected IteratorStepValue(Map<BigInteger, T> map, Iterator<T> iterator) {
		this.map = map;
		this.iterator = iterator;
	}

	@Override
	public StepValue<T> copy() {
		return new IteratorStepValue<>(this.map, this.iterator);
	}

	@Override
	public boolean doHasNext() {
		// 下一个已经取出来了, 为true
		if (this.map.containsKey(this.index.add(BigInteger.ONE))) {
			return true;
		}
		// 没取出来, 用下一个值
		return this.iterator.hasNext();
	}

	@Override
	public T doNext() {
		return this.map.computeIfAbsent(increasing(), i -> this.iterator.next());
	}

	@Override
	public T doCalculate(BigInteger index) {
		if (this.map.containsKey(index)) {
			return this.map.get(index);
		}

		// 如果迭代器已经取空了
		if (!this.iterator.hasNext()) {
			return null;
		}

		// 没取空, 接着取
		StepValue<T> value = copy();
		while (value.hasNext()) {
			T t = value.next();
			if (index.compareTo(value.index()) == 0) {
				return t;
			}
		}
		return null;
	}

	/**
	 * 移除上一个next返回的元素
	 */
	@Override
	@SneakyThrows
	public void remove() {
		this.lock.runByInterruptibly(() -> {
			// 至少需要调用一次next
			if (this.index.compareTo(BigInteger.ZERO) == 0) {
				throw new IllegalStateException();
			}
			// 被移除的索引
			BigInteger removeIndex = index;
			// 移除索引位置的值
			this.map.remove(removeIndex);
			// 索引回调
			this.index = removeIndex.subtract(BigInteger.ONE);
			// values缓存移除
			this.values = null;
			// 用于重设后续索引
			BigInteger current = removeIndex;
			while (true) {
				// 下一个索引
				BigInteger next = current.add(BigInteger.ONE);
				// 从缓存中移除下一个
				T value = this.map.remove(next);
				// 不存在后续索引则结束
				if (value == null) {
					break;
				}
				// 存在值, 放入到上一个索引中
				this.map.put(current, value);
				// 步进, 用于处理下一个
				current = next;
			}

		});
	}

}
