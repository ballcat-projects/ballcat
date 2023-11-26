/*
 * Copyright 2023 the original author or authors.
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

import lombok.extern.slf4j.Slf4j;
import org.ballcat.common.util.IpUtils;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 默认的访问日志过滤器，使用 log 输出
 *
 * @author Hccake
 */
@Slf4j
public class DefaultAccessLogFilter extends AbstractAccessLogFilter {

	public DefaultAccessLogFilter(AccessLogRecordOptions defaultRecordOptions, List<AccessLogRule> logRules) {
		super(defaultRecordOptions, logRules);
	}

	@Override
	protected boolean shouldLog(HttpServletRequest request) {
		return log.isDebugEnabled();
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

		log.debug(msg.toString());
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
			String payload = getRequestBody(request);
			if (payload != null) {
				msg.append(", request body=").append(payload);
			}
		}

		if (recordOptions.isIncludeResponseBody()) {
			String payload = getResponseBody(response);
			if (payload != null) {
				msg.append(", response body=").append(payload);
			}
		}

		msg.append("]");

		log.debug(msg.toString());
	}

}
