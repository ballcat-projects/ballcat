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

import org.ballcat.desensitize.rule.regex.EmailRegexDesensitizeRule;
import org.ballcat.desensitize.rule.slide.PhoneNumberSlideDesensitizeRule;
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
				DesensitizationUtil.desensitizeByRegex("test.demo@qq.com", new EmailRegexDesensitizeRule()));
		assertEquals("t****@qq.com",
				DesensitizationUtil.desensitizeByRegex("test.demo@qq.com", "(^.)[^@]*(@.*$)", "$1****$2"));
		assertEquals("010******76",
				DesensitizationUtil.desensitizeBySlide("01089898976", new PhoneNumberSlideDesensitizeRule()));
		assertEquals("***898989**",
				DesensitizationUtil.desensitizeBySlide("01089898976", new PhoneNumberSlideDesensitizeRule(), true));
		assertEquals("430123******431", DesensitizationUtil.desensitizeBySlide("430123990101431", 6, 3));
		assertEquals("430123********432X", DesensitizationUtil.desensitizeBySlide("43012319990101432X", 6, 4));
		assertEquals("430123????????432X", DesensitizationUtil.desensitizeBySlide("43012319990101432X", 6, 4, "?"));
		assertEquals("张*丰", DesensitizationUtil.desensitizeChineseName("张三丰"));
		assertEquals("430123********432X", DesensitizationUtil.desensitizeIdCardNo("43012319990101432X"));
		assertEquals("138******78", DesensitizationUtil.desensitizePhoneNumber("13812345678"));
		assertEquals("北京市西城区******", DesensitizationUtil.desensitizeAddress("北京市西城区金城坊街2号"));
		assertEquals("t****@qq.com", DesensitizationUtil.desensitizeEmail("test.demo@qq.com"));
		assertEquals("622260**********1234", DesensitizationUtil.desensitizeBankCardNo("62226000000043211234"));
		assertEquals("******", DesensitizationUtil.desensitizePassword(UUID.randomUUID().toString()));
		assertEquals("000****34", DesensitizationUtil.desensitizeKey("0000000123456q34"));
		assertEquals("192.*.*.*", DesensitizationUtil.desensitizeIP("192.168.2.1"));
		assertEquals("2001:*:*:*:*:*:*:*",
				DesensitizationUtil.desensitizeIP("2001:0db8:02de:0000:0000:0000:0000:0e13"));
		assertEquals("2001:*:*:*:*:*:*:*", DesensitizationUtil.desensitizeIP("2001:db8:2de:0:0:0:0:e13"));
		assertEquals("4*01***99*********",
				DesensitizationUtil.desensitizeByIndex("43012319990101432X", "1", "4-6", "9-"));
		assertEquals("4-01---99---------",
				DesensitizationUtil.desensitizeByIndex("43012319990101432X", '-', false, "1", "4-6", "9-"));
		assertEquals("-3--231--90101432X",
				DesensitizationUtil.desensitizeByIndex("43012319990101432X", '-', true, "1", "4-6", "9-"));
	}

}
