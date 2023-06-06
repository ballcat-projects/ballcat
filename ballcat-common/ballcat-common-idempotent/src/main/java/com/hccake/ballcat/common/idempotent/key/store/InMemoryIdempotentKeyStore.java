/*
 * Copyright 2023 the original author or authors.
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
package com.hccake.ballcat.common.idempotent.key.store;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;

import java.util.concurrent.TimeUnit;

/**
 * 基于内存的幂等Key存储组件
 *
 * @author hccake
 */
public class InMemoryIdempotentKeyStore implements IdempotentKeyStore {

	private final TimedCache<String, Long> cache;

	public InMemoryIdempotentKeyStore() {
		this.cache = CacheUtil.newTimedCache(Integer.MAX_VALUE);
		cache.schedulePrune(1);
	}

	@Override
	public synchronized boolean saveIfAbsent(String key, long duration, TimeUnit timeUnit) {
		Long value = cache.get(key, false);
		if (value == null) {
			long timeOut = TimeUnit.MILLISECONDS.convert(duration, timeUnit);
			cache.put(key, System.currentTimeMillis(), timeOut);
			return true;
		}
		return false;
	}

	@Override
	public void remove(String key) {
		cache.remove(key);
	}

}
