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

package org.ballcat.autoconfigure.web.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * jackson测试类
 *
 * @author evil0th Create on 2024/4/11
 */
@SpringBootTest
public class JacksonTest {

	@Autowired
	ObjectMapper objectMapper;

	@Test
	void testSerializeEmptyBeans() {
		assertDoesNotThrow(() -> this.objectMapper.writeValueAsString(new Object()));
	}

	@Test
	void testDeSerializeUnescapedControlChars() {
		assertDoesNotThrow(() -> this.objectMapper.readValue("{\"string\":\"abc\ncba\"}", Complex.class));
	}

	@Test
	void testDeSerializeUnknownProperties() {
		assertDoesNotThrow(() -> this.objectMapper.readValue("{\"unknown\":\"\"}", Complex.class));
	}

	@Test
	void testSerializeNull() throws Exception {
		Complex info = new Complex();
		String json = this.objectMapper.writeValueAsString(info);
		assertNotNull(json);
		assertEquals("{\"string\":\"\",\"map\":{},\"set\":[],\"list\":[]}", json);
	}

}
