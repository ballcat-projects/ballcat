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

package org.ballcat.common.core.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author hccake
 */
@Slf4j
class NullSerializerTest {

	@Test
	void test() throws JsonProcessingException {
		// 原始输出
		ObjectMapper objectMapper = new ObjectMapper();

		String json = objectMapper.writeValueAsString(new NullSeriralDemoData());
		Assertions.assertEquals("{\"str\":null,\"map\":null,\"collection\":null}", json, "Json 序列化异常");
	}

	@Test
	void test2() throws JsonProcessingException {
		// NULL值修改
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setSerializerProvider(new NullSerializerProvider());

		String json = objectMapper.writeValueAsString(new NullSeriralDemoData());
		Assertions.assertEquals("{\"str\":\"\",\"map\":{},\"collection\":[]}", json, "Json 序列化异常");
	}

}
