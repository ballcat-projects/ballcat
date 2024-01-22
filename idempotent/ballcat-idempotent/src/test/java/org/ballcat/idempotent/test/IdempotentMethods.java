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

package org.ballcat.idempotent.test;

import lombok.extern.slf4j.Slf4j;
import org.ballcat.idempotent.annotation.Idempotent;

/**
 * @author hccake
 */
@Slf4j
public class IdempotentMethods {

	/**
	 * 基础的幂等控制方法
	 */
	@Idempotent(uniqueExpression = "#key", duration = 1)
	public void method(String key) {
		log.info("===执行方法1成功===, key：" + key);
	}

	/**
	 * 幂等控制时，抛出不同的错误信息
	 */
	@Idempotent(uniqueExpression = "#key", duration = 1, message = "不允许短期内重复执行方法2")
	public void differentMessage(String key) {
		log.info("===执行方法2成功===, key：" + key);
	}

	/**
	 * 完成后允许重复执行
	 */
	@Idempotent(uniqueExpression = "'repeatableWhenFinished'", duration = 1, removeKeyWhenFinished = true)
	public void repeatableWhenFinished() {
		log.info("===执行方法 repeatableWhenFinished 成功===");
	}

	/**
	 * 完成后不允许重复执行
	 */
	@Idempotent(uniqueExpression = "'repeatableWhenFinished'", duration = 1)
	public void unRepeatableWhenFinished() {
		log.info("===执行方法 unRepeatableWhenFinished 成功===");
	}

	/**
	 * 异常后允许重复执行的方法
	 */
	@Idempotent(uniqueExpression = "'repeatableWhenError'", duration = 1, removeKeyWhenError = true)
	public void repeatableWhenError() {
		throw new TestException("repeatableWhenError 假装抛出一个异常");
	}

	/**
	 * 异常后不允许重复执行的方法
	 */
	@Idempotent(uniqueExpression = "'unRepeatableWhenError'", duration = 1)
	public void unRepeatableWhenError() {
		throw new TestException("unRepeatableWhenError 假装抛出一个异常");
	}

}
