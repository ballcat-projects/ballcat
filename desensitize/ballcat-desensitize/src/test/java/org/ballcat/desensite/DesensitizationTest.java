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

package org.ballcat.desensite;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.ballcat.desensitize.DesensitizationHandlerHolder;
import org.ballcat.desensitize.handler.IndexDesensitizationHandler;
import org.ballcat.desensitize.handler.PhoneNumberDesensitizationHandler;
import org.ballcat.desensitize.handler.RegexDesensitizationHandler;
import org.ballcat.desensitize.handler.SimpleDesensitizationHandler;
import org.ballcat.desensitize.handler.SixAsteriskDesensitizationHandler;
import org.ballcat.desensitize.handler.SlideDesensitizationHandler;
import org.ballcat.desensitize.json.JsonDesensitizeSerializerModifier;
import org.ballcat.desensitize.rule.regex.EmailRegexDesensitizeRule;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Hccake 2021/1/23
 */
@Slf4j
class DesensitizationTest {

	@Test
	void testSimple() {
		// 获取简单脱敏处理器
		SimpleDesensitizationHandler desensitizationHandler = DesensitizationHandlerHolder
			.getSimpleDesensitizationHandler(SixAsteriskDesensitizationHandler.class);
		String origin = "你好吗？"; // 原始字符串
		String target = desensitizationHandler.mask(origin); // 替换处理
		assertEquals("******", target);

		SimpleDesensitizationHandler phoneNumberDesensitizationHandler = DesensitizationHandlerHolder
			.getSimpleDesensitizationHandler(PhoneNumberDesensitizationHandler.class);
		String target2 = phoneNumberDesensitizationHandler.mask("15834427989");
		assertEquals("158****7989", target2);
	}

	@Test
	void testRegex() {
		// 获取正则脱敏处理器
		RegexDesensitizationHandler desensitizationHandler = DesensitizationHandlerHolder
			.getRegexDesensitizationHandler();
		String origin = "12123124213@qq.com"; // 原始字符串
		String regex = "(^.)[^@]*(@.*$)"; // 正则表达式
		String replacement = "$1****$2"; // 占位替换表达式
		String target1 = desensitizationHandler.mask(origin, regex, replacement); // 替换处理
		assertEquals("1****@qq.com", target1);

		// 内置的正则脱敏类型
		String target2 = desensitizationHandler.mask(origin, new EmailRegexDesensitizeRule());
		assertEquals("1****@qq.com", target2);
	}

	@Test
	void testSlide() {
		// 获取滑动脱敏处理器
		SlideDesensitizationHandler desensitizationHandler = DesensitizationHandlerHolder
			.getSlideDesensitizationHandler();
		String origin = "15805516789"; // 原始字符串
		String target1 = desensitizationHandler.mask(origin, 3, 2); // 替换处理
		assertEquals("158******89", target1);

		String target11 = desensitizationHandler.mask(origin, 3, 2, true); // 替换处理
		assertEquals("***055167**", target11);
	}

	@Test
	void testRule() {
		// 获取基于规则脱敏处理器
		IndexDesensitizationHandler desensitizationHandler = DesensitizationHandlerHolder
			.getIndexDesensitizationHandler();
		String origin = "43012319990101432X"; // 原始字符串
		String target1 = desensitizationHandler.mask(origin, "1", "4-6", "9-"); // 替换处理
		assertEquals("4*01***99*********", target1);

		String target2 = desensitizationHandler.mask(origin, true, "1", "4-6", "9-"); // 替换处理
		assertEquals("*3**231**90101432X", target2);
	}

	@Test
	void testJackson() throws Exception {
		TestUtils.resetEnv();

		// 指定DesensitizeHandler 若ignore方法为true 则忽略脱敏 false 则启用脱敏
		JsonDesensitizeSerializerModifier modifier = new JsonDesensitizeSerializerModifier((fieldName) -> {
			log.info("当前字段名称{}", fieldName);
			return false;
		});
		// 不指定 实现类 默认使用脱敏规则
		// JsonSerializerModifier modifier = new JsonSerializerModifier();
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setSerializerFactory(objectMapper.getSerializerFactory().withSerializerModifier(modifier));
		DesensitizationUser user = new DesensitizationUser().setEmail("chengbohua@foxmail.com")
			.setUsername("xiaoming")
			.setPassword("admina123456")
			.setPhoneNumber("15800000000")
			.setTestField("这是测试属性")
			.setCustomDesensitize("test")
			.setRuleDesensitize("43012319990101432X")
			.setRuleReverseDesensitize("43012319990101432X");
		String value = objectMapper.writeValueAsString(user);
		log.info("脱敏后的数据：{}", value);

		String expected = "{\"username\":\"xiaoming\",\"password\":\"adm****56\",\"email\":\"c****@foxmail.com\",\"phoneNumber\":\"158****0000\",\"testField\":\"TEST-这是测试属性\",\"customDesensitize\":\"test\",\"ruleDesensitize\":\"4*01***99*********\",\"ruleReverseDesensitize\":\"*3**231**90101432X\"}";
		assertEquals(expected, value);
	}

}
