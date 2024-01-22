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

package org.ballcat.redis.module;

import lombok.RequiredArgsConstructor;
import org.ballcat.redis.moudle.bloom.BloomRedisModuleHelper;
import org.ballcat.redis.prefix.IRedisPrefixConverter;
import org.ballcat.redis.serialize.PrefixStringRedisSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class BloomRedisModuleHelperConfig {

	private final LettuceConnectionFactory lettuceConnectionFactory;

	@Bean
	@DependsOn("cachePropertiesHolder") // 防止 CachePropertiesHolder 初始化落后导致的空指针
	public BloomRedisModuleHelper bloomRedisModuleHelper(IRedisPrefixConverter redisPrefixConverter) {
		BloomRedisModuleHelper bloomRedisModuleHelper = new BloomRedisModuleHelper(this.lettuceConnectionFactory);
		// 可选操作，配合 ballcat-spring-boot-starter-redis 的 key 前缀使用
		bloomRedisModuleHelper.setKeySerializer(new PrefixStringRedisSerializer(redisPrefixConverter));
		return bloomRedisModuleHelper;
	}

}
