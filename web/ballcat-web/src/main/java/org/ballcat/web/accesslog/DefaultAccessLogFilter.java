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

import cn.hutool.core.annotation.AnnotationUtil;
import lombok.extern.slf4j.Slf4j;
import org.ballcat.common.util.IpUtils;
import org.ballcat.web.accesslog.annotation.AccessLogging;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
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

	/**
	 * 针对需忽略的Url的规则匹配器
	 */
	private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

	/**
	 * URL 路径匹配的帮助类
	 */
	private static final UrlPathHelper URL_PATH_HELPER = new UrlPathHelper();

	private final List<AccessLogRule> logRules;

	private final RequestMappingHandlerMapping requestMappingHandlerMapping;

	public DefaultAccessLogFilter(List<AccessLogRule> logRules,
								  RequestMappingHandlerMapping requestMappingHandlerMapping) {
		this.logRules = logRules;
		this.requestMappingHandlerMapping = requestMappingHandlerMapping;
	}

	@Override
	protected boolean shouldLog(HttpServletRequest request) {
		return log.isDebugEnabled();
	}

	@Override
	protected AccessLogSettings getAccessLogSettings(HttpServletRequest request) {
		String lookupPathForRequest = URL_PATH_HELPER.getLookupPathForRequest(request);

		for (AccessLogRule logRule : logRules) {
			if (ANT_PATH_MATCHER.match(logRule.getUrlPattern(), lookupPathForRequest)) {
				return AccessLogSettings.builder()
						.enabled(!logRule.isIgnore())
						.includeQueryString(logRule.isIncludeQueryString())
						.includeRequestBody(logRule.isIncludeRequestBody())
						.includeResponseBody(logRule.isIncludeResponseBody())
						.build();
			}
		}

		return AccessLogSettings.IGNORED;
	}

	@Override
	protected void beforeRequest(HttpServletRequest request, AccessLogSettings accessLogSettings) {
		StringBuilder msg = new StringBuilder();
		msg.append("Before request [");
		msg.append(request.getMethod()).append(' ');
		msg.append(request.getRequestURI());

		if (accessLogSettings.shouldRecordQueryString()) {
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
								Throwable throwable, AccessLogSettings accessLogSettings) {
		StringBuilder msg = new StringBuilder();
		msg.append("After request [");
		msg.append(request.getMethod()).append(' ');
		msg.append(request.getRequestURI());

		if (accessLogSettings.shouldRecordQueryString()) {
			String queryString = request.getQueryString();
			if (queryString != null) {
				msg.append('?').append(queryString);
			}
		}

		String ipAddr = IpUtils.getIpAddr(request);
		msg.append(", client=").append(ipAddr);

		if (accessLogSettings.shouldRecordRequestBody() && (this.shouldRecordRequestBodyByAnnotation(request))) {
				String payload = getRequestBody(request);
				if (payload != null) {
					msg.append(", request body=").append(payload);
				}

		}

		if (accessLogSettings.shouldRecordResponseBody()) {
			String payload = getResponseBody(response);
			if (payload != null) {
				msg.append(", response body=").append(payload);
			}
		}

		msg.append("]");

		log.debug(msg.toString());
	}

	/**
	 * 根据注解判断是否记录请求体
	 * @param request 请求信息
	 * @return 记录返回 true，否则返回 false
	 */
	private boolean shouldRecordRequestBodyByAnnotation(HttpServletRequest request) {

		try {
			HandlerExecutionChain handlerExecutionChain = requestMappingHandlerMapping.getHandler(request);
			if (handlerExecutionChain == null) {
				return false;
			}

			HandlerMethod handlerMethod = (HandlerMethod) handlerExecutionChain.getHandler();

			AccessLogging annotation = AnnotationUtil.getAnnotation(handlerMethod.getMethod(), AccessLogging.class);

			if (annotation != null) {
				return annotation.recordRequestBody();
			}

			return true;
		} catch (Exception e) {
			return false;
		}

	}

}
