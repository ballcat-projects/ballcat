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
