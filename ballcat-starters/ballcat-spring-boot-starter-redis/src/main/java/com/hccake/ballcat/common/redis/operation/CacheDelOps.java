package com.hccake.ballcat.common.redis.operation;

import com.hccake.ballcat.common.redis.operation.function.VoidMethod;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/9/2 15:19
 */
public class CacheDelOps extends AbstractCacheOps {

	/**
	 * 删除缓存数据
	 * @return VoidMethod
	 */
	private VoidMethod cacheDel;

	public CacheDelOps(ProceedingJoinPoint joinPoint, VoidMethod cacheDel) {
		super(joinPoint);
		this.cacheDel = cacheDel;
	}

	public VoidMethod cacheDel() {
		return cacheDel;
	}

}
