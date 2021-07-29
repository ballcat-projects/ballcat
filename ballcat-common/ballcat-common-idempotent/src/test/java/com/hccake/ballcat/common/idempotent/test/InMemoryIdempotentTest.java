package com.hccake.ballcat.common.idempotent.test;

import cn.hutool.core.lang.Assert;
import com.hccake.ballcat.common.idempotent.exception.IdempotentException;
import com.hccake.ballcat.common.idempotent.key.InMemoryIdempotentKeyStore;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
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

	@Test
	void testIdempotent() throws InterruptedException {
		tryExecute("aaa");
		Assert.isTrue(tryExecute("bbb"));
		Assert.isFalse(tryExecute("bbb"));
		Thread.sleep(1000);
		Assert.isTrue(tryExecute("bbb"));
	}

	private boolean tryExecute(String key) {
		try {
			idempotentMethods.method1(key);
		}
		catch (IdempotentException e) {
			System.out.println(e.getMessage());
			return false;
		}
		return true;
	}

}
