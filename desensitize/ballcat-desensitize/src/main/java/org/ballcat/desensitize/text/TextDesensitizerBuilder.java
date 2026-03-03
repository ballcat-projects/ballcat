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
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.ballcat.desensitize.text.compiler.CompiledRule;
import org.ballcat.desensitize.text.compiler.RuleCompiler;
import org.ballcat.desensitize.text.config.BoundaryOptions;
import org.ballcat.desensitize.text.config.RuleSpec;

/**
 * 文本脱敏构建器：依据一组 {@link RuleSpec} 构建 {@link TextDesensitizer}。
 *
 * @author Hccake
 * @since 2.0.0
 */
public final class TextDesensitizerBuilder {

	private final List<RuleSpec> rules;

	private BoundaryOptions boundaryOptions = BoundaryOptions.defaults();

	public TextDesensitizerBuilder() {
		this(new ArrayList<>());
	}

	public TextDesensitizerBuilder(List<RuleSpec> rules) {
		this.rules = rules == null ? new ArrayList<>() : rules;
	}

	public TextDesensitizerBuilder addRule(RuleSpec rule) {
		if (rule != null) {
			this.rules.add(rule);
		}
		return this;
	}

	public TextDesensitizerBuilder config(BoundaryOptions config) {
		this.boundaryOptions = config == null ? BoundaryOptions.defaults() : config;
		return this;
	}

	public TextDesensitizer build() {
		// 编译 props 为 prepared 再构建
		List<CompiledRule> compiledRules = RuleCompiler.compile(this.rules);

		List<String> allPrefixes = new ArrayList<>();
		Map<String, List<Integer>> idx = new java.util.HashMap<>();
		for (int i = 0; i < compiledRules.size(); i++) {
			CompiledRule pr = compiledRules.get(i);
			for (String p : pr.getPrefixes()) {
				allPrefixes.add(p);
				String key = p.toLowerCase(Locale.ROOT);
				idx.computeIfAbsent(key, k -> new ArrayList<>()).add(i);
			}
		}
		PrefixScanner scanner = new PrefixScanner(allPrefixes);
		PrefixRuleIndex pi = new PrefixRuleIndex(idx);

		BoundaryOptions options = this.boundaryOptions == null ? BoundaryOptions.defaults() : this.boundaryOptions;
		BoundaryLocator boundaryLocator = new BoundaryLocator(options);

		return new TextDesensitizer(Collections.unmodifiableList(compiledRules), scanner, boundaryLocator, pi);
	}

}
