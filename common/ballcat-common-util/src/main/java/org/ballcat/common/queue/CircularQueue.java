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

package org.ballcat.common.queue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.ballcat.common.lock.JavaReentrantLock;
import org.springframework.util.CollectionUtils;

/**
 * 循环队列
 *
 * @author lingting 2023-05-30 10:25
 */
public class CircularQueue<T> {

	private final JavaReentrantLock lock = new JavaReentrantLock();

	private final List<T> source = new ArrayList<>();

	private Iterator<T> iterator;

	public CircularQueue<T> add(T t) throws InterruptedException {
		this.lock.runByInterruptibly(() -> this.source.add(t));
		return this;
	}

	public CircularQueue<T> addAll(Collection<T> collection) throws InterruptedException {
		this.lock.runByInterruptibly(() -> this.source.addAll(collection));
		return this;
	}

	public T pool() throws InterruptedException {
		return this.lock.getByInterruptibly(() -> {
			if (CollectionUtils.isEmpty(this.source)) {
				return null;
			}
			if (this.iterator == null || !this.iterator.hasNext()) {
				this.iterator = this.source.iterator();
			}

			return this.iterator.next();
		});
	}

}
