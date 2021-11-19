package com.hccake.ballcat.common.redis.test;

import cn.hutool.core.lang.Assert;
import com.hccake.ballcat.common.redis.lock.DistributedLock;
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
	void testSuccess() throws Throwable {
		String value = DistributedLock.<String>instance().action(lockKey, () -> "value")
				.onSuccess(ret -> ret + "Success").lock();
		Assert.isTrue(value.equals("valueSuccess"));
	}

	@Test
	void testHandleException() throws Throwable {
		String value = DistributedLock.<String>instance().action(lockKey, this::throwIOException)
				.onSuccess(ret -> ret + ret).onException(e -> System.out.println("发生异常了")).lock();
		Assert.isNull(value);
	}

	@Test
	void testThrowException() {
		Throwable throwable = null;
		try {
			DistributedLock.<String>instance().action(lockKey, this::throwIOException).lock();
		}
		catch (Throwable th) {
			throwable = th;
		}

		Assert.isTrue(throwable instanceof IOException);
	}

	String throwIOException() throws IOException {
		throw new IOException();
	}

}
