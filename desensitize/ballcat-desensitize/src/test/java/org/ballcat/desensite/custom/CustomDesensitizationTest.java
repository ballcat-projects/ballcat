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

package org.ballcat.desensite.custom;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.ballcat.desensite.DesensitizationUser;
import org.ballcat.desensite.TestUtils;
import org.ballcat.desensitize.DesensitizationHandlerHolder;
import org.ballcat.desensitize.annotation.AnnotationHandlerHolder;
import org.ballcat.desensitize.json.JsonDesensitizeSerializerModifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Hccake 2021/1/23
 *
 */
@Slf4j
class CustomDesensitizationTest {

	@Test
	void desensitizedExtend() throws Exception {
		TestUtils.resetEnv();

		ObjectMapper objectMapper = new ObjectMapper();

		// 注册自定义脱敏类型处理器
		CustomDesensitizationHandler customDesensitizationHandler = new CustomDesensitizationHandler();
		DesensitizationHandlerHolder.addDesensitizationHandler(CustomDesensitizationHandler.class,
				customDesensitizationHandler);
		// 注册注解 处理器
		AnnotationHandlerHolder.addHandleFunction(CustomerDesensitize.class, (annotation, value) -> {
			CustomerDesensitize customerDesensitize = (CustomerDesensitize) annotation;
			String type = customerDesensitize.type();
			log.info("注解上的参数：{}", type);
			CustomDesensitizationHandler handler = (CustomDesensitizationHandler) DesensitizationHandlerHolder
				.getDesensitizationHandler(CustomDesensitizationHandler.class);
			return handler.handle(value);
		});
		// 初始化序列号modifier
		JsonDesensitizeSerializerModifier modifier = new JsonDesensitizeSerializerModifier();
		objectMapper.setSerializerFactory(objectMapper.getSerializerFactory().withSerializerModifier(modifier));

		DesensitizationUser user = new DesensitizationUser().setEmail("chengbohua@foxmail.com")
			.setUsername("xiaoming")
			.setPassword("admina123456")
			.setPhoneNumber("15800000000")
			.setTestField("这是测试属性")
			.setCustomDesensitize("自定义属性");
		String value = objectMapper.writeValueAsString(user);
		log.info("脱敏后的数据：{}", value);
		String expected = "{\"username\":\"xiaoming\",\"password\":\"adm****56\",\"email\":\"c****@foxmail.com\",\"phoneNumber\":\"158****0000\",\"testField\":\"TEST-这是测试属性\",\"customDesensitize\":\"customer rule自定义属性\",\"ruleDesensitize\":null,\"ruleReverseDesensitize\":null}";
		Assertions.assertEquals(expected, value);
	}

}
