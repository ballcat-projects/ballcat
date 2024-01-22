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

import java.util.Collection;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 等待队列
 *
 * @author lingting 2023/1/29 10:52
 */
public class WaitQueue<V> {

	private final LinkedBlockingQueue<V> queue;

	public WaitQueue() {
		this(new LinkedBlockingQueue<>());
	}

	public WaitQueue(LinkedBlockingQueue<V> queue) {
		this.queue = queue;
	}

	public V get() {
		return this.queue.poll();
	}

	public V poll() throws InterruptedException {
		return poll(10, TimeUnit.HOURS);
	}

	public V poll(long timeout, TimeUnit unit) throws InterruptedException {
		V v;
		do {
			v = this.queue.poll(timeout, unit);
		}
		while (v == null);
		return v;
	}

	public void clear() {
		this.queue.clear();
	}

	public void add(V seat) {
		this.queue.add(seat);
	}

	public void addAll(Collection<V> accounts) {
		for (V account : accounts) {
			add(account);
		}
	}

}
