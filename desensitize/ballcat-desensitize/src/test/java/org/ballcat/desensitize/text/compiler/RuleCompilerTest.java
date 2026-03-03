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

import org.ballcat.desensitize.DesensitizeType;
import org.ballcat.desensitize.text.config.RegexReplacementParams;
import org.ballcat.desensitize.text.config.RuleSpec;
import org.ballcat.desensitize.text.config.SlideMaskParams;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RuleCompilerTest {

	@Test
	void regexReplacement_ok() {
		RuleSpec r = new RuleSpec().setName("phone")
			.setPrefixes(Collections.singletonList("phone"))
			.setValuePattern("(1[3-9]\\d)(\\d{4})(\\d{4})")
			.setMatchFromStart(true)
			.setDesensitizeType(DesensitizeType.REGEX_REPLACEMENT)
			.setRegex(new RegexReplacementParams().setReplacement("$1****$3"));
		assertDoesNotThrow(() -> RuleCompiler.compile(Collections.singletonList(r)));
	}

	@Test
	void regexReplacement_missingReplacement_shouldFail() {
		RuleSpec r = new RuleSpec().setName("phone")
			.setPrefixes(Collections.singletonList("phone"))
			.setValuePattern("(1[3-9]\\d)(\\d{4})(\\d{4})")
			.setDesensitizeType(DesensitizeType.REGEX_REPLACEMENT);
		assertThrows(IllegalArgumentException.class, () -> RuleCompiler.compile(Collections.singletonList(r)));
	}

	@Test
	void slideMask_ok() {
		SlideMaskParams slide = new SlideMaskParams();
		slide.setLeftPlainTextLen(0);
		slide.setRightPlainTextLen(4);
		slide.setMaskString("*");
		RuleSpec r = new RuleSpec().setName("bank")
			.setPrefixes(Collections.singletonList("bank"))
			.setValuePattern("\\d{16,19}")
			.setMatchFromStart(true)
			.setDesensitizeType(DesensitizeType.SLIDE_MASK)
			.setSlide(slide);
		assertDoesNotThrow(() -> RuleCompiler.compile(Collections.singletonList(r)));
	}

	@Test
	void slideMask_withRegex_shouldFail() {
		SlideMaskParams slide = new SlideMaskParams();
		slide.setLeftPlainTextLen(0);
		slide.setRightPlainTextLen(4);
		RuleSpec r = new RuleSpec().setName("bank")
			.setPrefixes(Collections.singletonList("bank"))
			.setValuePattern("\\d{16,19}")
			.setDesensitizeType(DesensitizeType.SLIDE_MASK)
			.setSlide(slide)
			.setRegex(new RegexReplacementParams().setReplacement("***"));
		assertThrows(IllegalArgumentException.class, () -> RuleCompiler.compile(Collections.singletonList(r)));
	}

	@Test
	void slideMask_missingParams_shouldFail() {
		RuleSpec r = new RuleSpec().setName("bank")
			.setPrefixes(Collections.singletonList("bank"))
			.setValuePattern("\\d{16,19}")
			.setDesensitizeType(DesensitizeType.SLIDE_MASK);
		assertThrows(IllegalArgumentException.class, () -> RuleCompiler.compile(Collections.singletonList(r)));
	}

}
