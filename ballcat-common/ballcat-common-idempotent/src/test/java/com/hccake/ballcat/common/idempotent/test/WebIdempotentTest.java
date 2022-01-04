package com.hccake.ballcat.common.idempotent.test;

import com.hccake.ballcat.common.idempotent.exception.IdempotentException;
import com.hccake.ballcat.common.idempotent.key.InMemoryIdempotentKeyStore;
import lombok.extern.slf4j.Slf4j;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author hccake
 */
@Slf4j
@WebMvcTest(IdempotentController.class)
@AutoConfigureMockMvc
@SpringJUnitConfig({ IdempotentTestConfiguration.class, InMemoryIdempotentKeyStore.class })
class WebIdempotentTest {

	@Autowired
	private IdempotentMethods idempotentMethods;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void shouldReturnDefaultMessage() throws Exception {
		this.mockMvc.perform(get("/").header("formId", "formId1")).andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString("hello word")));

		this.mockMvc.perform(get("/").header("formId", "formId1")).andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString("hello word")));
	}

	@Test
	void testIdempotent() throws InterruptedException {
		tryExecute("aaa");
		Assertions.assertTrue(tryExecute("bbb"));
		Assertions.assertFalse(tryExecute("bbb"));

		Awaitility.await().atMost(1100, TimeUnit.MILLISECONDS).pollDelay(1000, TimeUnit.MILLISECONDS)
				.untilAsserted(() -> assertTrue(tryExecute("bbb")));
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
