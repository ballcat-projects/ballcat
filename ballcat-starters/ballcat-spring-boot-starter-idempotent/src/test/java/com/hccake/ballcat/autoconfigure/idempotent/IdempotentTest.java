package com.hccake.ballcat.autoconfigure.idempotent;

import com.hccake.ballcat.common.idempotent.key.store.IdempotentKeyStore;
import com.hccake.ballcat.common.idempotent.key.store.InMemoryIdempotentKeyStore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

/**
 * 测试幂等装配
 *
 * @author lishangbu
 * @since 2022/10/18
 */
@SpringBootTest(classes = IdempotentAutoConfiguration.class)
class IdempotentTest {

	@Autowired
	private IdempotentKeyStore idempotentKeyStore;

	@Test
	void test() {
		Assertions.assertInstanceOf(InMemoryIdempotentKeyStore.class, idempotentKeyStore);
		idempotentKeyStore.saveIfAbsent("test", 1, TimeUnit.SECONDS);
	}

}
