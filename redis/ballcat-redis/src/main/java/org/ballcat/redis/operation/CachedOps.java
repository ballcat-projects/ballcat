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

package org.ballcat.redis.operation;

import java.lang.reflect.Type;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * @author Hccake 2019/9/2 15:19
 */
public class CachedOps extends AbstractCacheOps {

	/**
	 * 数据类型
	 */
	private final Type returnType;

	/**
	 * 缓存分布式锁的key
	 */
	private final String lockKey;

	/**
	 * 从Redis中获取缓存数据的操作
	 */
	private final Supplier<String> cacheQuery;

	/**
	 * 向缓存写入数据
	 */
	private final Consumer<Object> cachePut;

	/**
	 * 在Redis中锁竞争失败时的重试次数
	 */
	private final int retryCount;

	/**
	 * 基本构造函数
	 * @param joinPoint 织入方法
	 * @param lockKey 分布式锁key
	 * @param cacheQuery 查询缓存函数
	 * @param cachePut 更新缓存函数
	 * @param returnType 返回数据类型
	 * @param retryCount 锁竞争失败时的重试次数
	 */
	public CachedOps(ProceedingJoinPoint joinPoint, String lockKey, Supplier<String> cacheQuery,
			Consumer<Object> cachePut, Type returnType, int retryCount) {
		super(joinPoint);
		this.lockKey = lockKey;
		this.cacheQuery = cacheQuery;
		this.cachePut = cachePut;
		this.returnType = returnType;
		this.retryCount = retryCount;
	}

	public Supplier<String> cacheQuery() {
		return this.cacheQuery;
	}

	public Consumer<Object> cachePut() {
		return this.cachePut;
	}

	public Type returnType() {
		return this.returnType;
	}

	public String lockKey() {
		return this.lockKey;
	}

	public int retryCount() {
		return this.retryCount;
	}

}
