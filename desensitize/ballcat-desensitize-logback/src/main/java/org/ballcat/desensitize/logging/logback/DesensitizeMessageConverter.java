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

import ch.qos.logback.classic.pattern.MessageConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.ballcat.desensitize.text.TextDesensitizer;

/**
 * 对 formatted message 脱敏处理。
 *
 * @author Hccake
 */
public class DesensitizeMessageConverter extends MessageConverter {

	@Override
	public String convert(ILoggingEvent event) {
		LoggerDesensitizationFilter filter = LoggerDesensitizationFilterHolder.get();
		if (filter == null) {
			return event.getFormattedMessage();
		}
		TextDesensitizer engine = TextDesensitizerHolder.get();
		if (engine == null) {
			return event.getFormattedMessage();
		}

		String loggerName = event.getLoggerName();
		if (!filter.allow(loggerName)) {
			return event.getFormattedMessage();
		}

		String msg = event.getFormattedMessage();
		if (msg == null || msg.isEmpty()) {
			return msg;
		}
		return engine.desensitize(msg);
	}

}
