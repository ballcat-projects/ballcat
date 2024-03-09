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

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

/**
 * 顶级队列线程类
 *
 * @author lingting 2021/3/2 15:07
 */
public abstract class AbstractQueueThread<E> extends Thread {

	protected final Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * 默认缓存数据数量
	 */
	private static final int DEFAULT_BATCH_SIZE = 500;

	/**
	 * 默认等待时长 30秒；单位 毫秒
	 */
	private static final long DEFAULT_BATCH_TIMEOUT_MS = 30 * 1000L;

	/**
	 * 默认获取数据时的超时时间
	 */
	private static final long POLL_TIMEOUT_MS = 5 * 1000L;

	/**
	 * 用于子类自定义缓存数据数量
	 * @return long
	 */
	public int getBatchSize() {
		return DEFAULT_BATCH_SIZE;
	}

	/**
	 * 用于子类自定义等待时长
	 * @return 返回时长，单位毫秒
	 */
	public long getBatchTimeout() {
		return DEFAULT_BATCH_TIMEOUT_MS;
	}

	/**
	 * 用于子类自定义 获取数据的超时时间
	 * @return 返回时长，单位毫秒
	 */
	public long getPollTimeout() {
		return POLL_TIMEOUT_MS;
	}

	public boolean isRun() {
		return !isInterrupted() && isAlive();
	}

	protected void init() {
	}

	/**
	 * 往队列插入数据
	 * @param e 数据
	 */
	public abstract void put(E e);

	/**
	 * 数据处理前执行
	 */
	protected void preProcess() {
	}

	/**
	 * 从队列中取值
	 * @param time 等待时长, 单位 毫秒
	 * @return E
	 * @throws InterruptedException 线程中断
	 */
	protected abstract E poll(long time) throws InterruptedException;

	/**
	 * 处理接收的数据
	 * @param list 当前所有数据
	 * @param e 接收的数据
	 */
	protected void receiveProcess(List<E> list, E e) {
		list.add(e);
	}

	/**
	 * 处理所有已接收的数据
	 * @param list 所有已接收的数据
	 * @throws Exception 异常
	 */
	@SuppressWarnings("java:S112")
	protected abstract void process(List<E> list) throws Exception;

	@Override
	@SuppressWarnings("java:S1181")
	public void run() {
		init();
		List<E> list;
		while (isRun()) {
			list = new ArrayList<>(getBatchSize());

			try {
				preProcess();
				fillList(list);

				if (!isRun()) {
					shutdown(list);
				}
				else {
					process(list);
				}
			}
			catch (InterruptedException e) {
				shutdown(list);
				Thread.currentThread().interrupt();
			}
			catch (Exception e) {
				error(e, list);
			}
			// Throwable 异常直接结束. 这里捕获用来保留信息. 方便排查问题
			catch (Throwable throwable) {
				this.log.error("线程队列运行异常!", throwable);
				throw throwable;
			}
		}
	}

	protected void fillList(List<E> list) {
		long timestamp = 0;
		int count = 0;

		while (count < getBatchSize()) {
			E e = poll();

			if (e != null) {
				// 第一次插入数据
				if (count++ == 0) {
					// 记录时间
					timestamp = System.currentTimeMillis();
				}
				receiveProcess(list, e);
			}

			// 无法继续运行
			final boolean isBreak = !isRun()
					// 或者 已有数据且超过设定的等待时间
					|| (!CollectionUtils.isEmpty(list) && System.currentTimeMillis() - timestamp >= getBatchTimeout());
			if (isBreak) {
				break;
			}
		}
	}

	public E poll() {
		E e = null;
		try {
			e = poll(getPollTimeout());
		}
		catch (InterruptedException ex) {
			this.log.error("{} 类的poll线程被中断!id: {}", getClass().getSimpleName(), getId());
			interrupt();
		}
		return e;
	}

	/**
	 * 发生异常时处理异常
	 * @param e 异常
	 * @param list 当时的数据
	 */
	protected abstract void error(Throwable e, List<E> list);

	/**
	 * 线程被中断后的处理. 如果有缓存手段可以让数据进入缓存.
	 * @param list 当前数据
	 */
	protected void shutdown(List<E> list) {
		this.log.warn("{} 线程: {} 被关闭. 数据:{}", this.getClass().getSimpleName(), getId(), list);
	}

}
