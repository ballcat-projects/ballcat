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

package org.ballcat.springsecurity.oauth2.server.authorization.web.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ballcat.common.model.result.ApiResult;
import org.ballcat.common.model.result.SystemResultCode;
import org.ballcat.common.util.JsonUtils;
import org.ballcat.springsecurity.oauth2.ScopeNames;
import org.ballcat.springsecurity.util.PasswordUtils;
import org.ballcat.web.util.ModifyParameterRequestWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import static org.ballcat.springsecurity.oauth2.server.authorization.authentication.OAuth2AuthenticationProviderUtils.getAuthenticatedClientElseThrowInvalidClient;

/**
 * 前端传递过来的加密密码，需要在登录之前先解密
 *
 * @author Hccake 2019/9/28 16:57
 */
@Slf4j
@RequiredArgsConstructor
public class LoginPasswordDecoderFilter extends OncePerRequestFilter {

	private final RequestMatcher requestMatcher;

	private final String passwordSecretKey;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		if (!this.requestMatcher.matches(request)) {
			filterChain.doFilter(request, response);
			return;
		}

		// 未配置密码密钥时，直接跳过
		if (this.passwordSecretKey == null) {
			log.warn("passwordSecretKey not configured, skip password decoder");
			filterChain.doFilter(request, response);
			return;
		}

		// 非密码模式下，直接跳过
		String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
		if (!AuthorizationGrantType.PASSWORD.getValue().equals(grantType)) {
			filterChain.doFilter(request, response);
			return;
		}

		// 获取当前客户端
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		OAuth2ClientAuthenticationToken clientPrincipal = getAuthenticatedClientElseThrowInvalidClient(authentication);
		RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();

		// 测试客户端密码不加密，直接跳过（swagger 或 postman测试时使用）
		if (registeredClient != null && registeredClient.getScopes().contains(ScopeNames.SKIP_PASSWORD_DECODE)) {
			filterChain.doFilter(request, response);
			return;
		}

		// 解密前台加密后的密码
		Map<String, String[]> parameterMap = new HashMap<>(request.getParameterMap());
		String passwordAes = request.getParameter(OAuth2ParameterNames.PASSWORD);

		try {
			String password = PasswordUtils.decodeAES(passwordAes, this.passwordSecretKey);
			parameterMap.put(OAuth2ParameterNames.PASSWORD, new String[] { password });
		}
		catch (Exception e) {
			log.error("[doFilterInternal] password decode aes error，passwordAes: {}，passwordSecretKey: {}", passwordAes,
					this.passwordSecretKey, e);
			response.setHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
			response.setStatus(HttpStatus.BAD_REQUEST.value());
			ApiResult<String> apiResult = ApiResult.failed(SystemResultCode.UNAUTHORIZED, "用户名或密码错误！");
			response.getWriter().write(JsonUtils.toJson(apiResult));
			return;
		}

		// SpringSecurity 默认从ParameterMap中获取密码参数
		// 由于原生的request中对parameter加锁了，无法修改，所以使用包装类
		filterChain.doFilter(new ModifyParameterRequestWrapper(request, parameterMap), response);
	}

}
