package com.hccake.common.i18n.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * redis 缓存
 *
 * @author Yakir
 */
@RequiredArgsConstructor
public class RedisCacheService implements CacheService {

	private final StringRedisTemplate redisTemplate;

	@Override
	public void put(String cacheKey, String cacheValue) {
		redisTemplate.opsForValue().set(cacheKey, cacheValue);
	}

	@Override
	public void put(String cacheKey, String cacheValue, Long expireTime) {
		if (expireTime <= 0) {
			this.put(cacheKey, cacheValue);
		}
		else {
			redisTemplate.opsForValue().set(cacheKey, cacheValue, expireTime, TimeUnit.SECONDS);
		}
	}

	@Override
	public String get(String cacheKey) {
		return redisTemplate.opsForValue().get(cacheKey);
	}

	@Override
	public void del(String cacheKey) {
		redisTemplate.delete(cacheKey);
	}

}
