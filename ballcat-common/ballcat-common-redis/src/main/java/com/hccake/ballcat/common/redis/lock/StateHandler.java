package com.hccake.ballcat.common.redis.lock;

import com.hccake.ballcat.common.redis.lock.function.ExceptionHandler;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * @author huyuanzhi 状态处理器
 * @param <T> 返回类型
 */
public interface StateHandler<T> {

	/**
	 * 获取锁成功，业务方法执行成功回调
	 * @param action 回调方法引用
	 * @return 状态处理器
	 */
	StateHandler<T> onSuccess(UnaryOperator<T> action);

	/**
	 * 获取锁失败回调
	 * @param action 回调方法引用
	 * @return 状态处理器
	 */
	StateHandler<T> onLockFail(Supplier<T> action);

	/**
	 * 获取锁成功，执行业务方法异常回调
	 * @param action 回调方法引用
	 * @return 状态处理器
	 */
	StateHandler<T> onException(ExceptionHandler action);

	/**
	 * 终态，获取锁
	 * @return result
	 */
	T lock();

}
