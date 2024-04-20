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

import java.util.UUID;

import org.ballcat.desensitize.handler.PhoneNumberDesensitizationHandler;
import org.ballcat.desensitize.rule.regex.EmailRegexDesensitizeRule;
import org.ballcat.desensitize.rule.slide.IdCardNoSlideDesensitizeRule;
import org.ballcat.desensitize.util.DesensitizationUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author evil0th Create on 2024/4/12
 */
class DesensitizationUtilTest {

	@Test
	void test() {
		assertEquals("t****@qq.com",
				DesensitizationUtil.maskByRegex("test.demo@qq.com", new EmailRegexDesensitizeRule()));
		assertEquals("t****@qq.com",
				DesensitizationUtil.maskByRegex("test.demo@qq.com", "(^.)[^@]*(@.*$)", "$1****$2"));
		assertEquals("010****8976",
				DesensitizationUtil.maskBySimpleHandler("01089898976", PhoneNumberDesensitizationHandler.class));
		assertEquals("655356********1234",
				DesensitizationUtil.maskBySlide("655356198812031234", new IdCardNoSlideDesensitizeRule()));
		assertEquals("******19881203****",
				DesensitizationUtil.maskBySlide("655356198812031234", new IdCardNoSlideDesensitizeRule(), true));
		assertEquals("430123******431", DesensitizationUtil.maskBySlide("430123990101431", 6, 3));
		assertEquals("430123********432X", DesensitizationUtil.maskBySlide("43012319990101432X", 6, 4));
		assertEquals("430123????????432X", DesensitizationUtil.maskBySlide("43012319990101432X", 6, 4, "?"));
		assertEquals("张*丰", DesensitizationUtil.maskChineseName("张三丰"));
		assertEquals("430123********432X", DesensitizationUtil.maskIdCardNo("43012319990101432X"));
		assertEquals("138****5678", DesensitizationUtil.maskPhoneNumber("13812345678"));
		assertEquals("北京市西城区******", DesensitizationUtil.maskAddress("北京市西城区金城坊街2号"));
		assertEquals("t****@qq.com", DesensitizationUtil.maskEmail("test.demo@qq.com"));
		assertEquals("622260**********1234", DesensitizationUtil.maskBankCardNo("62226000000043211234"));
		assertEquals("******", DesensitizationUtil.maskPassword(UUID.randomUUID().toString()));
		assertEquals("192.*.*.*", DesensitizationUtil.maskIP("192.168.2.1"));
		assertEquals("2001:*:*:*:*:*:*:*", DesensitizationUtil.maskIP("2001:0db8:02de:0000:0000:0000:0000:0e13"));
		assertEquals("2001:*:*:*:*:*:*:*", DesensitizationUtil.maskIP("2001:db8:2de:0:0:0:0:e13"));
		assertEquals("4*01***99*********", DesensitizationUtil.maskByIndex("43012319990101432X", "1", "4-6", "9-"));
		assertEquals("4-01---99---------",
				DesensitizationUtil.maskByIndex("43012319990101432X", '-', false, "1", "4-6", "9-"));
		assertEquals("-3--231--90101432X",
				DesensitizationUtil.maskByIndex("43012319990101432X", '-', true, "1", "4-6", "9-"));
	}

	@Test
	void testMaskPhoneNumber() {
		String input1 = "12-34-567";
		String expected1 = "1*-**-*67";
		assertEquals(expected1, DesensitizationUtil.maskPhoneNumber(input1));

		String input2 = "12 34 567";
		String expected2 = "1* ** *67";
		assertEquals(expected2, DesensitizationUtil.maskPhoneNumber(input2));

		String input3 = "+86 12345678901";
		String expected3 = "+86 1******8901";
		assertEquals(expected3, DesensitizationUtil.maskPhoneNumber(input3));

		String input4 = "1234";
		String expected4 = "1*34";
		assertEquals(expected4, DesensitizationUtil.maskPhoneNumber(input4));

		String input5 = "12345678";
		String expected5 = "1*****78";
		assertEquals(expected5, DesensitizationUtil.maskPhoneNumber(input5));

		String input6 = "12345678012";
		String expected6 = "123****8012";
		assertEquals(expected6, DesensitizationUtil.maskPhoneNumber(input6));

		String input7 = "12";
		String expected7 = "12";
		assertEquals(input7, DesensitizationUtil.maskPhoneNumber(expected7));
	}

}
