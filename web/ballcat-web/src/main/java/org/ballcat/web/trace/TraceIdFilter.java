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

package org.ballcat.web.trace;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import org.ballcat.common.core.constant.MDCConstants;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * TraceId 过滤器
 * <p>
 * 利用 Slf4J 的 MDC 功能，将 traceId 放入 MDC 中，方便日志打印
 *
 * @author Hccake 2020/5/25 17:35
 */
@RequiredArgsConstructor
public class TraceIdFilter extends OncePerRequestFilter {

	private final String traceIdHeaderName;

	private final TraceIdGenerator traceIdGenerator;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// 先获取请求头中的 traceId，如果没有，则生成一个
		String traceId = request.getHeader(this.traceIdHeaderName);
		if (traceId == null || traceId.isEmpty()) {
			traceId = this.traceIdGenerator.generate();
		}

		MDC.put(MDCConstants.TRACE_ID_KEY, traceId);
		try {
			// 响应头中添加 traceId 参数，方便排查问题
			response.setHeader(this.traceIdHeaderName, traceId);
			filterChain.doFilter(request, response);
		}
		finally {
			MDC.remove(MDCConstants.TRACE_ID_KEY);
		}
	}

}
