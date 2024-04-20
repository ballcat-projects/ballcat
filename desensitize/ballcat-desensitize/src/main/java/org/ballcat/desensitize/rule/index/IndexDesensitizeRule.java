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

package org.ballcat.desensitize.rule.index;

import java.util.PriorityQueue;

/**
 *
 * index 脱敏规则工具类.
 * <p>
 * 1. 下标从0开始<br>
 * 2. 单个下标：数字<br>
 * 3. 范围下标：数字1-数字2/数字1-<br>
 * 4. 多个规则合并：规则1，规则2<br>
 * 如：3-6，8，10- ，表示第4，5，6，7，9，11以及11之后的位使用加密字符替换
 *
 * @author evil0th Create on 2024/4/12
 */
public final class IndexDesensitizeRule {

	private final PriorityQueue<Integer> indexQueue;

	private boolean endless;

	private IndexDesensitizeRule() {
		this.indexQueue = new PriorityQueue<>((o1, o2) -> o2 - o1);
		this.endless = false;
	}

	/**
	 * 解析规则
	 * @param rules 规则列表
	 * @return rule
	 */
	public static IndexDesensitizeRule analysis(String... rules) {
		IndexDesensitizeRule rule = new IndexDesensitizeRule();
		if (null == rules) {
			return rule;
		}
		for (String ruleStr : rules) {
			if (null == ruleStr || ruleStr.isEmpty()) {
				continue;
			}
			String[] groups = ruleStr.split(",");
			for (String group : groups) {
				if (null == group || group.isEmpty()) {
					continue;
				}
				String[] items = group.split("-");
				if (items.length == 1) {
					rule.addIndex(items[0]);
				}
				else {
					rule.addIndex(items[0], items[items.length - 1]);
				}
				rule.endless |= group.endsWith("-");
			}
		}
		return rule;
	}

	void addIndex(String index) {
		addIndex(Integer.parseInt(index));
	}

	void addIndex(String startIndex, String endIndex) {
		addIndex(Integer.parseInt(startIndex), Integer.parseInt(endIndex));
	}

	void addIndex(int index) {
		if (isIn(index)) {
			return;
		}
		this.indexQueue.add(index);
	}

	void addIndex(int startIndex, int endIndex) {
		for (int index = startIndex; index <= endIndex; ++index) {
			addIndex(index);
		}
	}

	public boolean isIn(int index) {
		if (this.indexQueue.contains(index)) {
			return true;
		}
		if (!this.indexQueue.isEmpty() && this.endless) {
			return this.indexQueue.peek() < index;
		}
		return false;
	}

	@Override
	public String toString() {
		return "IndexRule{" + "index=" + this.indexQueue + ", endless=" + this.endless + "}";
	}

}
