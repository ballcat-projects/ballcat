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

package org.ballcat.redis;

import java.io.IOException;

import org.ballcat.redis.lock.DistributedLock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

/**
 * @author huyuanzhi 2021/11/16
 */
@SpringJUnitConfig(TestRedisConfiguration.class)
class DistributeLockTest {

	String lockKey = "ballcat";

	@Test
	void testSuccess() {
		String value = DistributedLock.<String>instance()
			.action(this.lockKey, () -> "value")
			.onSuccess(ret -> ret + "Success")
			.lock();
		Assertions.assertEquals("valueSuccess", value);
	}

	@Test
	void testHandleException() {
		String value = DistributedLock.<String>instance()
			.action(this.lockKey, this::throwIOException)
			.onSuccess(ret -> ret + ret)
			.onException(e -> System.out.println("发生异常了"))
			.lock();
		Assertions.assertNull(value);
	}

	@Test
	void testThrowException() {
		Assertions.assertThrows(IOException.class,
				() -> DistributedLock.<String>instance().action(this.lockKey, this::throwIOException).lock());
	}

	String throwIOException() throws IOException {
		throw new IOException();
	}

}
