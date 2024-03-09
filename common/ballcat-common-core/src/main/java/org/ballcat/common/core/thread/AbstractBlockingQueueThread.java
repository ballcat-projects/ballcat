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

package org.ballcat.common.core.thread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 抽象的线程类，主要用于汇聚详情数据 做一些基础的处理后 进行批量插入
 *
 * @author lingting
 */
public abstract class AbstractBlockingQueueThread<T> extends AbstractQueueThread<T> {

	private final BlockingQueue<T> queue = new LinkedBlockingQueue<>();

	@Override
	public void put(T t) {
		if (t != null) {
			try {
				this.queue.put(t);
			}
			catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
			catch (Exception e) {
				this.log.error("{} put Object error, param: {}", this.getClass().toString(), t, e);
			}
		}
	}

	@Override
	protected T poll(long time) throws InterruptedException {
		return this.queue.poll(time, TimeUnit.MILLISECONDS);
	}

}
