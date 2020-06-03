package com.hccake.ballcat.common.redis.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hccake.ballcat.common.redis.core.CacheLock;
import com.hccake.ballcat.common.redis.serialize.CacheSerializer;
import com.hccake.ballcat.common.redis.serialize.JacksonSerializer;
import com.hccake.ballcat.common.redis.serialize.PrefixJdkRedisSerializer;
import com.hccake.ballcat.common.redis.serialize.PrefixStringRedisSerializer;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/9/2 14:13
 * 指定扫描包
 */
@RequiredArgsConstructor
@EnableConfigurationProperties(CacheProperties.class)
public class RedisAutoConfiguration {
	private final RedisConnectionFactory redisConnectionFactory;


    /**
     * 初始化配置类
     * @return GlobalCacheProperties
     */
    @Bean
    public CachePropertiesHolder cachePropertiesHolder(CacheProperties cacheProperties){
		CachePropertiesHolder cachePropertiesHolder = new CachePropertiesHolder();
		cachePropertiesHolder.setCacheProperties(cacheProperties);
		return cachePropertiesHolder;
    }

	/**
	 * 默认使用 Jackson 序列化
	 * @param objectMapper objectMapper
	 * @return JacksonSerializer
	 */
	@Bean
    public CacheSerializer cacheSerializer(ObjectMapper objectMapper){
        return new JacksonSerializer(objectMapper);
    }

	/**
	 * 初始化CacheLock
	 * @param stringRedisTemplate 默认使用字符串类型操作，后续扩展
	 * @return CacheLock
	 */
	@Bean
	public CacheLock cacheLock(StringRedisTemplate stringRedisTemplate){
		CacheLock cacheLock = new CacheLock();
		cacheLock.setStringRedisTemplate(stringRedisTemplate);
		return cacheLock;
	}


	@Bean
	@DependsOn("cachePropertiesHolder")
	@ConditionalOnProperty(name = "ballcat.redis.key-prefix")
	@ConditionalOnMissingBean
	public StringRedisTemplate stringRedisTemplate() {
		StringRedisTemplate template = new StringRedisTemplate();
		template.setConnectionFactory(redisConnectionFactory);
		template.setKeySerializer(new PrefixStringRedisSerializer(CachePropertiesHolder.keyPrefix()));
		return template;
	}

	@Bean
	@DependsOn("cachePropertiesHolder")
	@ConditionalOnProperty(name = "ballcat.redis.key-prefix")
	@ConditionalOnMissingBean(name = "redisTemplate")
	public RedisTemplate<Object, Object> redisTemplate() {
		RedisTemplate<Object, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(redisConnectionFactory);
		template.setKeySerializer(new PrefixJdkRedisSerializer(CachePropertiesHolder.keyPrefix()));
		return template;
	}



}
