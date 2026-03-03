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

package org.ballcat.desensitize.text;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Aho-Corasick 前缀扫描用例：大小写不敏感、子串冲突（phone vs phoneNumber）。
 *
 * @author Hccake
 */
public class PrefixScannerCaseTest {

	@Test
	void should_match_case_insensitive_and_chinese_prefix() {
		PrefixScanner scanner = new PrefixScanner(Arrays.asList("phone", "phoneNumber", "手机号"));
		String text = "PhoNe: 1, phoneNumber: 2, 手机号：3";
		List<PrefixScanner.Hit> hits = scanner.findAll(text);
		List<String> kws = hits.stream().map(h -> h.keyword).collect(Collectors.toList());
		assertTrue(kws.contains("phone"));
		assertTrue(kws.contains("phoneNumber"));
		assertTrue(kws.contains("手机号"));
	}

	@Test
	void should_emit_both_for_overlapping_keywords() {
		PrefixScanner scanner = new PrefixScanner(Arrays.asList("phone", "phoneNumber"));
		List<PrefixScanner.Hit> hits = scanner.findAll("abc phoneNumber: 138");
		List<String> kws = hits.stream().map(h -> h.keyword).collect(Collectors.toList());
		assertTrue(kws.contains("phoneNumber"));
		assertTrue(kws.contains("phone"));
		// 验证命中区间正确（endExclusive - start = 11 对应 "phoneNumber" 长度）
		PrefixScanner.Hit longest = hits.stream()
			.filter(h -> "phoneNumber".equals(h.keyword))
			.findFirst()
			.orElseThrow(AssertionError::new);
		assertEquals("phoneNumber".length(), longest.endExclusive - longest.start);
	}

}
