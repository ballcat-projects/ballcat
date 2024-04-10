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

package org.ballcat.web.accesslog;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.ballcat.common.util.IpUtils;
import org.slf4j.event.Level;

/**
 * 默认的访问日志过滤器，使用 log 输出
 *
 * @author Hccake
 */
@Getter
@Setter
@Slf4j
public class DefaultAccessLogFilter extends AbstractAccessLogFilter {

	private Level logLevel;

	public DefaultAccessLogFilter(AccessLogRecordOptions defaultRecordOptions, List<AccessLogRule> logRules,
			Level logLevel) {
		super(defaultRecordOptions, logRules);
		this.logLevel = logLevel != null ? logLevel : Level.DEBUG;
	}

	@Override
	protected boolean shouldLog(HttpServletRequest request) {
		switch (this.logLevel) {
			case TRACE:
				return log.isTraceEnabled();
			case DEBUG:
				return log.isDebugEnabled();
			case INFO:
				return log.isInfoEnabled();
			case WARN:
				return log.isWarnEnabled();
			case ERROR:
				return log.isErrorEnabled();
			default:
				return false;
		}
	}

	@Override
	protected void beforeRequest(HttpServletRequest request, AccessLogRecordOptions recordOptions) {
		StringBuilder msg = new StringBuilder();
		msg.append("Before request [");
		msg.append(request.getMethod()).append(' ');
		msg.append(request.getRequestURI());

		if (recordOptions.isIncludeQueryString()) {
			String queryString = request.getQueryString();
			if (queryString != null) {
				msg.append('?').append(queryString);
			}
		}

		String ipAddr = IpUtils.getIpAddr(request);
		msg.append(", client=").append(ipAddr);

		msg.append("]");

		writeLogWithSpecialLevel(msg);
	}

	@Override
	protected void afterRequest(HttpServletRequest request, HttpServletResponse response, Long executionTime,
			Throwable throwable, AccessLogRecordOptions recordOptions) {
		StringBuilder msg = new StringBuilder();
		msg.append("After request [");
		msg.append(request.getMethod()).append(' ');
		msg.append(request.getRequestURI());

		if (recordOptions.isIncludeQueryString()) {
			String queryString = request.getQueryString();
			if (queryString != null) {
				msg.append('?').append(queryString);
			}
		}

		String ipAddr = IpUtils.getIpAddr(request);
		msg.append(", client=").append(ipAddr);

		if (recordOptions.isIncludeRequestBody()) {
			String payload = getRequestBody(request, recordOptions.getMaxRequestBodyLength());
			if (payload != null) {
				msg.append(", request body=").append(payload);
			}
		}

		if (recordOptions.isIncludeResponseBody()) {
			String payload = getResponseBody(response, recordOptions.getMaxResponseBodyLength());
			if (payload != null) {
				msg.append(", response body=").append(payload);
			}
		}

		msg.append("]");

		writeLogWithSpecialLevel(msg);
	}

	/**
	 * 根据指定的日志级别进行日志输出。
	 */
	private void writeLogWithSpecialLevel(StringBuilder msg) {
		switch (this.logLevel) {
			case TRACE:
				log.trace(msg.toString());
				break;
			case DEBUG:
				log.debug(msg.toString());
				break;
			case INFO:
				log.info(msg.toString());
				break;
			case WARN:
				log.warn(msg.toString());
				break;
			case ERROR:
				log.error(msg.toString());
				break;
		}
	}

}
