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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = TestApplication.class)
@ActiveProfiles("it")
public class LoggerLevelE2EIT {

	private static final Logger allow = LoggerFactory
		.getLogger("org.ballcat.desensitize.logging.integration.LoggerLevelE2EIT");

	private static final Logger deny = LoggerFactory
		.getLogger("org.ballcat.desensitize.logging.integration.hot.Feature");

	@Test
	void include_exclude_via_map_levels_should_work() throws Exception {
		allow.info("phone: 13877891234");
		deny.info("phone: 13900112233");

		byte[] bytes = Files.readAllBytes(Paths.get("target/test-logs/e2e.log"));
		String s = new String(bytes, StandardCharsets.UTF_8);
		assertTrue(s.contains("phone: 138****1234"));
		assertTrue(s.contains("phone: 13900112233"));
		assertFalse(s.contains("13877891234"));
	}

}
