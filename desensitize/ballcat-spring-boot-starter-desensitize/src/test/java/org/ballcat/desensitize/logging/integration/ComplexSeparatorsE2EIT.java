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

package org.ballcat.desensitize.logging.integration;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 复杂分隔符/包裹符/跨行场景的端到端验证。 验证 dMsg 与 dEx 转换规则生效，且仅异常 message 被脱敏。
 *
 * @author Hccake
 */
@SpringBootTest(classes = TestApplication.class)
@ActiveProfiles("it")
public class ComplexSeparatorsE2EIT {

	private static final Logger log = LoggerFactory.getLogger(ComplexSeparatorsE2EIT.class);

	@Test
	void complex_separators_and_enclosers_should_be_desensitized() throws Exception {
		// 多样分隔符/包裹符与跨行
		log.info("mobile='13877891234'");
		log.info("手机号:\"13877891234\"");
		log.info("idCard=【110105199001012345】");
		log.info("证件号: 110105199001012345");
		log.info("bank=[6222021234567890123]");
		log.info("acct:[\"6222021234567890\"]");
		log.info("phone:\n:\n\"13877891234\" bank = [6222021234567890]");

		// 值未命中，保持原值
		log.info("mobile='abc'");
		log.info("idNo: 11010519900101");
		log.info("phoneNumber: 13877891234"); // 非配置前缀

		// 异常仅 message 脱敏，栈保持
		Exception cause = new IllegalArgumentException("bank: 6222021234567890123");
		Exception ex = new RuntimeException("phone: 13877891234", cause);
		log.error("boom", ex);

		byte[] bytes = Files.readAllBytes(Paths.get("target/test-logs/e2e.log"));
		String s = new String(bytes, StandardCharsets.UTF_8);

		// 命中应被脱敏
		assertTrue(s.contains("'138****1234'"));
		assertTrue(s.contains("\"138****1234\""));
		assertTrue(s.contains("idCard=【1101***********345】") || s.contains("证件号: 1101***********345"));
		assertTrue(s.contains("bank=[***************0123]"));
		assertTrue(s.contains("acct:[\"************7890\"]") || s.contains("bank = [************7890]"));

		// 值未命中或前缀未配置保持原样
		assertTrue(s.contains("mobile='abc'"));
		assertTrue(s.contains("idNo: 11010519900101"));
		assertTrue(s.contains("phoneNumber: 13877891234"));

		// 异常仅 message 被脱敏，且包含栈标识
		assertTrue(s.contains("RuntimeException: phone: 138****1234"));
		assertTrue(s.contains("Caused by: java.lang.IllegalArgumentException: bank: ***************0123"));
		assertTrue(s.contains("at ")); // 栈痕迹
	}

}
