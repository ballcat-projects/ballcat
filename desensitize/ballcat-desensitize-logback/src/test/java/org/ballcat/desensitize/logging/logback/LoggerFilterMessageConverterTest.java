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
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.OutputStreamAppender;
import ch.qos.logback.core.encoder.LayoutWrappingEncoder;
import org.ballcat.desensitize.DesensitizeType;
import org.ballcat.desensitize.text.TextDesensitizer;
import org.ballcat.desensitize.text.TextDesensitizerBuilder;
import org.ballcat.desensitize.text.config.RegexReplacementParams;
import org.ballcat.desensitize.text.config.RuleSpec;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LoggerFilterMessageConverterTest {

	private LoggerContext ctx;

	private ByteArrayOutputStream out;

	private OutputStreamAppender<ILoggingEvent> app;

	@BeforeEach
	void setUp() {
		// 引擎
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
		rules.put("com.allow", Boolean.TRUE);
		LoggerDesensitizationFilterHolder.set(new LoggerDesensitizationFilter(true, false, rules));

		// Logback
		this.ctx = new LoggerContext();
		PatternLayout layout = new PatternLayout();
		layout.setContext(this.ctx);
		layout.getDefaultConverterMap().put("dMsg", DesensitizeMessageConverter.class.getName());
		layout.setPattern("%dMsg%nopex%n");
		layout.start();
		LayoutWrappingEncoder<ILoggingEvent> enc = new LayoutWrappingEncoder<>();
		enc.setContext(this.ctx);
		enc.setLayout(layout);
		enc.setCharset(StandardCharsets.UTF_8);
		enc.start();
		this.out = new ByteArrayOutputStream();
		this.app = new OutputStreamAppender<>();
		this.app.setContext(this.ctx);
		this.app.setEncoder(enc);
		this.app.setOutputStream(this.out);
		this.app.start();
	}

	@AfterEach
	void tearDown() {
		try {
			if (this.app != null) {
				this.app.stop();
			}
		}
		catch (Exception ignored) {
		}
		try {
			if (this.ctx != null) {
				this.ctx.stop();
			}
		}
		catch (Exception ignored) {
		}
		TextDesensitizerHolder.set(null);
		LoggerDesensitizationFilterHolder.set(null);
	}

	@Test
	void allowed_logger_should_be_desensitized_but_denied_should_pass_through() {
		Logger allow = this.ctx.getLogger("com.allow.service.UserService");
		Logger deny = this.ctx.getLogger("com.deny.service.OrderService");
		allow.addAppender(this.app);
		deny.addAppender(this.app);

		allow.info("phone: 13877891234");
		deny.info("phone: 13900112233");

		this.app.stop();
		String s = new String(this.out.toByteArray(), StandardCharsets.UTF_8);
		assertTrue(s.contains("phone: 138****1234")); // 允许的被脱敏
		assertTrue(s.contains("phone: 13900112233")); // 拒绝的保持原文
		assertFalse(s.contains("13877891234"));
	}

}
