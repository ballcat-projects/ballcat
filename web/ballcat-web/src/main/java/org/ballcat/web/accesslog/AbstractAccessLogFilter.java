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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.ballcat.common.core.constant.MDCConstants;
import org.ballcat.web.util.RepeatBodyRequestWrapper;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.lang.Nullable;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.ServletRequestPathUtils;
import org.springframework.web.util.UrlPathHelper;
import org.springframework.web.util.WebUtils;

/**
 * @author Hccake 2019/10/15 21:53
 */
public abstract class AbstractAccessLogFilter extends OncePerRequestFilter implements AccessLogFilter, Ordered {

	/**
	 * 针对需忽略的Url的规则匹配器
	 */
	private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

	/**
	 * URL 路径匹配的帮助类
	 */
	private static final UrlPathHelper URL_PATH_HELPER = new UrlPathHelper();

	public static final int DEFAULT_MAX_BODY_LENGTH = 256;

	private int order = 0;

	private final AccessLogRecordOptions defaultRecordOptions;

	private final List<AccessLogRule> logRules;

	protected AbstractAccessLogFilter(AccessLogRecordOptions defaultRecordOptions, List<AccessLogRule> logRules) {
		Assert.notNull(defaultRecordOptions, "default record options cannot be null!");
		this.defaultRecordOptions = defaultRecordOptions;
		this.logRules = logRules;
	}

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

		// 不需要记录直接跳出
		if (!shouldLog(request)) {
			filterChain.doFilter(request, response);
			return;
		}

		// 根据当前请求获取对应的日志记录规则
		AccessLogRecordOptions recordOptions = getRecordOptions(request);

		if (recordOptions.isIgnored()) {
			filterChain.doFilter(request, response);
			return;
		}

		HttpServletRequest requestToUse;
		if (recordOptions.isIncludeRequestBody()) {
			// 避免由于路由信息缺失，导致无法获取到请求目标的执行方法
			if (!ServletRequestPathUtils.hasParsedRequestPath(request)) {
				ServletRequestPathUtils.parseAndCache(request);
			}

			// 包装 request，以保证可以重复读取body
			// spring 提供的 ContentCachingRequestWrapper，在 body 没有被程序使用时，获取到的 body 缓存为空
			// 且对于 form data，会混淆 payload 和 query string
			requestToUse = new RepeatBodyRequestWrapper(request);
		}
		else {
			requestToUse = request;
		}

		HttpServletResponse responseToUse;
		if (recordOptions.isIncludeResponseBody()) {
			// 包装 response，便于重复获取 body
			responseToUse = new ContentCachingResponseWrapper(response);
		}
		else {
			responseToUse = response;
		}

		// 开始时间
		Long startTime = System.currentTimeMillis();
		Throwable myThrowable = null;
		final String traceId = MDC.get(MDCConstants.TRACE_ID_KEY);

		// 请求前处理
		try {
			beforeRequest(requestToUse, recordOptions);
		}
		catch (Exception e) {
			this.logger.error("[Access Log] process before request error", e);
		}

		try {
			filterChain.doFilter(requestToUse, responseToUse);
		}
		catch (Throwable throwable) {
			// 记录外抛异常
			myThrowable = throwable;
			throw throwable;
		}
		finally {
			// 这里抛 BusinessException 后会丢失 traceId，需要重新设置
			if (StringUtils.isBlank(MDC.get(MDCConstants.TRACE_ID_KEY))) {
				MDC.put(MDCConstants.TRACE_ID_KEY, traceId);
			}
			// 结束时间
			Long endTime = System.currentTimeMillis();
			// 执行时长
			Long executionTime = endTime - startTime;
			// 记录在doFilter里被程序处理过后的异常，可参考
			// http://www.runoob.com/servlet/servlet-exception-handling.html
			Throwable throwable = (Throwable) requestToUse.getAttribute("javax.servlet.error.exception");
			if (throwable != null) {
				myThrowable = throwable;
			}

			// 生产一个日志并记录
			try {
				afterRequest(requestToUse, responseToUse, executionTime, myThrowable, recordOptions);
			}
			catch (Exception e) {
				this.logger.error("[Access Log] process after request error, handler: %s", e);
			}

			// 重新写入数据到响应信息中
			if (responseToUse instanceof ContentCachingResponseWrapper) {
				((ContentCachingResponseWrapper) responseToUse).copyBodyToResponse();
			}
		}
	}

	@Nullable
	protected String getRequestBody(HttpServletRequest request, int maxLength) {
		RepeatBodyRequestWrapper wrapper = WebUtils.getNativeRequest(request, RepeatBodyRequestWrapper.class);
		if (wrapper == null) {
			return null;
		}
		if (wrapper.getCharacterEncoding() != null) {
			return getMessagePayload(wrapper.getBodyByteArray(), maxLength, wrapper.getCharacterEncoding());
		}
		else {
			return getMessagePayload(wrapper.getBodyByteArray(), maxLength, Charset.defaultCharset().name());
		}
	}

	@Nullable
	protected String getResponseBody(HttpServletResponse response, int maxLength) {
		ContentCachingResponseWrapper wrapper = WebUtils.getNativeResponse(response,
				ContentCachingResponseWrapper.class);
		if (wrapper == null) {
			return null;
		}
		return getMessagePayload(wrapper.getContentAsByteArray(), maxLength, Charset.defaultCharset().name());
	}

	@Nullable
	protected String getMessagePayload(byte[] buf, int maxLength, String characterEncoding) {
		if (buf.length > 0) {
			try {
				if (maxLength < 0) {
					return new String(buf, characterEncoding);
				}
				else if (maxLength == 0) {
					return "";
				}
				else {
					return new String(buf, 0, Math.min(buf.length, maxLength), characterEncoding);
				}
			}
			catch (UnsupportedEncodingException ex) {
				return "[unknown]";
			}
		}
		return null;
	}

	protected AccessLogRecordOptions getRecordOptions(HttpServletRequest request) {
		if (CollectionUtils.isEmpty(this.logRules)) {
			return this.defaultRecordOptions;
		}

		String lookupPathForRequest = URL_PATH_HELPER.getLookupPathForRequest(request);

		for (AccessLogRule logRule : this.logRules) {
			if (ANT_PATH_MATCHER.match(logRule.getUrlPattern(), lookupPathForRequest)) {
				return logRule.getOptions();
			}
		}

		return this.defaultRecordOptions;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	@Override
	public int getOrder() {
		return this.order;
	}

	protected boolean shouldLog(HttpServletRequest request) {
		return true;
	}

	protected abstract void beforeRequest(HttpServletRequest request, AccessLogRecordOptions recordOptions);

	protected abstract void afterRequest(HttpServletRequest request, HttpServletResponse response, Long executionTime,
			Throwable throwable, AccessLogRecordOptions recordOptions);

}
