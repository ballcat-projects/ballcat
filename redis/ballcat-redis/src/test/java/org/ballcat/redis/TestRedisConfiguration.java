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

package org.ballcat.redis;

import org.ballcat.redis.config.CacheProperties;
import org.ballcat.redis.config.CachePropertiesHolder;
import org.ballcat.redis.prefix.IRedisPrefixConverter;
import org.ballcat.redis.prefix.impl.DefaultRedisPrefixConverter;
import org.ballcat.redis.serialize.PrefixStringRedisSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author huyuanzhi 2021/11/16
 */
@Configuration
@EnableConfigurationProperties(CacheProperties.class)
public class TestRedisConfiguration {

	private static final String host = "localhost";

	private static final int port = 6379;

	@Bean
	public CachePropertiesHolder cachePropertiesHolder(CacheProperties cacheProperties) {
		CachePropertiesHolder cachePropertiesHolder = new CachePropertiesHolder();
		cachePropertiesHolder.setCacheProperties(cacheProperties);
		return cachePropertiesHolder;
	}

	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		LettuceConnectionFactory factory = new LettuceConnectionFactory(host, port);
		factory.afterPropertiesSet();
		return factory;
	}

	@Bean
	@ConditionalOnClass(IRedisPrefixConverter.class)
	@ConditionalOnMissingBean
	public StringRedisTemplate stringRedisTemplate(IRedisPrefixConverter redisPrefixConverter,
			RedisConnectionFactory redisConnectionFactory) {
		StringRedisTemplate template = new StringRedisTemplate();
		template.setConnectionFactory(redisConnectionFactory);
		template.setKeySerializer(new PrefixStringRedisSerializer(redisPrefixConverter));
		return template;
	}

	@Bean
	public RedisHelper redisHelper(StringRedisTemplate template) {
		RedisHelper.setRedisTemplate(template);
		return RedisHelper.INSTANCE;
	}

	@Bean
	@ConditionalOnMissingBean(IRedisPrefixConverter.class)
	public IRedisPrefixConverter redisPrefixConverter() {
		return new DefaultRedisPrefixConverter("ballcat");
	}

}
