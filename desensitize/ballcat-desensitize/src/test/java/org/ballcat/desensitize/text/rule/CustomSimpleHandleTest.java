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
import org.ballcat.desensitize.TestDesensitizationHandler;
import org.ballcat.desensitize.text.TextDesensitizer;
import org.ballcat.desensitize.text.TextDesensitizerBuilder;
import org.ballcat.desensitize.text.config.RuleSpec;
import org.ballcat.desensitize.text.config.SimpleHandlerParams;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomSimpleHandleTest {

	@Test
	void simple_handle_should_apply() {
		SimpleHandlerParams sp = new SimpleHandlerParams();
		sp.setHandler(TestDesensitizationHandler.class);
		RuleSpec r = new RuleSpec().setName("note")
			.setPrefixes(Collections.singletonList("note"))
			.setValuePattern("[a-z]*")
			.setDesensitizeType(DesensitizeType.SIMPLE_HANDLE)
			.setSimple(sp);
		TextDesensitizer engine = new TextDesensitizerBuilder().addRule(r).build();
		String out = engine.desensitize("note: abc");
		assertEquals("note: TEST-abc", out);
	}

}
