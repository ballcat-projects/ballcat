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

package org.ballcat.apisignature;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.ballcat.common.model.result.ApiResult;
import org.ballcat.common.util.JsonUtils;
import org.ballcat.web.util.RepeatBodyRequestWrapper;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.UrlPathHelper;
import org.springframework.web.util.WebUtils;

/**
 * API 签名校验过滤器。
 *
 * @author hccake
 */
@Slf4j
@RequiredArgsConstructor
public class ApiSignatureFilter extends OncePerRequestFilter {

	private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

	private static final UrlPathHelper URL_PATH_HELPER = new UrlPathHelper();

	private final ApiSignatureProperties config;

	private final ApiKeyManager apiKeyManager;

	private final NonceStore nonceStore;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String lookupPathForRequest = URL_PATH_HELPER.getLookupPathForRequest(request);

		// 如果在排除 url 中，则直接跳过
		List<String> excludeUrlPattens = this.config.getExcludeUrlPattens();
		for (String excludeUrlPatten : excludeUrlPattens) {
			if (ANT_PATH_MATCHER.match(excludeUrlPatten, lookupPathForRequest)) {
				filterChain.doFilter(request, response);
				return;
			}
		}

		// 如果不在排除 url 中，且不在包含 url 规则中，也直接跳过
		List<String> includeUrlPattens = this.config.getIncludeUrlPattens();
		Optional<String> isInclude = includeUrlPattens.stream()
			.filter(x -> ANT_PATH_MATCHER.match(x, lookupPathForRequest))
			.findAny();
		if (!isInclude.isPresent()) {
			filterChain.doFilter(request, response);
			return;
		}

		// 校验时间戳，由于服务器时钟同步问题，时间戳的有效期校验为上下 5 分钟
		String timestamp = request.getHeader(this.config.getTimestampHeader());
		long timestampDiffThreshold = this.config.getTimestampDiffThreshold();
		if (StringUtils.isEmpty(timestamp) || !StringUtils.isNumeric(timestamp)
				|| Math.abs(System.currentTimeMillis() - Long.parseLong(timestamp)) > timestampDiffThreshold) {
			sendErrorResponse(response, "invalid timestamp.");
			return;
		}

		// 校验请求随机数, 如果随机数重复，说明请求重放
		String nonce = request.getHeader(this.config.getNonceHeader());
		if (nonce == null || nonce.length() != 32 || repeatedNonce(nonce)) {
			sendErrorResponse(response, "invalid nonce.");
			return;
		}

		// 校验 ACCESS_KEY
		String accessKey = request.getHeader(this.config.getAccessKeyHeader());
		if (StringUtils.isEmpty(accessKey)) {
			sendErrorResponse(response, "invalid access key.");
			return;
		}

		// 获取 ACCESS_KEY 对应的主体信息
		Object subject = getSubject(accessKey);
		if (subject == null) {
			sendErrorResponse(response, "invalid subject.");
			return;
		}

		// 获取对应的 SECRET_KEY
		String secretKey = getSecretKey(subject);
		if (StringUtils.isEmpty(accessKey)) {
			sendErrorResponse(response, "invalid access key.");
			return;
		}

		// 没有签名需要直接返回
		String signature = request.getHeader(this.config.getSignatureHeader());
		if (StringUtils.isEmpty(signature)) {
			sendErrorResponse(response, "invalid signature.");
			return;
		}

		// 拼接不包含域名的完整请求 url，需要包含 queryString
		String requestURI = request.getRequestURI();
		String queryString = request.getQueryString();
		String uriPrefix = this.config.getUriPrefix();
		if (uriPrefix != null) {
			requestURI = uriPrefix + requestURI;
		}

		// 根据请求参数生成服务端签名，和请求签名做比对
		String requestPayload;
		try {
			// 包装 request, 以便重复获取 body
			if (!(request instanceof RepeatBodyRequestWrapper)) {
				request = new RepeatBodyRequestWrapper(request);
			}
			requestPayload = getRequestPayload(request);
		}
		catch (Exception e) {
			log.error("generate signature error", e);
			sendErrorResponse(response, "generate signature error.");
			return;
		}

		// 生成服务端签名, 和请求签名做比对
		SignatureBuilder signatureBuilder = new SignatureBuilder().setHttpMethod(request.getMethod())
			.setRequestURI(requestURI)
			.setQueryString(queryString)
			.setRequestPayload(requestPayload)
			.setTimestamp(timestamp)
			.setNonce(nonce)
			.setAccessKey(accessKey)
			.setSecretKey(secretKey);
		String serverSignature;
		try {
			serverSignature = signatureBuilder.build();
		}
		catch (Exception ex) {
			log.error("generate signature error", ex);
			sendErrorResponse(response, "generate signature error.");
			return;
		}

		if (!signature.equalsIgnoreCase(serverSignature)) {
			sendErrorResponse(response, "invalid signature.");
			return;
		}

		try {
			SubjectHolder.setSubject(subject);
			filterChain.doFilter(request, response);
		}
		finally {
			SubjectHolder.clear();
		}
	}

	protected Object getSubject(String accessKey) {
		return this.apiKeyManager.getSubject(accessKey);
	}

	protected String getSecretKey(Object subject) {
		return this.apiKeyManager.getSecretKey(subject);
	}

	/**
	 * 检查随机串是否重复。
	 * @param nonce 随机串，32位
	 * @return 重复则返回 true。
	 */
	protected boolean repeatedNonce(String nonce) {
		long nonceTimeout = this.config.getNonceTimeout();
		TimeUnit nonceTimeoutUnit = this.config.getNonceTimeoutUnit();
		return !this.nonceStore.storeIfAbsent(nonce, nonceTimeout, nonceTimeoutUnit);
	}

	private static void sendErrorResponse(HttpServletResponse response, String errorMessage) throws IOException {
		log.warn("api signature verification failed：" + errorMessage);
		ApiResult<Void> apiResult = ApiResult.failed(401, errorMessage);
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.getWriter().write(JsonUtils.toJson(apiResult));
		response.setContentType("application/json;charset=UTF-8");
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
	}

	private static String getRequestPayload(HttpServletRequest request) throws UnsupportedEncodingException {
		RepeatBodyRequestWrapper wrapper = WebUtils.getNativeRequest(request, RepeatBodyRequestWrapper.class);
		Assert.notNull(wrapper, "get request body error.");
		if (wrapper.getCharacterEncoding() != null) {
			return new String(wrapper.getBodyByteArray(), wrapper.getCharacterEncoding());
		}
		else {
			return new String(wrapper.getBodyByteArray(), Charset.defaultCharset());
		}
	}

}
