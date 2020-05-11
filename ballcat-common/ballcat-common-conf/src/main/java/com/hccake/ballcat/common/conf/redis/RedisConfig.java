package com.hccake.ballcat.common.conf.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author Hccake
 * @version 1.0
 * @date 2020/3/27 23:19
 * 使用自定义redisKey前缀，防止Key冲突
 */
@Configuration
@ConditionalOnProperty("redis.global-key-prefix")
public class RedisConfig {

    /**
     * 如果没有配置，则不处理
     */
    @Value("${redis.global-key-prefix:}")
    private String redisKeyPrefix;

    @Bean
    @ConditionalOnMissingBean(StringRedisTemplate.class)
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new PrefixStringRedisSerializer(redisKeyPrefix));
        return template;
    }

    @Bean
    @ConditionalOnMissingBean(RedisTemplate.class)
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new PrefixJdkRedisSerializer(redisKeyPrefix));
        return template;
    }

}