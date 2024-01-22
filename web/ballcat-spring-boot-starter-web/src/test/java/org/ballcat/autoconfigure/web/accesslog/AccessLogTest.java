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

package org.ballcat.autoconfigure.web.accesslog;

import org.ballcat.web.accesslog.AccessLogFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
class AccessLogTest {

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private AccessLogFilter accessLogFilter;

	private MockMvc mockMvc;

	@BeforeEach
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).addFilters(this.accessLogFilter).build();
	}

	@Test
	void testDefault() throws Exception {
		TestAccessLogFilter testAccessLogFilter = (TestAccessLogFilter) this.accessLogFilter;
		testAccessLogFilter.clearHttpInfo();

		// ’/test‘ 走默认选项。只记录 queryString, 不记录请求体和响应体
		HttpInfo httpInfo = testAccessLogFilter.getHttpInfo();
		assertNull(httpInfo);
		this.mockMvc.perform(MockMvcRequestBuilders.get("/test?a=1"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.content().string("Hello, World!"));
		httpInfo = testAccessLogFilter.getHttpInfo();
		assertNotNull(httpInfo);
		assertEquals("a=1", httpInfo.getQueryString());
		assertNull(httpInfo.getRequestBody());
		assertNull(httpInfo.getResponseBody());
	}

	@Test
	void testIgnored() throws Exception {
		TestAccessLogFilter testAccessLogFilter = (TestAccessLogFilter) this.accessLogFilter;
		testAccessLogFilter.clearHttpInfo();

		// ’/test/ignored‘ 根据配置会进行忽略
		HttpInfo httpInfo = testAccessLogFilter.getHttpInfo();
		assertNull(httpInfo);
		this.mockMvc.perform(MockMvcRequestBuilders.get("/test/ignored?a=2"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.content().string("Hello, World!"));
		assertNull(testAccessLogFilter.getHttpInfo());
	}

	@Test
	void testAnnotation() throws Exception {
		TestAccessLogFilter testAccessLogFilter = (TestAccessLogFilter) this.accessLogFilter;
		testAccessLogFilter.clearHttpInfo();

		// ’/test/annotation‘ 根据注解会进行记录 queryString 和响应体
		HttpInfo httpInfo = testAccessLogFilter.getHttpInfo();
		assertNull(httpInfo);

		String prefix = "haha";
		this.mockMvc.perform(MockMvcRequestBuilders.post("/test/annotation?a=3").content(prefix))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.content().string(prefix + " Hello, World!"));
		httpInfo = testAccessLogFilter.getHttpInfo();
		assertNotNull(httpInfo);
		assertEquals("a=3", httpInfo.getQueryString());
		assertNull(httpInfo.getRequestBody());
		assertEquals(prefix + " Hello, World!", httpInfo.getResponseBody());
	}

	@Test
	void testRecordAll() throws Exception {
		TestAccessLogFilter testAccessLogFilter = (TestAccessLogFilter) this.accessLogFilter;
		testAccessLogFilter.clearHttpInfo();

		// ’/test/record-all‘ 根据配置会进行记录 queryString 请求体和响应体
		HttpInfo httpInfo = testAccessLogFilter.getHttpInfo();
		assertNull(httpInfo);

		String prefix = "haha";
		this.mockMvc.perform(MockMvcRequestBuilders.post("/test/record-all?a=4").content(prefix))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.content().string(prefix + " Hello, World!"));
		httpInfo = testAccessLogFilter.getHttpInfo();
		assertNotNull(httpInfo);
		assertEquals("a=4", httpInfo.getQueryString());
		assertEquals(prefix, httpInfo.getRequestBody());
		assertEquals(prefix + " Hello, World!", httpInfo.getResponseBody());
	}

}
