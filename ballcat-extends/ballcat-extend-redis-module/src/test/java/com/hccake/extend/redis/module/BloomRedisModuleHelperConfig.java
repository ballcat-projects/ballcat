package com.hccake.extend.redis.module;

import com.hccake.ballcat.common.redis.prefix.IRedisPrefixConverter;
import com.hccake.ballcat.common.redis.serialize.PrefixStringRedisSerializer;
import com.hccake.extend.redis.moudle.bloom.BloomRedisModuleHelper;
import lombok.RequiredArgsConstructor;
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
		BloomRedisModuleHelper bloomRedisModuleHelper = new BloomRedisModuleHelper(lettuceConnectionFactory);
		// 可选操作，配合 ballcat-spring-boot-starter-redis 的 key 前缀使用
		bloomRedisModuleHelper.setKeySerializer(new PrefixStringRedisSerializer(redisPrefixConverter));
		return bloomRedisModuleHelper;
	}

}