package com.hccake.ballcat.common.redis.test;

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
	void testSuccess() {
		String lock = DistributedLock.<String>builder().action(lockKey, () -> lockKey).success(ret -> ret + ret).lock();
		System.out.println(lock);
	}

	@Test
	void testException() {
		String lock = DistributedLock.<String>builder().action(lockKey, this::get).success(ret -> ret + ret)
				.exception(e -> System.out.println("发生异常了")).lock();
		System.out.println(lock);
	}

	String get() throws IOException {
		throw new IOException();
	}

}
