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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.ahocorasick.trie.Emit;
import org.ahocorasick.trie.Trie;

/**
 * Aho-Corasick 前缀扫描封装（基于 org.ahocorasick）。 对输入在外部完成大小写与全/半角归一后，在归一化文本上执行 AC 扫描以定位所有前缀命中。
 *
 * @author Hccake
 * @since 2.0.0
 */
public class PrefixScanner {

	private final Trie trie; // 基于前缀构建的 AC 自动机（大小写不敏感）

	PrefixScanner(List<String> prefixes) {
		Trie.TrieBuilder builder = Trie.builder().ignoreCase();
		for (String p : prefixes) {
			if (p != null && !p.isEmpty()) {
				builder.addKeyword(p);
			}
		}
		this.trie = builder.build();
	}

	List<Hit> findAll(CharSequence normalizedText) {
		List<Hit> hits = new ArrayList<>();
		if (normalizedText == null || normalizedText.length() == 0) {
			return hits;
		}
		Collection<Emit> emits = this.trie.parseText(normalizedText.toString());
		for (Emit e : emits) {
			hits.add(new Hit(e.getStart(), e.getEnd() + 1, e.getKeyword())); // end is
																				// exclusive
		}
		return hits;
	}

	static final class Hit {

		final int start; // 命中开始（归一化索引）

		final int endExclusive; // 命中结束（归一化索引，开区间）

		final String keyword; // 命中的前缀关键字（大小写不敏感）

		Hit(int start, int endExclusive, String keyword) {
			this.start = start;
			this.endExclusive = endExclusive;
			this.keyword = keyword;
		}

	}

}
