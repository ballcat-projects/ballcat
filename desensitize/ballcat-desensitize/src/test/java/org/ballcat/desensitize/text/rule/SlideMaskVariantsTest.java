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

package org.ballcat.desensitize.text.rule;

import java.util.Collections;

import org.ballcat.desensitize.DesensitizeType;
import org.ballcat.desensitize.text.TextDesensitizer;
import org.ballcat.desensitize.text.TextDesensitizerBuilder;
import org.ballcat.desensitize.text.config.RuleSpec;
import org.ballcat.desensitize.text.config.SlideMaskParams;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SlideMaskVariantsTest {

	@Test
	void multiCharMask_and_reverse_false() {
		SlideMaskParams slide = new SlideMaskParams();
		slide.setLeftPlainTextLen(2);
		slide.setRightPlainTextLen(3);
		slide.setMaskString("XX");
		RuleSpec r = new RuleSpec().setName("bank")
			.setPrefixes(Collections.singletonList("bank"))
			.setValuePattern("\\d{8}")
			.setMatchFromStart(true)
			.setDesensitizeType(DesensitizeType.SLIDE_MASK)
			.setSlide(slide);
		TextDesensitizer engine = new TextDesensitizerBuilder().addRule(r).build();
		String out = engine.desensitize("bank=12345678");
		assertEquals("bank=12XXXXXX678", out);
	}

	@Test
	void keepMoreThanLength_shouldReturnOriginal() {
		SlideMaskParams slide = new SlideMaskParams();
		slide.setLeftPlainTextLen(5);
		slide.setRightPlainTextLen(5);
		slide.setMaskString("*");
		RuleSpec r = new RuleSpec().setName("bank")
			.setPrefixes(Collections.singletonList("bank"))
			.setValuePattern("\\d{8}")
			.setMatchFromStart(true)
			.setDesensitizeType(DesensitizeType.SLIDE_MASK)
			.setSlide(slide);
		TextDesensitizer engine = new TextDesensitizerBuilder().addRule(r).build();
		String in = "bank=12345678";
		assertEquals(in, engine.desensitize(in));
	}

}
