package com.hccake.ballcat.common.redis.core;

import com.hccake.ballcat.common.redis.config.CachePropertiesHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author Hccake
 * @version 1.0
 * @date 2020/3/27 21:15
 * 缓存锁的操作类
 */
public class CacheLock {
    private static final Logger log = LoggerFactory.getLogger(CacheLock.class);
    private static StringRedisTemplate redisTemplate;

	public void setStringRedisTemplate(StringRedisTemplate redisTemplate) {
		CacheLock.redisTemplate = redisTemplate;
	}


    /**
     * 上锁
     * @param requestId 请求id
     * @return Boolean 是否成功获得锁
     */
    public static Boolean lock(String lockKey, String requestId) {
        log.info("lock: {key:{}, clientId:{}}", lockKey, requestId);
        return redisTemplate.opsForValue()
                .setIfAbsent(lockKey, requestId, CachePropertiesHolder.lockedTimeOut(), TimeUnit.SECONDS);
    }


    /**
     *
     * 释放锁lua脚本
     *  KEYS【1】：key值是为要加的锁定义的字符串常量
     *  ARGV【1】：value值是 request id, 用来防止解除了不该解除的锁. 可用 UUID
     */
    private static final String RELEASE_LOCK_LUA_SCRIPT = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
    /**
     * 释放锁成功返回值
     */
    private static final Long RELEASE_LOCK_SUCCESS_RESULT = 1L;

    /**
     * 释放锁
     *
     * @param key      锁ID
     * @param requestId 请求ID
     * @return 是否成功
     */
    public static boolean releaseLock(String key, String requestId) {
        log.info("release lock: {key:{}, clientId:{}}", key, requestId);
        //指定ReturnType为Long.class
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(RELEASE_LOCK_LUA_SCRIPT, Long.class);
        Long result = redisTemplate.execute(redisScript, Collections.singletonList(key), requestId);
        return Objects.equals(result, RELEASE_LOCK_SUCCESS_RESULT);
    }



}
