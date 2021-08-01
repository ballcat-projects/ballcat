package com.hccake.ballcat.common.redis.operation.function;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/9/2 20:22
 */
@FunctionalInterface
public interface ResultMethod<T> {

	/**
	 * 执行并返回一个结果
	 * @return result
	 */
	T run();

}
