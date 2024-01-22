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

import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;
import org.awaitility.Awaitility;
import org.ballcat.idempotent.exception.IdempotentException;
import org.ballcat.idempotent.key.store.InMemoryIdempotentKeyStore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

/**
 * @author hccake
 */
@Slf4j
@SpringJUnitConfig({ IdempotentTestConfiguration.class, InMemoryIdempotentKeyStore.class })
class InMemoryIdempotentTest {

	@Autowired
	private IdempotentMethods idempotentMethods;

	/**
	 * 幂等基础测试：防重以及时长
	 */
	@Test
	void testIdempotent() {
		Assertions.assertDoesNotThrow(() -> this.idempotentMethods.method("aaa"));

		Executable executable = () -> this.idempotentMethods.method("bbb");
		Assertions.assertDoesNotThrow(executable);
		Assertions.assertThrowsExactly(IdempotentException.class, executable);

		Awaitility.await()
			.atMost(1100, TimeUnit.MILLISECONDS)
			.pollDelay(1000, TimeUnit.MILLISECONDS)
			.untilAsserted(() -> Assertions.assertDoesNotThrow(executable));
	}

	@Test
	void testRepeatableWhenFinished() {
		// 异常时可重复执行
		Executable executable = () -> this.idempotentMethods.repeatableWhenFinished();
		Assertions.assertDoesNotThrow(executable);
		Assertions.assertDoesNotThrow(executable);

		// 异常时不可重复执行
		Executable normal = () -> this.idempotentMethods.unRepeatableWhenFinished();
		Assertions.assertDoesNotThrow(normal);
		Assertions.assertThrowsExactly(IdempotentException.class, normal);
	}

	@Test
	void testRepeatableWhenError() {
		// 异常时可重复执行
		Executable executable = () -> this.idempotentMethods.repeatableWhenError();
		Assertions.assertThrowsExactly(TestException.class, () -> this.idempotentMethods.repeatableWhenError());
		Assertions.assertThrowsExactly(TestException.class, () -> this.idempotentMethods.repeatableWhenError());

		// 异常时不可重复执行
		Executable normal = () -> this.idempotentMethods.unRepeatableWhenError();
		Assertions.assertThrowsExactly(TestException.class, () -> this.idempotentMethods.unRepeatableWhenError());
		Assertions.assertThrowsExactly(IdempotentException.class, () -> this.idempotentMethods.unRepeatableWhenError());
	}

	/**
	 * 幂等错误消息测试
	 */
	@Test
	void testIdempotentMessage() {
		this.idempotentMethods.method("ccc");
		try {
			this.idempotentMethods.method("ccc");
		}
		catch (Exception ex) {
			Assertions.assertEquals("重复请求，请稍后重试", ex.getMessage());
		}

		this.idempotentMethods.differentMessage("ddd");
		try {
			this.idempotentMethods.differentMessage("ddd");
		}
		catch (Exception ex) {
			Assertions.assertEquals("不允许短期内重复执行方法2", ex.getMessage());
		}
	}

}
