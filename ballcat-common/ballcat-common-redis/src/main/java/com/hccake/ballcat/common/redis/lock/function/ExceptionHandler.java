package com.hccake.ballcat.common.redis.lock.function;

/**
 * 异常处理器，可在处理完异常后再次抛出异常
 *
 * @author hccake
 */
@FunctionalInterface
public interface ExceptionHandler {

	/**
	 * 处理异常
	 * @param throwable 待处理的异常
	 */
	void handle(Throwable throwable);

}
