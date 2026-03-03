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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * PrefixIndex 映射一致性测试：prefix -> 规则索引数组。
 *
 * @author Hccake
 */
public class PrefixRuleIndexMappingTest {

	@Test
	void mapping_should_preserve_order_and_return_empty_for_unknown() throws Exception {
		Map<String, List<Integer>> map = new HashMap<>();
		map.put("phone", Arrays.asList(2, 5));
		map.put("bank", Collections.singletonList(7));

		PrefixRuleIndex prefixRuleIndex = new PrefixRuleIndex(map);
		int[] phone = prefixRuleIndex.candidatesFor("phone");
		int[] bank = prefixRuleIndex.candidatesFor("bank");
		int[] none = prefixRuleIndex.candidatesFor("id");

		assertArrayEquals(new int[] { 2, 5 }, phone);
		assertArrayEquals(new int[] { 7 }, bank);
		assertEquals(0, none.length);
	}

}
