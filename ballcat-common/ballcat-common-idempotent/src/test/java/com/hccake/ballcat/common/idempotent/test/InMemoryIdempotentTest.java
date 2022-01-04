package com.hccake.ballcat.common.idempotent.test;

import com.hccake.ballcat.common.idempotent.exception.IdempotentException;
import com.hccake.ballcat.common.idempotent.key.InMemoryIdempotentKeyStore;
import lombok.extern.slf4j.Slf4j;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.concurrent.TimeUnit;

/**
 * @author hccake
 */
@Slf4j
@SpringJUnitConfig({ IdempotentTestConfiguration.class, InMemoryIdempotentKeyStore.class })
class InMemoryIdempotentTest {

	@Autowired
	private IdempotentMethods idempotentMethods;

	@Test
	void testIdempotent() {
		tryExecute("aaa");
		Assertions.assertTrue(tryExecute("bbb"));
		Assertions.assertFalse(tryExecute("bbb"));

		Awaitility.await().atMost(1100, TimeUnit.MILLISECONDS).pollDelay(1000, TimeUnit.MILLISECONDS)
				.untilAsserted(() -> Assertions.assertTrue(tryExecute("bbb")));
	}

	private boolean tryExecute(String key) {
		try {
			idempotentMethods.method1(key);
		}
		catch (IdempotentException e) {
			log.info("[method1]幂等拦截，不允许执行", e);
			return false;
		}
		return true;
	}

}
