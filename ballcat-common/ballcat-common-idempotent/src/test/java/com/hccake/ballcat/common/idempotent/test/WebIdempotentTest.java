package com.hccake.ballcat.common.idempotent.test;

import cn.hutool.core.lang.Assert;
import com.hccake.ballcat.common.idempotent.exception.IdempotentException;
import com.hccake.ballcat.common.idempotent.key.InMemoryIdempotentKeyStore;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
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
