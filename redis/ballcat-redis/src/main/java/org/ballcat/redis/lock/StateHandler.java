/*
 * Copyright 2023-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ballcat.redis.lock;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import org.ballcat.redis.lock.function.ExceptionHandler;

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
	 * 控制锁竞争失败时的重试次数
	 * @param retryCount
	 */
	StateHandler<T> retryCount(int retryCount);

	/**
	 * 终态，获取锁
	 * @return result
	 */
	T lock();

}
