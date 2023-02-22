package com.hccake.ballcat.common.idempotent.test;

import com.hccake.ballcat.common.idempotent.exception.IdempotentException;
import com.hccake.ballcat.common.idempotent.key.store.InMemoryIdempotentKeyStore;
import lombok.extern.slf4j.Slf4j;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
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

	/**
	 * 幂等基础测试：防重以及时长
	 */
	@Test
	void testIdempotent() {
		Assertions.assertDoesNotThrow(() -> idempotentMethods.method("aaa"));

		Executable executable = () -> idempotentMethods.method("bbb");
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
		Executable executable = () -> idempotentMethods.repeatableWhenFinished();
		Assertions.assertDoesNotThrow(executable);
		Assertions.assertDoesNotThrow(executable);

		// 异常时不可重复执行
		Executable normal = () -> idempotentMethods.unRepeatableWhenFinished();
		Assertions.assertDoesNotThrow(normal);
		Assertions.assertThrowsExactly(IdempotentException.class, normal);
	}

	@Test
	void testRepeatableWhenError() {
		// 异常时可重复执行
		Executable executable = () -> idempotentMethods.repeatableWhenError();
		Assertions.assertThrowsExactly(TestException.class, () -> idempotentMethods.repeatableWhenError());
		Assertions.assertThrowsExactly(TestException.class, () -> idempotentMethods.repeatableWhenError());

		// 异常时不可重复执行
		Executable normal = () -> idempotentMethods.unRepeatableWhenError();
		Assertions.assertThrowsExactly(TestException.class, () -> idempotentMethods.unRepeatableWhenError());
		Assertions.assertThrowsExactly(IdempotentException.class, () -> idempotentMethods.unRepeatableWhenError());
	}

	/**
	 * 幂等错误消息测试
	 */
	@Test
	void testIdempotentMessage() {
		idempotentMethods.method("ccc");
		try {
			idempotentMethods.method("ccc");
		}
		catch (Exception ex) {
			Assertions.assertEquals("重复请求，请稍后重试", ex.getMessage());
		}

		idempotentMethods.differentMessage("ddd");
		try {
			idempotentMethods.differentMessage("ddd");
		}
		catch (Exception ex) {
			Assertions.assertEquals("不允许短期内重复执行方法2", ex.getMessage());
		}
	}

}
