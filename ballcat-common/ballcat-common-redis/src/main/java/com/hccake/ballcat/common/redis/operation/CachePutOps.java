package com.hccake.ballcat.common.redis.operation;

import org.aspectj.lang.ProceedingJoinPoint;

import java.util.function.Consumer;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/9/2 15:19
 */
public class CachePutOps extends AbstractCacheOps {

	/**
	 * 向缓存写入数据
	 */
	private final Consumer<Object> cachePut;

	public CachePutOps(ProceedingJoinPoint joinPoint, Consumer<Object> cachePut) {
		super(joinPoint);
		this.cachePut = cachePut;
	}

	public Consumer<Object> cachePut() {
		return cachePut;
	}

}
