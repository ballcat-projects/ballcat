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

/**
 * Logger 过滤器（Map 风格，类似 Spring Boot logging.level）： - 全局 master 开关控制是否参与脱敏； - 基于 logger
 * 名的最长匹配规则查找开关，未命中回退到默认决策（root）； - 例如：com.example.service.UserService → 依次查找
 * com.example.service.UserService → com.example.service → com.example → com → default。
 *
 * @author Hccake
 */
public class LoggerDesensitizationFilter {

	private final boolean globalEnabled;

	private final boolean defaultDecision;

	private final TrieNode rootRuleTrieNode = new TrieNode();

	public LoggerDesensitizationFilter(boolean globalEnabled, boolean defaultDecision, Map<String, Boolean> rules) {
		this.globalEnabled = globalEnabled;
		this.defaultDecision = defaultDecision;
		if (rules != null) {
			for (Map.Entry<String, Boolean> entry : rules.entrySet()) {
				Boolean value = entry.getValue();
				String loggerName = entry.getKey();
				insert(loggerName, Boolean.TRUE.equals(value));
			}
		}
	}

	/**
	 * 将一个日志器配置规则及其值插入到Trie中。
	 * @param loggerName 配置规则名称，例如 "com.a.b"
	 * @param value 与规则关联的值
	 */
	public void insert(String loggerName, boolean value) {
		// 如果输入为空，则返回
		if (loggerName == null || loggerName.isEmpty()) {
			return;
		}
		String[] segments = loggerName.split("\\.");
		TrieNode current = this.rootRuleTrieNode;

		for (String segment : segments) {
			// 如果子节点不存在，则创建新节点
			current = current.children.computeIfAbsent(segment, k -> new TrieNode());
		}

		// 标记此节点为已配置，并存储其值
		current.isConfigured = true;
		current.value = value;
	}

	/**
	 * 是否允许对该条日志进行脱敏。
	 * @param loggerName 日志记录器名称（如 com.example.Foo）
	 */
	public boolean allow(String loggerName) {
		if (!this.globalEnabled) {
			return false;
		}
		return lookup(loggerName);
	}

	/**
	 * 查找给定日志器名称的最长匹配配置。 该算法遍历Trie，并返回路径上找到的最后一个已配置的值 。
	 * 查找时间复杂度为O(L)，其中L为日志器名称的长度，与配置规则总数无关。
	 * @param loggerName 待查找的日志器名称
	 * @return 匹配到的配置值，如果找不到任何匹配，则返回默认值
	 */
	public boolean lookup(String loggerName) {
		String[] segments = loggerName.split("\\.");
		TrieNode current = this.rootRuleTrieNode;
		boolean longestMatchValue = this.defaultDecision;

		for (String segment : segments) {
			TrieNode next = current.children.get(segment);
			if (next == null) {
				// 路径中断，返回找到的最后一个有效配置
				break;
			}
			current = next;

			// 如果当前节点有配置，更新最长匹配值
			if (current.isConfigured) {
				longestMatchValue = current.value;
			}
		}
		return longestMatchValue;
	}

	/**
	 * Trie的节点，用于存储日志器名称的分段。 每个节点通过其在树中的位置隐式地表示一个前缀。
	 *
	 * @author Hccake
	 */
	static class TrieNode {

		// 使用Map来存储子节点，键为日志器名称的下一分段
		// 这种方式比固定大小的数组更灵活，适用于任意字符串分段.[1, 2]
		Map<String, TrieNode> children = new HashMap<>();

		// 如果此节点代表一个已配置的日志器名称的末尾，则isConfigured为true。
		boolean isConfigured = false;

		// 存储与该配置规则关联的值（例如，布尔值或日志级别）。
		boolean value = false;

	}

}
