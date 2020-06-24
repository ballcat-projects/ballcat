package com.hccake.ballcat.common.redis.operation;

import org.aspectj.lang.ProceedingJoinPoint;

import java.util.function.Consumer;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/9/2 15:19
 */
public class CachePutOps extends AbstractCacheOps {

	public CachePutOps(ProceedingJoinPoint joinPoint, Consumer<Object> cachePut) {
		super(joinPoint);
		this.cachePut = cachePut;
	}

	/**
	 * 向缓存写入数据
	 * @return Consumer
	 */
	private Consumer<Object> cachePut;

	public Consumer<Object> cachePut() {
		return cachePut;
	}

}
