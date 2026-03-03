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

import java.lang.reflect.Constructor;
import java.util.stream.Stream;

import org.ballcat.desensitize.text.config.BoundaryOptions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class BoundaryLocatorTest {

	@Test
	void test() {
		BoundaryOptions opt = new BoundaryOptions();
		BoundaryLocator sm = new BoundaryLocator(opt);

		String text = "phone: \n=\n 【13866660000】";
		int valueStart = sm.locate(text, 5).getStart();
		assertEquals(12, valueStart);
	}

	@Test
	void testSeparatorsAndEnclosers() {
		BoundaryOptions opt = new BoundaryOptions();
		BoundaryLocator sm = new BoundaryLocator(opt);
		String s = "phone : \"13877891234\"";
		int idx = s.indexOf("phone") + "phone".length();
		int vs = sm.locate(s, idx).getStart();
		assertTrue(vs > 0);
		assertEquals('1', s.charAt(vs));
	}

	@Test
	void testChineseColonAndBracket() {
		BoundaryOptions opt = new BoundaryOptions();
		BoundaryLocator sm = new BoundaryLocator(opt);
		String s = "银行卡： [6222021234567890]";
		int idx = s.indexOf("银行卡") + 3;
		int vs = sm.locate(s, idx).getStart();
		assertTrue(vs > 0);
		assertEquals('6', s.charAt(vs));
	}

	@Test
	void testUnclosedEnclosers() {
		BoundaryOptions opt = new BoundaryOptions();
		BoundaryLocator sm = new BoundaryLocator(opt);
		String s1 = "phone=\"138";
		int vs1 = sm.locate(s1, s1.indexOf("phone") + 5).getStart();
		assertTrue(vs1 > 0);

		String s2 = "phone=[138";
		int vs2 = sm.locate(s2, s2.indexOf("phone") + 5).getStart();
		assertTrue(vs2 > 0);
	}

	@Test
	void testJsonStyleColonAndQuotes() {
		BoundaryOptions opt = new BoundaryOptions();
		BoundaryLocator sm = new BoundaryLocator(opt);
		String s = "\"phone\":\"13877891234\"";
		int idx = s.indexOf("\"phone\"") + "\"phone\"".length();
		int vs = sm.locate(s, idx).getStart();
		assertTrue(vs > 0);
		assertEquals('1', s.charAt(vs));
	}

	static Stream<String> cases() {
		return Stream.of("phone: 138", "phone : 138", "phone:\t138", "phone：138", "\"phone\":\"138\"", "phone=\"138\"",
				"phone=【138】", "phone=[138]",
				// 未闭合包裹符
				"phone=\"138", "phone=[138");
	}

	@ParameterizedTest
	@MethodSource("cases")
	void locate_value_start_should_skip_ws_and_optional_enclosers(String text) {
		BoundaryOptions opts = new BoundaryOptions();
		try {
			Class<?> cls = Class.forName("org.ballcat.desensitize.text.BoundaryLocator");
			Constructor<?> c = cls.getDeclaredConstructor(BoundaryOptions.class);
			c.setAccessible(true);
			Object m = c.newInstance(opts);
			BoundaryLocator.Boundary boundary = (BoundaryLocator.Boundary) cls
				.getDeclaredMethod("locate", CharSequence.class, int.class)
				.invoke(m, text, text.indexOf("e") + 1);
			int start = boundary.getStart();
			assertTrue(start >= 0 && start < text.length());
		}
		catch (Exception e) {
			fail(e);
		}
	}

}
