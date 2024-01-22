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

package org.ballcat.common.thread;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.ballcat.common.function.ThrowingRunnable;
import org.slf4j.MDC;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * @author lingting 2022/11/17 20:15
 */
@Slf4j
@Getter
@AllArgsConstructor
@SuppressWarnings("java:S6548")
public class ThreadPool {

	protected static final ThreadPool THREAD_POOL;

	protected static final Integer QUEUE_MAX = 10;

	protected ThreadPoolExecutor pool;

	static {
		THREAD_POOL = new ThreadPool(new ThreadPoolExecutor(
				// 核心线程数大小. 不论是否空闲都存在的线程
				300,
				// 最大线程数 - 1万个
				10000,
				// 存活时间. 非核心线程数如果空闲指定时间. 就回收
				// 存活时间不宜过长. 避免任务量遇到尖峰情况时. 大量空闲线程占用资源
				10,
				// 存活时间的单位
				TimeUnit.SECONDS,
				// 等待任务存放队列 - 队列最大值
				// 这样配置. 当积压任务数量为 队列最大值 时. 会创建新线程来执行任务. 直到线程总数达到 最大线程数
				new LinkedBlockingQueue<>(QUEUE_MAX),
				// 新线程创建工厂 - LinkedBlockingQueue 不支持线程优先级. 所以直接新增线程就可以了
				runnable -> new Thread(null, runnable),
				// 拒绝策略 - 在主线程继续执行.
				new ThreadPoolExecutor.CallerRunsPolicy()));
	}

	public static ThreadPool instance() {
		return THREAD_POOL;
	}

	public static ThreadPool update(ThreadPoolExecutor executor) {
		ThreadPool instance = instance();
		instance.pool = executor;
		return instance;
	}

	/**
	 * 线程池是否运行中
	 */
	public boolean isRunning() {
		ThreadPoolExecutor executor = getPool();
		return !executor.isShutdown() && !executor.isTerminated() && !executor.isTerminating();
	}

	/**
	 * 核心线程数
	 */
	public long getCorePoolSize() {
		return getPool().getCorePoolSize();
	}

	/**
	 * 活跃线程数
	 */
	public long getActiveCount() {
		return getPool().getActiveCount();
	}

	/**
	 * 已执行任务总数
	 */
	public long getTaskCount() {
		return getPool().getTaskCount();
	}

	/**
	 * 允许的最大线程数量
	 */
	public long getMaximumPoolSize() {
		return getPool().getMaximumPoolSize();
	}

	/**
	 * 是否可能触发拒绝策略, 仅为估算
	 */
	public boolean isReject() {
		long activeCount = getActiveCount();
		long size = getMaximumPoolSize();

		// 活跃线程占比未达到 90% 不可能
		long per = activeCount / size;
		if (per <= 90) {
			return false;
		}

		// 占比达到90%的情况下, 剩余可用线程数小于10 则可能触发拒绝
		return size - activeCount < 10;
	}

	public void execute(ThrowingRunnable runnable) {
		execute(null, runnable);
	}

	public void execute(String name, ThrowingRunnable runnable) {
		// 获取当前线程的配置
		Map<String, String> map = MDC.getCopyOfContextMap();
		getPool().execute(() -> {
			Thread thread = Thread.currentThread();
			String oldName = thread.getName();
			if (StringUtils.hasText(name)) {
				thread.setName(name);
			}

			// 存在则填充
			if (!CollectionUtils.isEmpty(map)) {
				MDC.setContextMap(map);
			}

			try {
				runnable.run();
			}
			catch (InterruptedException e) {
				thread.interrupt();
				log.warn("线程池内部线程被中断!");
			}
			catch (Throwable throwable) {
				log.error("线程池内部线程异常!", throwable);
			}
			finally {
				thread.setName(oldName);
				MDC.clear();
			}
		});
	}

	public <T> CompletableFuture<T> async(Supplier<T> supplier) {
		return CompletableFuture.supplyAsync(supplier, this.pool);
	}

	public <T> Future<T> submit(Callable<T> callable) {
		return this.pool.submit(callable);
	}

}
