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
import java.util.Arrays;
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
import org.ballcat.desensitize.text.config.SlideMaskParams;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Logback ConversionRule 适配层测试： - %dMsg：仅对 formatted message 脱敏 - %dEx：仅对异常 message
 * 脱敏，堆栈与嵌套结构保持
 *
 * @author Hccake
 */
class LogbackAdapterTest {

	private LoggerContext context;

	private Logger logger;

	private ByteArrayOutputStream out;

	private OutputStreamAppender<ILoggingEvent> appender;

	@BeforeEach
	void setUp() {
		// 构建引擎并注入
		RuleSpec phone = new RuleSpec().setName("phone")
			.setPrefixes(Arrays.asList("phone", "mobile", "手机号"))
			.setValuePattern("(?<!\\d)(1[3-9]\\d)(\\d{4})(\\d{4})(?!\\d)")
			.setDesensitizeType(DesensitizeType.REGEX_REPLACEMENT)
			.setRegex(new RegexReplacementParams().setReplacement("$1****$3"));
		RuleSpec bank = new RuleSpec().setName("bank-card")
			.setPrefixes(Arrays.asList("银行卡", "bankCard", "bank"))
			.setValuePattern("\\d{16,19}")
			.setMatchFromStart(true)
			.setDesensitizeType(DesensitizeType.SLIDE_MASK)
			.setSlide(new SlideMaskParams(0, 4, "*", false));
		TextDesensitizer engine = new TextDesensitizerBuilder().addRule(phone).addRule(bank).build();
		TextDesensitizerHolder.set(engine);

		// 过滤器：root=false，com.allow=true
		Map<String, Boolean> rules = new HashMap<>();
		rules.put("test", Boolean.TRUE);
		LoggerDesensitizationFilterHolder.set(new LoggerDesensitizationFilter(true, false, rules));

		// LoggerContext & Logger
		this.context = new LoggerContext();
		this.logger = this.context.getLogger("test");
		this.logger.setAdditive(false);

		this.out = new ByteArrayOutputStream();

		this.appender = new OutputStreamAppender<>();
		this.appender.setContext(this.context);
		this.appender.setOutputStream(this.out);
	}

	@AfterEach
	void tearDown() {
		if (this.appender != null) {
			this.appender.stop();
		}
		if (this.context != null) {
			this.context.stop();
		}
		TextDesensitizerHolder.set(null);
	}

	private void attachPattern(String pattern) {
		PatternLayout layout = new PatternLayout();
		layout.setContext(this.context);
		// 注册转换词
		layout.getDefaultConverterMap().put("dMsg", DesensitizeMessageConverter.class.getName());
		layout.getDefaultConverterMap().put("dEx", DesensitizeThrowableProxyConverter.class.getName());
		layout.setPattern(pattern);
		layout.start();

		LayoutWrappingEncoder<ILoggingEvent> enc = new LayoutWrappingEncoder<>();
		enc.setContext(this.context);
		enc.setLayout(layout);
		enc.setCharset(StandardCharsets.UTF_8);
		enc.start();

		this.appender.setEncoder(enc);
		this.appender.start();
		this.logger.addAppender(this.appender);
	}

	private String readOutputAndReset() {
		try {
			this.appender.stop();
		}
		catch (Exception ignored) {
		}
		String s = new String(this.out.toByteArray(), StandardCharsets.UTF_8);
		this.out.reset();
		return s;
	}

	@Test
	void testdMsgPhoneAndBank() {
		attachPattern("%dMsg%nopex%n");
		this.logger.info("phone: 13877891234 bank=[6222021234567890123]");
		String outStr = readOutputAndReset();
		assertTrue(outStr.contains("phone: 138****1234"));
		assertTrue(outStr.contains("bank=[***************0123]"));
		assertFalse(outStr.contains("13877891234"));
		assertFalse(outStr.contains("6222021234567890123"));
	}

	@Test
	void testdExNestedExceptionMessages() {
		attachPattern("%dEx%n");
		Exception cause = new IllegalArgumentException("bank: 6222021234567890123");
		Exception ex = new RuntimeException("phone: 13877891234", cause);
		this.logger.error("oops", ex);
		String outStr = readOutputAndReset();

		// 顶层与嵌套异常的 message 均应被脱敏
		assertTrue(outStr.contains("RuntimeException: phone: 138****1234"));
		assertTrue(outStr.contains("Caused by: java.lang.IllegalArgumentException: bank: ***************0123"));
		// 堆栈行应存在，不应被整体清空
		assertTrue(outStr.contains("\tat "));
	}

}
