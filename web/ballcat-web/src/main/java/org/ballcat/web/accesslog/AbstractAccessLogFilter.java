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

import org.apache.commons.lang3.StringUtils;
import org.ballcat.common.core.constant.MDCConstants;
import org.ballcat.common.core.request.wrapper.RepeatBodyRequestWrapper;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * @author Hccake 2019/10/15 21:53
 */
public abstract class AbstractAccessLogFilter extends OncePerRequestFilter implements Ordered {

	public static final int DEFAULT_MAX_BODY_LENGTH = 256;

	private int maxBodyLength = DEFAULT_MAX_BODY_LENGTH;

	private int order = 0;

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
		AccessLogSettings accessLogSettings = getAccessLogSettings(request);

		if (!accessLogSettings.enabled()) {
			filterChain.doFilter(request, response);
			return;
		}

		HttpServletRequest requestToUse;
		if (accessLogSettings.shouldRecordRequestBody()) {
			// 包装 request，以保证可以重复读取body
			// spring 提供的 ContentCachingRequestWrapper，在 body 没有被程序使用时，获取到的 body 缓存为空
			// 且对于 form data，会混淆 payload 和 query string
			requestToUse = new RepeatBodyRequestWrapper(request);
		}
		else {
			requestToUse = request;
		}

		HttpServletResponse responseToUse;
		if (accessLogSettings.shouldRecordResponseBody()) {
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
			beforeRequest(requestToUse, accessLogSettings);
		}
		catch (Exception e) {
			logger.error("[Access Log] process before request error", e);
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
				afterRequest(requestToUse, responseToUse, executionTime, myThrowable, accessLogSettings);
			}
			catch (Exception e) {
				logger.error("[Access Log] process after request error, handler: %s", e);
			}

			// 重新写入数据到响应信息中
			if (responseToUse instanceof ContentCachingResponseWrapper) {
				((ContentCachingResponseWrapper) responseToUse).copyBodyToResponse();
			}
		}
	}

	@Nullable
	protected String getRequestBody(HttpServletRequest request) {
		RepeatBodyRequestWrapper wrapper = WebUtils.getNativeRequest(request, RepeatBodyRequestWrapper.class);
		if (wrapper == null) {
			return null;
		}
		return getMessagePayload(wrapper.getBodyByteArray(), wrapper.getCharacterEncoding());
	}

	@Nullable
	protected String getResponseBody(HttpServletResponse response) {
		ContentCachingResponseWrapper wrapper = WebUtils.getNativeResponse(response,
				ContentCachingResponseWrapper.class);
		if (wrapper == null) {
			return null;
		}
		return getMessagePayload(wrapper.getContentAsByteArray(), wrapper.getCharacterEncoding());
	}

	@Nullable
	protected String getMessagePayload(byte[] buf, String characterEncoding) {
		if (buf.length > 0) {
			int length = Math.min(buf.length, getMaxBodyLength());
			try {
				return new String(buf, 0, length, characterEncoding);
			}
			catch (UnsupportedEncodingException ex) {
				return "[unknown]";
			}
		}
		return null;
	}

	public void setMaxBodyLength(int maxBodyLength) {
		Assert.isTrue(maxBodyLength >= 0, "'maxBodyLength' must be greater than or equal to 0");
		this.maxBodyLength = maxBodyLength;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	@Override
	public int getOrder() {
		return this.order;
	}

	protected int getMaxBodyLength() {
		return this.maxBodyLength;
	}

	protected boolean shouldLog(HttpServletRequest request) {
		return true;
	}

	protected abstract AccessLogSettings getAccessLogSettings(HttpServletRequest request);

	protected abstract void beforeRequest(HttpServletRequest request, AccessLogSettings accessLogSettings);

	protected abstract void afterRequest(HttpServletRequest request, HttpServletResponse response, Long executionTime,
			Throwable throwable, AccessLogSettings accessLogSettings);

}
