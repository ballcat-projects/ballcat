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

package org.ballcat.redis.thread;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import javax.validation.constraints.NotNull;

import lombok.extern.slf4j.Slf4j;
import org.ballcat.common.core.thread.AbstractQueueThread;
import org.ballcat.common.util.JsonUtils;
import org.ballcat.redis.RedisHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

/**
 * @see java.util.concurrent.LinkedBlockingDeque
 * @author lingting 2021/3/2 21:09
 */
@Slf4j
public abstract class AbstractRedisThread<E> extends AbstractQueueThread<E> {

	@Autowired
	@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
	protected RedisHelper redisHelper;

	/**
	 * 是否正在运行
	 */
	protected boolean run = true;

	/**
	 * 锁
	 */
	protected final ReentrantLock lock = new ReentrantLock();

	/**
	 * 激活与休眠线程
	 */
	protected final Condition condition = this.lock.newCondition();

	/**
	 * 获取数据存储的key
	 * @return java.lang.String
	 */
	public abstract String getKey();

	/**
	 * 对象 转换成 string. 把String 存入redis
	 * @param e 对象
	 * @return java.lang.String
	 */
	protected String convertToString(@NotNull E e) {
		return JsonUtils.toJson(e);
	}

	/**
	 * 获取目标对象的type , 即 E 的实际类型. 如果获取失败, 请重写此方法
	 * @return java.lang.reflect.Type
	 */
	protected Type getObjType() {
		return ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	/**
	 * string 转换成 对象
	 * @param str string
	 * @return java.lang.String
	 */
	@Nullable
	protected E convertToObj(String str) {
		if (!StringUtils.hasText(str)) {
			return null;
		}
		return JsonUtils.toObj(str, getObjType());
	}

	@Override
	public void put(E e) {
		// 不插入空值
		if (e != null) {
			try {
				this.lock.lockInterruptibly();
				try {
					// 线程被中断后无法执行Redis命令
					RedisHelper.rPush(getKey(), convertToString(e));
					// 激活线程
					this.condition.signal();
				}
				finally {
					this.lock.unlock();
				}
			}
			catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
			catch (Exception ex) {
				log.error("{} put error, param: {}", this.getClass().toString(), e, ex);
			}
		}
	}

	/**
	 * 从redis中获取数据
	 * <p>
	 * 忽略sonar 警告. 子类有可能需要在get时做其他操作.
	 * @return java.lang.String
	 */
	@SuppressWarnings("java:S2177")
	protected String get() {
		return RedisHelper.lPop(getKey());
	}

	@Override
	@Nullable
	public E poll(long time) throws InterruptedException {
		if (!isRun()) {
			// 停止运行时返回null, 让数据待在redis里面
			return null;
		}
		// 上锁
		this.lock.lockInterruptibly();
		try {
			// 设置等待时长
			long nanos = TimeUnit.MILLISECONDS.toNanos(time);
			String pop;
			do {
				// 获取数据
				pop = get();
				if (StringUtils.hasText(pop)) {
					break;
				}

				// 休眠. 返回剩余的休眠时间
				nanos = this.condition.awaitNanos(nanos);
			}
			while (isRun() && nanos > 0);

			return convertToObj(pop);
		}
		finally {
			this.lock.unlock();
		}

	}

	@Override
	protected void shutdown(List<E> list) {
		// 修改运行标志
		this.run = false;
		for (E e : list) {
			// 所有数据插入redis
			put(e);
			log.error("{}", e);
		}
	}

	@Override
	public boolean isRun() {
		// 运行中 且 未被中断
		return this.run && !isInterrupted();
	}

}
