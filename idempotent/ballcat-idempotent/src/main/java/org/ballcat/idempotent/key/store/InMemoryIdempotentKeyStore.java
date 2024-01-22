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

package org.ballcat.idempotent.key.store;

import java.util.concurrent.TimeUnit;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;

/**
 * 基于内存的幂等Key存储组件
 *
 * TODO: (by evil0th) 2023/6/6 JDK11+后采用Caffeine重构，移除hutool
 *
 * @author hccake
 */
public class InMemoryIdempotentKeyStore implements IdempotentKeyStore {

	private final TimedCache<String, Long> cache;

	public InMemoryIdempotentKeyStore() {
		this.cache = CacheUtil.newTimedCache(Integer.MAX_VALUE);
		this.cache.schedulePrune(1);
	}

	@Override
	public synchronized boolean saveIfAbsent(String key, long duration, TimeUnit timeUnit) {
		Long value = this.cache.get(key, false);
		if (value == null) {
			long timeOut = TimeUnit.MILLISECONDS.convert(duration, timeUnit);
			this.cache.put(key, System.currentTimeMillis(), timeOut);
			return true;
		}
		return false;
	}

	@Override
	public void remove(String key) {
		this.cache.remove(key);
	}

}
