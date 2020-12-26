package com.hccake.ballcat.common.websocket.function;

/**
 * session function
 *
 * @author Yakir
 */
@FunctionalInterface
public interface SessionFunction<T, R> {

	/**
	 * apply
	 * @param t
	 * @return
	 */
	R apply(T t);

}
