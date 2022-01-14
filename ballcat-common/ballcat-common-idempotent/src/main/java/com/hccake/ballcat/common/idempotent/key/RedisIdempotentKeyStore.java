package com.hccake.ballcat.common.idempotent.key;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

/**
 * 基于 Redis 的幂等Key的存储器
 *
 * @author hccake
 */
@RequiredArgsConstructor
public class RedisIdempotentKeyStore implements IdempotentKeyStore {

	private final StringRedisTemplate stringRedisTemplate;

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
