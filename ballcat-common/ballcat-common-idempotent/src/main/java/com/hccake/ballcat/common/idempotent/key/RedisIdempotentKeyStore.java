package com.hccake.ballcat.common.idempotent.key;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

/**
 * @author hccake
 */
public class RedisIdempotentKeyStore implements IdempotentKeyStore {

	StringRedisTemplate stringRedisTemplate;

	@Override
	public boolean saveIfAbsent(String key, long duration) {
		ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();
		Boolean saveSuccess = opsForValue.setIfAbsent(key, String.valueOf(System.currentTimeMillis()), duration,
				TimeUnit.SECONDS);
		return saveSuccess != null && saveSuccess;
	}

	@Override
	public void remove(String key) {
		stringRedisTemplate.delete(key);
	}

}
