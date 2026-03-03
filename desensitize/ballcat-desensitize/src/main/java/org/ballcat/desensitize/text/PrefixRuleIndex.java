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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 前缀到规则下标的索引，按配置顺序存放候选规则。
 *
 * @author Hccake
 * @since 2.0.0
 */
public class PrefixRuleIndex {

	private final Map<String, int[]> prefixToRuleIndexes; // key: lower-case prefix

	PrefixRuleIndex(Map<String, List<Integer>> source) {
		Map<String, int[]> tmp = new HashMap<>();
		for (Map.Entry<String, List<Integer>> e : source.entrySet()) {
			List<Integer> list = e.getValue();
			int[] arr = new int[list.size()];
			for (int i = 0; i < list.size(); i++) {
				arr[i] = list.get(i);
			}
			tmp.put(e.getKey(), arr);
		}
		this.prefixToRuleIndexes = Collections.unmodifiableMap(tmp);
	}

	int[] candidatesFor(String keywordLower) {
		int[] arr = this.prefixToRuleIndexes.get(keywordLower);
		return arr == null ? new int[0] : arr;
	}

}
