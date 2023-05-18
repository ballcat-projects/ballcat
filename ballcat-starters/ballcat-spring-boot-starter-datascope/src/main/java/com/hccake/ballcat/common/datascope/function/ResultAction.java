package com.hccake.ballcat.common.datascope.function;

/**
 * 有返回值的操作接口
 *
 * @author hccake
 */
@FunctionalInterface
public interface ResultAction<T> {

	/**
	 * 执行操作
	 */
	T execute();

}