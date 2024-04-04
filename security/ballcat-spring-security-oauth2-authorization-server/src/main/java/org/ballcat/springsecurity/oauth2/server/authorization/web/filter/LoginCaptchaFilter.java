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

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import org.ballcat.common.model.result.ApiResult;
import org.ballcat.common.model.result.SystemResultCode;
import org.ballcat.common.util.JsonUtils;
import org.ballcat.security.captcha.CaptchaValidateResult;
import org.ballcat.security.captcha.CaptchaValidator;
import org.ballcat.springsecurity.oauth2.ScopeNames;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import static org.ballcat.springsecurity.oauth2.server.authorization.authentication.OAuth2AuthenticationProviderUtils.getAuthenticatedClientElseThrowInvalidClient;

/**
 * @author Hccake 2021/1/11
 *
 */
@RequiredArgsConstructor
public class LoginCaptchaFilter extends OncePerRequestFilter {

	private final RequestMatcher requestMatcher;

	private final CaptchaValidator captchaValidator;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		if (!this.requestMatcher.matches(request)) {
			filterChain.doFilter(request, response);
			return;
		}

		// 只对 password 的 grant_type 进行拦截处理
		String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
		if (!AuthorizationGrantType.PASSWORD.getValue().equals(grantType)) {
			filterChain.doFilter(request, response);
			return;
		}

		// 获取当前客户端
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		OAuth2ClientAuthenticationToken clientPrincipal = getAuthenticatedClientElseThrowInvalidClient(authentication);
		RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();

		// 测试客户端 跳过验证码（swagger 或 postman测试时使用）
		if (registeredClient != null && registeredClient.getScopes().contains(ScopeNames.SKIP_CAPTCHA)) {
			filterChain.doFilter(request, response);
			return;
		}

		CaptchaValidateResult captchaValidateResult = this.captchaValidator.validate(request);
		if (captchaValidateResult.isSuccess()) {
			filterChain.doFilter(request, response);
		}
		else {
			response.setHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			ApiResult<String> apiResult = ApiResult.failed(SystemResultCode.UNAUTHORIZED,
					StringUtils.hasText(captchaValidateResult.getMessage()) ? captchaValidateResult.getMessage()
							: "Captcha code error");
			response.getWriter().write(JsonUtils.toJson(apiResult));
		}

	}

}
