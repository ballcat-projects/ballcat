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

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.core.OutputStreamAppender;
import ch.qos.logback.core.encoder.LayoutWrappingEncoder;
import org.ballcat.desensitize.DesensitizeType;
import org.ballcat.desensitize.text.TextDesensitizer;
import org.ballcat.desensitize.text.TextDesensitizerBuilder;
import org.ballcat.desensitize.text.config.RegexReplacementParams;
import org.ballcat.desensitize.text.config.RuleSpec;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ThrowableConverterStackTest {

	@Test
	void stack_should_be_kept_but_messages_masked() {
		RuleSpec phone = new RuleSpec().setName("phone")
			.setPrefixes(Collections.singletonList("phone"))
			.setValuePattern("(1[3-9]\\d)(\\d{4})(\\d{4})")
			.setMatchFromStart(true)
			.setDesensitizeType(DesensitizeType.REGEX_REPLACEMENT)
			.setRegex(new RegexReplacementParams().setReplacement("$1****$3"));
		TextDesensitizer engine = new TextDesensitizerBuilder().addRule(phone).build();
		TextDesensitizerHolder.set(engine);

		// 过滤器：root=false，com.allow=true
		Map<String, Boolean> rules = new HashMap<>();
		rules.put("t", Boolean.TRUE);
		LoggerDesensitizationFilterHolder.set(new LoggerDesensitizationFilter(true, false, rules));

		LoggerContext ctx = new LoggerContext();
		Logger logger = ctx.getLogger("t");
		PatternLayout layout = new PatternLayout();
		layout.setContext(ctx);
		layout.getDefaultConverterMap().put("dEx", DesensitizeThrowableProxyConverter.class.getName());
		layout.setPattern("%dEx%n");
		layout.start();
		LayoutWrappingEncoder<ch.qos.logback.classic.spi.ILoggingEvent> enc = new LayoutWrappingEncoder<>();
		enc.setContext(ctx);
		enc.setLayout(layout);
		enc.setCharset(StandardCharsets.UTF_8);
		enc.start();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		OutputStreamAppender<ch.qos.logback.classic.spi.ILoggingEvent> app = new OutputStreamAppender<>();
		app.setContext(ctx);
		app.setEncoder(enc);
		app.setOutputStream(out);
		app.start();
		logger.addAppender(app);

		Exception cause = new IllegalArgumentException("phone: 13877891234");
		Exception ex = new RuntimeException("phone: 13900112233", cause);
		logger.error("oops", ex);
		app.stop();
		String s = new String(out.toByteArray(), StandardCharsets.UTF_8);
		assertTrue(s.contains("RuntimeException: phone: 139****2233"));
		assertTrue(s.contains("Caused by: java.lang.IllegalArgumentException: phone: 138****1234"));
		assertTrue(s.contains("\tat "));
	}

}
