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

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.classic.spi.ThrowableProxyUtil;
import ch.qos.logback.core.CoreConstants;

/**
 * 转换词：在保留原始堆栈与嵌套结构的前提下，仅对各层异常的 message 进行脱敏。 与 DesensitizeThrowableProxyConverter
 * 不同的是，会为异常添加一行空行，方便阅读。
 *
 * @see org.springframework.boot.logging.logback.ExtendedWhitespaceThrowableProxyConverter
 * @author Hccake
 */
public class DesensitizeExtendedWhitespaceThrowableProxyConverter extends DesensitizeThrowableProxyConverter {

	protected void extraData(StringBuilder builder, StackTraceElementProxy step) {
		ThrowableProxyUtil.subjoinPackagingData(builder, step);
	}

	protected void prepareLoggingEvent(ILoggingEvent event) {
	}

	@Override
	protected String throwableProxyToString(IThrowableProxy tp) {
		return CoreConstants.LINE_SEPARATOR + super.throwableProxyToString(tp) + CoreConstants.LINE_SEPARATOR;
	}

}
