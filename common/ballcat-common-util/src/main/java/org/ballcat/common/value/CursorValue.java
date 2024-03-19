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

package org.ballcat.common.value;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import lombok.Getter;
import org.slf4j.Logger;
import org.springframework.util.CollectionUtils;

/**
 * @author lingting 2023-12-29 11:30
 */
public abstract class CursorValue<T> implements Iterator<T> {

	protected final Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

	protected final List<T> current = new ArrayList<>();

	/**
	 * 是否已经无数据了, 如果为true, 则不会继续调用 {@link #nextBatchData()}方法, 且
	 * {@link #hasNext()}方法永远返回false
	 */
	protected boolean empty = false;

	/**
	 * 已读取数据数量
	 */
	@Getter
	protected long count = 0;

	@Override
	public boolean hasNext() {
		if (!CollectionUtils.isEmpty(this.current)) {
			return true;
		}

		if (this.empty) {
			return false;
		}

		List<T> list = nextBatchData();

		if (CollectionUtils.isEmpty(list)) {
			this.empty = true;
			return false;
		}

		this.current.addAll(list);
		return true;
	}

	@Override
	public T next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}
		this.count++;
		return this.current.remove(0);
	}

	/**
	 * 获取下一批数据
	 */
	protected abstract List<T> nextBatchData();

	public Stream<T> stream() {
		Spliterator<T> spliterator = Spliterators.spliteratorUnknownSize(this, Spliterator.ORDERED);
		return StreamSupport.stream(spliterator, false);
	}

}
