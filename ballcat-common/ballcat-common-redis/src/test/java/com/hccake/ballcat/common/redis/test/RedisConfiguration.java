package com.hccake.ballcat.common.redis.test;

import com.hccake.ballcat.common.redis.config.CacheProperties;
import com.hccake.ballcat.common.redis.config.CachePropertiesHolder;
import com.hccake.ballcat.common.redis.core.CacheLock;
import com.hccake.ballcat.common.redis.prefix.IRedisPrefixConverter;
import com.hccake.ballcat.common.redis.prefix.impl.DefaultRedisPrefixConverter;
import com.hccake.ballcat.common.redis.serialize.PrefixStringRedisSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author huyuanzhi
 * @version 1.0
 * @date 2021/11/16
 */
@Configuration
@EnableConfigurationProperties(CacheProperties.class)
public class RedisConfiguration {

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
	public CacheLock cacheLock(StringRedisTemplate stringRedisTemplate) {
		CacheLock cacheLock = new CacheLock();
		cacheLock.setStringRedisTemplate(stringRedisTemplate);
		return cacheLock;
	}

	@Bean
	@ConditionalOnMissingBean(IRedisPrefixConverter.class)
	public IRedisPrefixConverter redisPrefixConverter() {
		return new DefaultRedisPrefixConverter("ballcat");
	}

}
