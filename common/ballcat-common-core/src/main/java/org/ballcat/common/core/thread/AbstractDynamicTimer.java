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

import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.ballcat.common.lock.JavaReentrantLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lingting 2023-04-22 10:39
 */
@SuppressWarnings("java:S1066")
public abstract class AbstractDynamicTimer<T> extends Thread {

	protected final Logger log = LoggerFactory.getLogger(getClass());

	private final JavaReentrantLock lock = new JavaReentrantLock();

	protected final PriorityBlockingQueue<T> queue = new PriorityBlockingQueue<>(defaultCapacity(), comparator());

	public abstract Comparator<T> comparator();

	/**
	 * 默认大小
	 */
	protected int defaultCapacity() {
		return 11;
	}

	/**
	 * 还有多久要处理该对象
	 * @param t 对象
	 * @return 具体处理该对象还要多久, 单位: 毫秒
	 */
	protected abstract long sleepTime(T t);

	protected void init() {
	}

	public boolean isRun() {
		return !isInterrupted() && isAlive();
	}

	public void put(T t) {
		if (t == null) {
			return;
		}

		try {
			this.lock.runByInterruptibly(() -> {
				this.queue.add(t);
				this.lock.signalAll();
			});
		}
		catch (InterruptedException e) {
			interrupt();
		}
		catch (Exception e) {
			this.log.error("{} put error, param: {}", this.getClass(), t, e);
		}
	}

	/**
	 * 将取出的元素重新放入队列
	 */
	protected void replay(T t) {
		put(t);
	}

	@Override
	public void run() {
		init();
		while (isRun()) {
			try {
				T t = pool();
				this.lock.runByInterruptibly(() -> {
					if (t == null) {
						this.lock.await(24, TimeUnit.HOURS);
						return;
					}

					long sleepTime = sleepTime(t);
					// 需要休眠
					if (sleepTime > 0) {
						// 如果是被唤醒
						if (this.lock.await(sleepTime, TimeUnit.MILLISECONDS)) {
							replay(t);
							return;
						}
					}

					process(t);
				});

			}
			catch (InterruptedException e) {
				interrupt();
				shutdown();
			}
			catch (Exception e) {
				error(e);
			}
		}
	}

	protected T pool() {
		return this.queue.poll();
	}

	protected abstract void process(T t);

	protected void error(Exception e) {
		this.log.error("类: {}; 线程: {}; 运行异常! ", getClass().getName(), getId(), e);
	}

	protected void shutdown() {
		this.log.warn("类: {}; 线程: {}; 被中断! 剩余数据: {}", getClass().getName(), getId(), this.queue.size());
	}

}
