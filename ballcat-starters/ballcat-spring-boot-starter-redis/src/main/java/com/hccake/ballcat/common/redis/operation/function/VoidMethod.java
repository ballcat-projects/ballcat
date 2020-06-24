package com.hccake.ballcat.common.redis.operation.function;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/9/2 20:15
 */
@FunctionalInterface
public interface VoidMethod {

	/**
	 * 只执行 无返回值
	 */
	void run();

}
