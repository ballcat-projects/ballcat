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

package org.ballcat.desensitize.logging.logback;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 单元测试类，用于验证 LoggerDesensitizationFilter 的功能。 测试重点是 globalEnabled、defaultDecision 和最长前缀匹配
 * (LPM) 算法的正确性。
 */
class LoggerDesensitizationFilterTest {

	private Map<String, Boolean> rules;

	/**
	 * 在每个测试方法执行前初始化规则映射。 这确保了每个测试都是独立的。
	 */
	@BeforeEach
	void setUp() {
		this.rules = new HashMap<>();
		// 规则：com.a 允许脱敏
		this.rules.put("com.a", true);
		// 规则：com.a.b.c 不允许脱敏
		this.rules.put("com.a.b.c", false);
	}

	@Test
	void testGlobalEnabledIsFalse() {
		// 当 globalEnabled 为 false 时，无论任何规则或默认值，都应返回 false。
		LoggerDesensitizationFilter filter = new LoggerDesensitizationFilter(false, true, this.rules);

		assertFalse(filter.allow("com.a"), "globalEnabled为false时，应始终不允许脱敏");
		assertFalse(filter.allow("com.a.b.c"), "globalEnabled为false时，应始终不允许脱敏");
		assertFalse(filter.allow("org.springframework"), "globalEnabled为false时，应始终不允许脱敏");
	}

	@Test
	void testDefaultDecisionIsTrue() {
		// 当 globalEnabled 为 true，defaultDecision 为 true 时的测试。
		// 未匹配任何规则时，应返回 true。
		LoggerDesensitizationFilter filter = new LoggerDesensitizationFilter(true, true, this.rules);

		// 精确匹配 com.a，规则为 true
		assertTrue(filter.allow("com.a"), "精确匹配 'com.a' 应返回 true");

		// 精确匹配 com.a.b.c，规则为 false
		assertFalse(filter.allow("com.a.b.c"), "精确匹配 'com.a.b.c' 应返回 false");

		// 最长前缀匹配 (LPM) [1, 2]：com.a.b 匹配到 com.a，规则为 true
		assertTrue(filter.allow("com.a.b"), "LPM 'com.a.b' 应返回 true");

		// LPM [1, 2]：com.a.v.c 匹配到 com.a，规则为 true
		assertTrue(filter.allow("com.a.v.c"), "LPM 'com.a.v.c' 应返回 true");

		// LPM [1, 2]：com.a.b.c.d 匹配到 com.a.b.c，规则为 false
		assertFalse(filter.allow("com.a.b.c.d"), "LPM 'com.a.b.c.d' 应返回 false");

		// 没有匹配，应返回默认值 true
		assertTrue(filter.allow("org.springframework.web"), "未匹配规则时应返回默认值 true");
	}

	@Test
	void testDefaultDecisionIsFalse() {
		// 当 globalEnabled 为 true，defaultDecision 为 false 时的测试。
		// 未匹配任何规则时，应返回 false。
		LoggerDesensitizationFilter filter = new LoggerDesensitizationFilter(true, false, this.rules);

		// 精确匹配 com.a，规则为 true
		assertTrue(filter.allow("com.a"), "精确匹配 'com.a' 应返回 true");

		// 精确匹配 com.a.b.c，规则为 false
		assertFalse(filter.allow("com.a.b.c"), "精确匹配 'com.a.b.c' 应返回 false");

		// 最长前缀匹配 (LPM) [1, 2]：com.a.b 匹配到 com.a，规则为 true
		assertTrue(filter.allow("com.a.b"), "LPM 'com.a.b' 应返回 true");

		// LPM [1, 2]：com.a.b.c.d 匹配到 com.a.b.c，规则为 false
		assertFalse(filter.allow("com.a.b.c.d"), "LPM 'com.a.b.c.d' 应返回 false");

		// 没有匹配，应返回默认值 false
		assertFalse(filter.allow("org.springframework.web"), "未匹配规则时应返回默认值 false");
	}

	@Test
	void testEmptyRulesMap() {
		// 测试规则映射为空（或 null）时的行为。
		LoggerDesensitizationFilter filter1 = new LoggerDesensitizationFilter(true, true, new HashMap<>());
		assertTrue(filter1.allow("com.a.b"), "空规则且默认值true时，应返回true");
		assertTrue(filter1.allow("org.springframework"), "空规则且默认值true时，应返回true");

		LoggerDesensitizationFilter filter2 = new LoggerDesensitizationFilter(true, false, null);
		assertFalse(filter2.allow("com.a.b"), "null规则且默认值false时，应返回false");
		assertFalse(filter2.allow("org.springframework"), "null规则且默认值false时，应返回false");
	}

}
