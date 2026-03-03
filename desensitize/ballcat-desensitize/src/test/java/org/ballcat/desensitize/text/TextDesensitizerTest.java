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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import org.ballcat.desensitize.DesensitizeType;
import org.ballcat.desensitize.text.config.RegexReplacementParams;
import org.ballcat.desensitize.text.config.RuleSpec;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TextDesensitizerTest {

	@Test
	@DisplayName("前缀大小写不敏感匹配验证")
	void mixed_case_prefix_should_match() {
		TextDesensitizer engine = new TextDesensitizerBuilder().addRule(TestFixtures.phoneRegexRule()).build();
		String s = engine.desensitize("PhoNe=\"13877891234\" and 手机号：13877891234");
		assertTrue(s.contains("\"138****1234\""));
		assertTrue(s.contains("手机号：138****1234"));
	}

	@Test
	@DisplayName("中文前缀与全角符号验证")
	void fullwidth_colon_and_brackets_and_multiline() {
		TextDesensitizer engine = new TextDesensitizerBuilder().addRule(TestFixtures.phoneRegexRule())
			.addRule(TestFixtures.bankSlideRule())
			.build();
		String in = "手机号：\"13877891234\"\n银行卡：\n=\n【6222021234567890123】";
		String out = engine.desensitize(in);
		assertTrue(out.contains("\"138****1234\""));
		assertTrue(out.contains("【***************0123】"));
	}

	@Test
	@DisplayName("JSON风格键值的脱敏验证")
	void single_field_json_should_be_masked() {
		TextDesensitizer engine = new TextDesensitizerBuilder().addRule(TestFixtures.phoneRegexRule()).build();
		String in = "{\"phone\":\"13877891234\"}";
		String out = engine.desensitize(in);
		assertEquals("{\"phone\":\"138****1234\"}", out);
	}

	@Test
	@DisplayName("JSON风格键值对验证")
	void multiple_fields_json_should_be_masked() {
		TextDesensitizer engine = new TextDesensitizerBuilder().addRule(TestFixtures.phoneRegexRule())
			.addRule(TestFixtures.idRegexRule())
			.addRule(TestFixtures.bankSlideRule())
			.build();
		String in = "{\"phone\":\"13877891234\",\"idCard\":\"110105199001011234\",\"bankCard\":\"6222021234567890123\"}";
		String out = engine.desensitize(in);
		assertEquals(
				"{\"phone\":\"138****1234\",\"idCard\":\"1101***********234\",\"bankCard\":\"***************0123\"}",
				out);
	}

	@Test
	@Tag("long")
	@DisplayName("超长文本验证")
	void long_payload_should_be_masked_correctly() {
		TextDesensitizer engine = new TextDesensitizerBuilder().addRule(TestFixtures.phoneRegexRule())
			.addRule(TestFixtures.bankSlideRule())
			.addRule(TestFixtures.idRegexRule())
			.build();

		String token = " 手机号：13877891234 身份证:110105199001012345 bank=[6222021234567890123] ";
		String payload = TestFixtures.buildLongPayload(120_000, token, 200);
		assertTrue(payload.length() >= 120_000);

		String out = engine.desensitize(payload);
		assertFalse(out.contains("13877891234"));
		assertFalse(out.contains("110105199001012345"));
		assertFalse(out.contains("6222021234567890123"));
		assertTrue(out.contains("138****1234"));
		assertTrue(out.contains("1101***********345") || out.contains("110105********2345"));
		assertTrue(out.contains("***************0123"));
	}

	@Test
	@DisplayName("matchFromStart 开启测试")
	void matchFromStart_true_should_only_lookingAt() {
		RuleSpec r = new RuleSpec().setName("kv")
			.setPrefixes(Collections.singletonList("key"))
			.setValuePattern("xx(\\d{4})")
			.setMatchFromStart(true)
			.setDesensitizeType(DesensitizeType.REGEX_REPLACEMENT)
			.setRegex(new RegexReplacementParams().setReplacement("xx****"));
		TextDesensitizer engine = new TextDesensitizerBuilder().addRule(r).build();
		String out = engine.desensitize("key: aa xx1234");
		// lookingAt 失败 -> 不替换
		assertTrue(out.endsWith("aa xx1234"));
	}

	@Test
	@DisplayName("matchFromStart 关闭测试")
	void matchFromStart_false_should_use_find() {
		RuleSpec r = new RuleSpec().setName("kv")
			.setPrefixes(Collections.singletonList("key"))
			.setValuePattern("xx(\\d{4})")
			.setMatchFromStart(false)
			.setDesensitizeType(DesensitizeType.REGEX_REPLACEMENT)
			.setRegex(new RegexReplacementParams().setReplacement("xx****"));
		TextDesensitizer engine = new TextDesensitizerBuilder().addRule(r).build();
		String out = engine.desensitize("key: aa xx1234");
		assertTrue(out.contains("xx****"));
	}

	@Test
	@DisplayName("混合分隔符与空白验证")
	void mixed_separators_and_spaces_should_work() {
		TextDesensitizer engine = new TextDesensitizerBuilder().addRule(TestFixtures.phoneRegexRule())
			.addRule(TestFixtures.bankSlideRule())
			.build();
		String in = "mobile  :   \"   13877891234   \"  , bank :   [  6222021234567890  ]";
		String out = engine.desensitize(in);
		assertEquals("mobile  :   \"   138****1234   \"  , bank :   [  ************7890  ]", out);
	}

	@Test
	@DisplayName("同一文本内多次/多类型命中")
	void multiple_hits_should_all_be_masked() {
		TextDesensitizer engine = new TextDesensitizerBuilder().addRule(TestFixtures.phoneRegexRule())
			.addRule(TestFixtures.bankSlideRule())
			.addRule(TestFixtures.idRegexRule())
			.build();
		String in = "phone:\"13877891234\" bank=[6222021234567890123] 身份证:110105199001012345 手机号：13877891234";
		String out = engine.desensitize(in);
		assertTrue(out.contains("\"138****1234\""));
		assertTrue(out.contains("bank=[***************0123]"));
		assertTrue(out.contains("身份证:1101***********345"));
		assertTrue(out.contains("手机号：138****1234"));
	}

	@Test
	@DisplayName("多规则冲突：短前缀不应抢占长前缀场景（phone vs phoneNumber）。")
	void longer_prefix_should_take_effect() {
		RuleSpec phone = new RuleSpec().setName("phone")
			.setPrefixes(Collections.singletonList("phone"))
			.setValuePattern("(1[3-9]\\d)(\\d{4})(\\d{4})")
			.setMatchFromStart(true)
			.setDesensitizeType(DesensitizeType.REGEX_REPLACEMENT)
			.setRegex(new RegexReplacementParams().setReplacement("$1****$3"));
		RuleSpec phoneNumber = new RuleSpec().setName("phoneNumber")
			.setPrefixes(Collections.singletonList("phoneNumber"))
			.setValuePattern("(1[3-9]\\d)(\\d{4})(\\d{4})")
			.setMatchFromStart(true)
			.setDesensitizeType(DesensitizeType.REGEX_REPLACEMENT)
			.setRegex(new RegexReplacementParams().setReplacement("$1-****-$3"));
		TextDesensitizer engine = new TextDesensitizerBuilder().addRule(phone).addRule(phoneNumber).build();
		String out = engine.desensitize("phoneNumber: 13877891234 phone: 13900112233");
		assertTrue(out.contains("138-****-1234"));
		assertTrue(out.contains("139****2233"));
	}

	@Test
	@DisplayName("无前缀场景不处理")
	void no_prefix_should_not_change_text() {
		TextDesensitizer engine = new TextDesensitizerBuilder().addRule(TestFixtures.phoneRegexRule()).build();
		String in = "号码 13877891234 不应处理";
		assertEquals(in, engine.desensitize(in));
	}

	@Test
	@DisplayName("并发线程安全：多线程并发 sanitize 输出一致无异常。")
	void concurrent_sanitize_should_be_consistent() {
		TextDesensitizer engine = new TextDesensitizerBuilder().addRule(TestFixtures.phoneRegexRule())
			.addRule(TestFixtures.bankSlideRule())
			.addRule(TestFixtures.idRegexRule())
			.build();

		List<String> inputs = new ArrayList<>();
		inputs.add("phone:\"13877891234\"");
		inputs.add("手机号：13877891234");
		inputs.add("bank=[6222021234567890123]");
		inputs.add("身份证:110105199001012345");
		inputs.add("mixed phone=\"13900112233\" bank=[6222021234567890]");

		AtomicInteger errors = new AtomicInteger();
		IntStream.range(0, 2000).parallel().forEach(i -> {
			String in = inputs.get(i % inputs.size());
			try {
				String out = engine.desensitize(in);
				// 不应包含原始明文
				assertFalse(out.contains("13877891234"));
				assertFalse(out.contains("13900112233"));
				assertFalse(out.contains("6222021234567890123"));
				assertFalse(out.contains("6222021234567890"));
				assertFalse(out.contains("110105199001012345"));
			}
			catch (Throwable t) {
				errors.incrementAndGet();
			}
		});
		assertEquals(0, errors.get(), "sanitize should not throw under concurrency");
	}

}
