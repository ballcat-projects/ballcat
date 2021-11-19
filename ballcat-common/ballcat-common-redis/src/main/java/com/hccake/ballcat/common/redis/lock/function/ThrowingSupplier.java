package com.hccake.ballcat.common.redis.lock.function;

public interface ThrowingSupplier<T> {

	/**
	 * 可抛异常的supplier
	 * @return T
	 * @throws Throwable 异常
	 */
	T get() throws Throwable;

}
