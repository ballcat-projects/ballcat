package com.hccake.common.i18n.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * local cache
 *
 * @author Yakir
 */
public class LocalCacheService implements CacheService {

	/**
	 * 本地缓存 map
	 */
	private static final Map<String, String> CACHE_MAP = new HashMap<>();

	@Override
	public void put(String cacheKey, String cacheValue) {
		put(cacheKey, cacheValue, null);
	}

	@Override
	public void put(String cacheKey, String cacheValue, Long expireTime) {
		CACHE_MAP.put(cacheKey, cacheValue);
	}

	@Override
	public String get(String cacheKey) {
		return CACHE_MAP.get(cacheKey);
	}

	@Override
	public void del(String cacheKey) {
		CACHE_MAP.remove(cacheKey);
	}

}
