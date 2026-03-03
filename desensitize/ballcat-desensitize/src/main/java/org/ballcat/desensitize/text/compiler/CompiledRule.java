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

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import lombok.Getter;
import org.ballcat.desensitize.DesensitizeType;
import org.ballcat.desensitize.text.config.RegexReplacementParams;
import org.ballcat.desensitize.text.config.SimpleHandlerParams;
import org.ballcat.desensitize.text.config.SlideMaskParams;

/**
 * 已准备态规则：从规则声明编译得到的只读数据。
 *
 * @see org.ballcat.desensitize.text.config.RuleSpec
 * @author Hccake
 * @since 2.0.0
 */
@Getter
public final class CompiledRule {

	String name;

	List<String> prefixes;

	Pattern valuePatternCompiled;

	boolean matchFromStart;

	DesensitizeType desensitizeType;

	RegexReplacementParams regex;

	SlideMaskParams slide;

	SimpleHandlerParams simple;

	public CompiledRule(String name, List<String> prefixes, Pattern valuePatternCompiled, boolean matchFromStart,
			DesensitizeType desensitizeType, SlideMaskParams slide, RegexReplacementParams regex,
			SimpleHandlerParams simple) {
		this.name = name;
		this.prefixes = Collections.unmodifiableList(prefixes);
		this.valuePatternCompiled = valuePatternCompiled;
		this.matchFromStart = matchFromStart;
		this.desensitizeType = desensitizeType;
		this.slide = slide;
		this.regex = regex;
		this.simple = simple;
	}

}
