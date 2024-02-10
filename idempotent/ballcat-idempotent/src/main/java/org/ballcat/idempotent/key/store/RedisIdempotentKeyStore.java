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

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

/**
 * 基于 Redis 的幂等Key的存储器
 *
 * @author hccake
 */
@RequiredArgsConstructor
public class RedisIdempotentKeyStore implements IdempotentKeyStore {

	@Autowired
	@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
	private StringRedisTemplate stringRedisTemplate;

	@Override
	public boolean saveIfAbsent(String key, long duration, TimeUnit timeUnit) {
		ValueOperations<String, String> opsForValue = this.stringRedisTemplate.opsForValue();
		Boolean saveSuccess = opsForValue.setIfAbsent(key, String.valueOf(System.currentTimeMillis()), duration,
				timeUnit);
		return saveSuccess != null && saveSuccess;
	}

	@Override
	public void remove(String key) {
		this.stringRedisTemplate.delete(key);
	}

}
