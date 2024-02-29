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

package org.ballcat.springsecurity.web.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.ballcat.springsecurity.util.PasswordUtils;
import org.ballcat.web.util.ModifyParameterRequestWrapper;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 前端传递过来的加密密码，需要在登录之前先解密
 *
 * @author Hccake
 */
@Slf4j
public class LoginPasswordDecoderFilter extends OncePerRequestFilter implements MessageSourceAware {

	private final RequestMatcher requestMatcher;

	private final String passwordSecretKey;

	public LoginPasswordDecoderFilter(RequestMatcher requestMatcher, String passwordSecretKey) {
		Assert.notNull(requestMatcher, "requestMatcher can not be null");
		Assert.notNull(passwordSecretKey, "passwordSecretKey can not be null");
		this.requestMatcher = requestMatcher;
		this.passwordSecretKey = passwordSecretKey;
	}

	private AuthenticationFailureHandler failureHandler = new SimpleUrlAuthenticationFailureHandler();

	private String passwordParameterName = "password";

	protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

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

		// 解密前台加密后的密码
		Map<String, String[]> parameterMap = new HashMap<>(request.getParameterMap());
		String passwordAes = request.getParameter(this.passwordParameterName);

		try {
			String password = PasswordUtils.decodeAES(passwordAes, this.passwordSecretKey);
			parameterMap.put(this.passwordParameterName, new String[] { password });
		}
		catch (Exception e) {
			log.error("[doFilterInternal] password decode aes error，passwordAes: {}，passwordSecretKey: {}", passwordAes,
					this.passwordSecretKey, e);
			this.failureHandler.onAuthenticationFailure(request, response, new BadCredentialsException(this.messages
				.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials")));
			return;
		}

		// SpringSecurity 默认从ParameterMap中获取密码参数
		// 由于原生的request中对parameter加锁了，无法修改，所以使用包装类
		filterChain.doFilter(new ModifyParameterRequestWrapper(request, parameterMap), response);
	}

	public String getPasswordParameterName() {
		return this.passwordParameterName;
	}

	public void setPasswordParameterName(String passwordParameterName) {
		this.passwordParameterName = passwordParameterName;
	}

	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messages = new MessageSourceAccessor(messageSource);
	}

	public void setFailureHandler(AuthenticationFailureHandler failureHandler) {
		Assert.notNull(failureHandler, "failureHandler can not be null");
		this.failureHandler = failureHandler;
	}

}
