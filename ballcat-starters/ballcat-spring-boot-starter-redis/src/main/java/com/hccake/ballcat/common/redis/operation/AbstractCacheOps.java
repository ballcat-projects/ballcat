package com.hccake.ballcat.common.redis.operation;

import com.hccake.ballcat.common.redis.config.CachePropertiesHolder;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/9/2 15:19
 */
public abstract class AbstractCacheOps {

	protected AbstractCacheOps(ProceedingJoinPoint joinPoint) {
		this.joinPoint = joinPoint;
	}

	private final ProceedingJoinPoint joinPoint;

	/**
	 * 织入方法
	 * @return ProceedingJoinPoint
	 */
	public ProceedingJoinPoint joinPoint() {
		return joinPoint;
	}

	/**
	 * 检查缓存数据是否是空值
	 * @param cacheData
	 * @return
	 */
	public boolean nullValue(Object cacheData) {
		return CachePropertiesHolder.nullValue().equals(cacheData);
	}

}
