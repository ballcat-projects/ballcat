package com.hccake.ballcat.common.redis.test;

import com.hccake.ballcat.common.redis.lock.DistributedLock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.io.IOException;

/**
 * @author huyuanzhi
 * @version 1.0
 * @date 2021/11/16
 */
@SpringJUnitConfig(RedisConfiguration.class)
class DistributeLockTest {

	String lockKey = "ballcat";

	@Test
	void testSuccess() {
		String value = DistributedLock.<String>instance().action(lockKey, () -> "value")
				.onSuccess(ret -> ret + "Success").lock();
		Assertions.assertEquals("valueSuccess", value);
	}

	@Test
	void testHandleException() {
		String value = DistributedLock.<String>instance().action(lockKey, this::throwIOException)
				.onSuccess(ret -> ret + ret).onException(e -> System.out.println("发生异常了")).lock();
		Assertions.assertNull(value);
	}

	@Test
	void testThrowException() {
		Assertions.assertThrows(IOException.class,
				() -> DistributedLock.<String>instance().action(lockKey, this::throwIOException).lock());
	}

	String throwIOException() throws IOException {
		throw new IOException();
	}

}
