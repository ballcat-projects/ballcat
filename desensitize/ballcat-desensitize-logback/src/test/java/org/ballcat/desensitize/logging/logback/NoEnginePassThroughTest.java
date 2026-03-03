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

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.core.OutputStreamAppender;
import ch.qos.logback.core.encoder.LayoutWrappingEncoder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class NoEnginePassThroughTest {

	@Test
	void desMsg_should_pass_when_engine_null() {
		TextDesensitizerHolder.set(null);
		LoggerContext ctx = new LoggerContext();
		Logger logger = ctx.getLogger("t");
		PatternLayout layout = new PatternLayout();
		layout.setContext(ctx);
		layout.getDefaultConverterMap().put("dMsg", DesensitizeMessageConverter.class.getName());
		layout.setPattern("%dMsg%nopex%n");
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

		String msg = "phone: 13877891234";
		logger.info(msg);
		app.stop();
		String s = new String(out.toByteArray(), StandardCharsets.UTF_8);
		assertTrue(s.contains(msg));
	}

}
