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

package org.ballcat.desensitize.text.compiler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import org.ballcat.desensitize.DesensitizeType;
import org.ballcat.desensitize.text.config.RegexReplacementParams;
import org.ballcat.desensitize.text.config.RuleSpec;

/**
 * 规则编译器：配置 POJO -> PreparedRule。
 *
 * @author Hccake
 * @since 2.0.0
 */
public final class RuleCompiler {

	private RuleCompiler() {
	}

	public static List<CompiledRule> compile(List<RuleSpec> specs) {
		Objects.requireNonNull(specs, "specs");
		if (specs.isEmpty()) {
			return Collections.emptyList();
		}

		List<CompiledRule> out = new ArrayList<>(specs.size());
		for (RuleSpec s : specs) {
			validate(s);
			Pattern p = s.getValuePattern() == null ? null : Pattern.compile(s.getValuePattern());
			CompiledRule pr = new CompiledRule(s.getName(), s.getPrefixes(), p,
					s.getMatchFromStart() == null || s.getMatchFromStart(), s.getDesensitizeType(), s.getSlide(),
					s.getRegex(), s.getSimple());
			out.add(pr);
		}
		return out;
	}

	private static void validate(RuleSpec s) {
		if (s.getName() == null || s.getName().trim().isEmpty()) {
			throw new IllegalArgumentException("rule name must not be empty");
		}
		if (s.getPrefixes() == null || s.getPrefixes().isEmpty()) {
			throw new IllegalArgumentException("prefixes must not be empty");
		}
		DesensitizeType t = s.getDesensitizeType();
		if (t == null) {
			throw new IllegalArgumentException("desensitize type must not be null");
		}

		if (s.getValuePattern() == null) {
			throw new IllegalArgumentException("valuePattern must not be null");
		}

		switch (t) {
			case REGEX_REPLACEMENT:
				RegexReplacementParams rp = s.getRegex();
				if (rp == null || rp.getReplacement() == null) {
					throw new IllegalArgumentException("regex.replacement required for REGEX_REPLACEMENT");
				}
				if (s.getSlide() != null || s.getSimple() != null) {
					throw new IllegalArgumentException("slide/simple must be null for REGEX_REPLACEMENT");
				}
				break;
			case SLIDE_MASK:
				if (s.getSlide() == null) {
					throw new IllegalArgumentException("slide params required for SLIDE_MASK");
				}
				if (s.getRegex() != null || s.getSimple() != null) {
					throw new IllegalArgumentException("regex/simple must be null for SLIDE_MASK");
				}
				break;
			case SIMPLE_HANDLE:
				if (s.getSimple() == null || s.getSimple().getHandler() == null) {
					throw new IllegalArgumentException("simple.handler required for SIMPLE_HANDLE");
				}
				if (s.getRegex() != null || s.getSlide() != null) {
					throw new IllegalArgumentException("regex/slide must be null for SIMPLE_HANDLE");
				}
				break;
			default:
				// 索引脱敏等类型：预留
				throw new IllegalArgumentException("unsupported desensitize type: " + t);
		}
	}

}
