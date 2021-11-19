package com.hccake.ballcat.common.redis.lock;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

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
	StateHandler<T> success(Function<? super T, ? extends T> action);

	/**
	 * 获取锁失败回调
	 * @param action 回调方法引用
	 * @return 状态处理器
	 */
	StateHandler<T> fail(Supplier<? extends T> action);

	/**
	 * 获取锁成功，执行业务方法异常回调
	 * @param action 回调方法引用
	 * @return 状态处理器
	 */
	StateHandler<T> exception(Consumer<? super Throwable> action);

	/**
	 * 终态，获取锁
	 */
	T lock();

}
