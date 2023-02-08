package com.hccake.ballcat.common.redis.lock;

import com.hccake.ballcat.common.redis.RedisHelper;
import com.hccake.ballcat.common.redis.config.CachePropertiesHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 缓存锁的操作类
 *
 * @author Hccake 2020/3/27 21:15
 */
@Slf4j
public final class CacheLock {

	private CacheLock() {
	}

	/**
	 * 释放锁lua脚本 KEYS【1】：key值是为要加的锁定义的字符串常量 ARGV【1】：value值是 request id, 用来防止解除了不该解除的锁. 可用
	 * UUID
	 */
	private static final DefaultRedisScript<Long> RELEASE_LOCK_LUA_SCRIPT = new DefaultRedisScript<>(
			"if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end",
			Long.class);

	/**
	 * 释放锁成功返回值
	 */
	private static final Long RELEASE_LOCK_SUCCESS_RESULT = 1L;

	/**
	 * 上锁
	 * @param lockKey 锁定标记
	 * @param requestId 请求id
	 * @return Boolean 是否成功获得锁
	 */
	public static Boolean lock(String lockKey, String requestId) {
		return lock(lockKey, requestId, CachePropertiesHolder.defaultLockTimeout(), TimeUnit.SECONDS);
	}

	/**
	 * 上锁
	 * @param lockKey 锁定标记
	 * @param requestId 请求id
	 * @param timeout 锁超时时间,单位秒
	 * @return Boolean 是否成功获得锁
	 */
	public static Boolean lock(String lockKey, String requestId, long timeout) {
		return lock(lockKey, requestId, timeout, TimeUnit.SECONDS);
	}

	/**
	 * 上锁
	 * @param lockKey 锁定标记
	 * @param requestId 请求id
	 * @param timeout 锁超时时间
	 * @param timeUnit 时间过期单位
	 * @return Boolean 是否成功获得锁
	 */
	public static Boolean lock(String lockKey, String requestId, long timeout, TimeUnit timeUnit) {
		if (log.isTraceEnabled()) {
			log.trace("lock: {key:{}, clientId:{}}", lockKey, requestId);
		}
		return RedisHelper.setNxEx(lockKey, requestId, timeout, timeUnit);
	}

	/**
	 * 释放锁
	 * @param key 锁ID
	 * @param requestId 请求ID
	 * @return 是否成功
	 */
	public static boolean releaseLock(String key, String requestId) {
		if (log.isTraceEnabled()) {
			log.trace("release lock: {key:{}, clientId:{}}", key, requestId);
		}
		Long result = RedisHelper.execute(RELEASE_LOCK_LUA_SCRIPT, Collections.singletonList(key), requestId);
		return Objects.equals(result, RELEASE_LOCK_SUCCESS_RESULT);
	}

}
