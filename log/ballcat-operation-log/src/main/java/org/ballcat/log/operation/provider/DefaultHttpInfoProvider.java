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

package org.ballcat.log.operation.provider;

import javax.servlet.http.HttpServletRequest;

import org.ballcat.common.util.IpUtils;
import org.ballcat.log.operation.domain.OperationLogInfo;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class DefaultHttpInfoProvider implements HttpInfoProvider {

	@Override
	public OperationLogInfo.HttpInfo get() {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (attributes == null) {
			return null;
		}
		HttpServletRequest request = attributes.getRequest();
		OperationLogInfo.HttpInfo httpInfo = new OperationLogInfo.HttpInfo();
		httpInfo.setRequestUri(request.getRequestURI());
		httpInfo.setRequestMethod(request.getMethod());
		httpInfo.setClientIp(IpUtils.getIpAddr(request));
		httpInfo.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
		httpInfo.setReferer(request.getHeader(HttpHeaders.REFERER));
		return httpInfo;
	}

}
