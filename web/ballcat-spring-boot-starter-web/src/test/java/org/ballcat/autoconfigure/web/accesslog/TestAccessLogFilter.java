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

package org.ballcat.autoconfigure.web.accesslog;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.Getter;
import org.ballcat.web.accesslog.AbstractAccessLogFilter;
import org.ballcat.web.accesslog.AccessLogRecordOptions;
import org.ballcat.web.accesslog.AccessLogRule;

@Getter
public class TestAccessLogFilter extends AbstractAccessLogFilter {

	private HttpInfo httpInfo;

	public TestAccessLogFilter(AccessLogRecordOptions defaultRecordOptions, List<AccessLogRule> logRules) {
		super(defaultRecordOptions, logRules);
	}

	@Override
	protected void beforeRequest(HttpServletRequest request, AccessLogRecordOptions recordOptions) {
		this.httpInfo = new HttpInfo();
		if (recordOptions.isIncludeQueryString()) {
			this.httpInfo.setQueryString(request.getQueryString());
		}
	}

	@Override
	protected void afterRequest(HttpServletRequest request, HttpServletResponse response, Long executionTime,
			Throwable throwable, AccessLogRecordOptions recordOptions) {
		if (recordOptions.isIncludeRequestBody()) {
			String payload = getRequestBody(request, recordOptions.getMaxRequestBodyLength());
			if (payload != null) {
				this.httpInfo.setRequestBody(payload);
			}
		}

		if (recordOptions.isIncludeResponseBody()) {
			String payload = getResponseBody(response, recordOptions.getMaxResponseBodyLength());
			if (payload != null) {
				this.httpInfo.setResponseBody(payload);
			}
		}
	}

	public void clearHttpInfo() {
		this.httpInfo = null;
	}

}
