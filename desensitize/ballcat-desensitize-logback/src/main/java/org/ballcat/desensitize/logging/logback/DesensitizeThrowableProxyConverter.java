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

import ch.qos.logback.classic.pattern.ThrowableProxyConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.ThrowableProxyUtil;
import ch.qos.logback.core.CoreConstants;
import org.ballcat.desensitize.text.TextDesensitizer;

/**
 * 转换词：在保留原始堆栈与嵌套结构的前提下，仅对各层异常的 message 进行脱敏。
 *
 * @author Hccake
 */
public class DesensitizeThrowableProxyConverter extends ThrowableProxyConverter {

	private static final ThreadLocal<Boolean> TL_ALLOW = new ThreadLocal<>();

	@Override
	public String convert(ILoggingEvent event) {
		LoggerDesensitizationFilter filter = LoggerDesensitizationFilterHolder.get();
		boolean allow = filter != null && filter.allow(event.getLoggerName());
		try {
			TL_ALLOW.set(allow);
			return super.convert(event);
		}
		finally {
			TL_ALLOW.remove();
		}
	}

	@Override
	protected String throwableProxyToString(IThrowableProxy tp) {
		StringBuilder sb = new StringBuilder(2048);
		this.recursiveAppend(sb, null, 1, tp);
		return sb.toString();
	}

	private void recursiveAppend(StringBuilder sb, String prefix, int indent, IThrowableProxy tp) {
		if (tp != null) {
			this.subjoinFirstLine(sb, prefix, indent, tp);
			sb.append(CoreConstants.LINE_SEPARATOR);
			this.subjoinSTEPArray(sb, indent, tp);
			IThrowableProxy[] suppressed = tp.getSuppressed();
			if (suppressed != null) {
				for (IThrowableProxy current : suppressed) {
					this.recursiveAppend(sb, "Suppressed: ", indent + 1, current);
				}
			}
			this.recursiveAppend(sb, "Caused by: ", indent, tp.getCause());
		}
	}

	private void subjoinFirstLine(StringBuilder buf, String prefix, int indent, IThrowableProxy tp) {
		ThrowableProxyUtil.indent(buf, indent - 1);
		if (prefix != null) {
			buf.append(prefix);
		}
		this.subjoinExceptionMessage(buf, tp);
	}

	private void subjoinExceptionMessage(StringBuilder buf, IThrowableProxy tp) {
		String msg = getMsg(tp);
		buf.append(tp.getClassName()).append(": ").append(msg);
	}

	private static String getMsg(IThrowableProxy tp) {
		String raw = tp.getMessage();

		Boolean allow = TL_ALLOW.get();
		if (!Boolean.TRUE.equals(allow)) {
			return raw;
		}

		TextDesensitizer textDesensitizer = TextDesensitizerHolder.get();
		if (textDesensitizer == null) {
			return raw;
		}

		return textDesensitizer.desensitize(raw);
	}

}
