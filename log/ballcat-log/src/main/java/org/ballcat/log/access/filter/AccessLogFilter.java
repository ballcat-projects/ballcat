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
package org.ballcat.log.access.filter;

import org.ballcat.common.core.constant.MDCConstants;
import org.ballcat.common.core.request.wrapper.RepeatBodyRequestWrapper;
import org.ballcat.log.access.handler.AccessLogHandler;
import org.ballcat.log.util.LogUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author Hccake 2019/10/15 21:53
 */
@RequiredArgsConstructor
public class AccessLogFilter extends OncePerRequestFilter {

	private final AccessLogHandler<?> accessLogService;

	private final List<String> ignoreUrlPatterns;

	/**
	 * 针对需忽略的Url的规则匹配器
	 */
	private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

	/**
	 * URL 路径匹配的帮助类
	 */
	private static final UrlPathHelper URL_PATH_HELPER = new UrlPathHelper();

	/**
	 * Same contract as for {@code doFilter}, but guaranteed to be just invoked once per
	 * request within a single request thread. See {@link #shouldNotFilterAsyncDispatch()}
	 * for details.
	 * <p>
	 * Provides HttpServletRequest and HttpServletResponse arguments instead of the
	 * default ServletRequest and ServletResponse ones.
	 * @param request 请求信息
	 * @param response 请求体
	 * @param filterChain 过滤器链
	 */
	@Override
	@SuppressWarnings("java:S1181")
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		// 跳过部分忽略 url
		String lookupPathForRequest = URL_PATH_HELPER.getLookupPathForRequest(request);
		for (String ignoreUrlPattern : ignoreUrlPatterns) {
			if (ANT_PATH_MATCHER.match(ignoreUrlPattern, lookupPathForRequest)) {
				filterChain.doFilter(request, response);
				return;
			}
		}

		// 包装request，以保证可以重复读取body 但不对文件上传请求body进行处理
		HttpServletRequest requestWrapper;
		if (LogUtils.isMultipartContent(request)) {
			requestWrapper = request;
		}
		else {
			requestWrapper = new RepeatBodyRequestWrapper(request);
		}
		// 包装 response，便于重复获取 body
		ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

		// 开始时间
		Long startTime = System.currentTimeMillis();
		Throwable myThrowable = null;
		final String traceId = MDC.get(MDCConstants.TRACE_ID_KEY);
		try {
			filterChain.doFilter(requestWrapper, responseWrapper);
		}
		catch (Throwable throwable) {
			// 记录外抛异常
			myThrowable = throwable;
			throw throwable;
		}
		finally {
			// 这里抛BusinessException后会丢失traceId，需要重新设置
			if (StringUtils.isBlank(MDC.get(MDCConstants.TRACE_ID_KEY))) {
				MDC.put(MDCConstants.TRACE_ID_KEY, traceId);
			}
			// 结束时间
			Long endTime = System.currentTimeMillis();
			// 执行时长
			Long executionTime = endTime - startTime;
			// 记录在doFilter里被程序处理过后的异常，可参考
			// http://www.runoob.com/servlet/servlet-exception-handling.html
			Throwable throwable = (Throwable) requestWrapper.getAttribute("javax.servlet.error.exception");
			if (throwable != null) {
				myThrowable = throwable;
			}
			// 生产一个日志并记录
			try {
				accessLogService.handleLog(requestWrapper, responseWrapper, executionTime, myThrowable);
			}
			catch (Exception e) {
				logger.error("logging access_log error!", e);
			}
			// 重新写入数据到响应信息中
			responseWrapper.copyBodyToResponse();
		}
	}

}