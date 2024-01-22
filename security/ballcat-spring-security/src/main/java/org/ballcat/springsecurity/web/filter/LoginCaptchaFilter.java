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

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import org.ballcat.security.captcha.CaptchaValidateResult;
import org.ballcat.security.captcha.CaptchaValidator;
import org.ballcat.springsecurity.authentication.InvalidCaptchaException;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * @author Hccake
 * @since 2.0.0
 */
@RequiredArgsConstructor
public class LoginCaptchaFilter extends OncePerRequestFilter implements MessageSourceAware {

	private final RequestMatcher requestMatcher;

	private final CaptchaValidator captchaValidator;

	private AuthenticationFailureHandler failureHandler = new SimpleUrlAuthenticationFailureHandler();

	protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		if (!this.requestMatcher.matches(request)) {
			filterChain.doFilter(request, response);
			return;
		}

		CaptchaValidateResult captchaValidateResult = this.captchaValidator.validate(request);
		if (captchaValidateResult.isSuccess()) {
			filterChain.doFilter(request, response);
		}
		else {
			this.logger.error("Captcha verification failed, captchaValidateResult: " + captchaValidateResult);
			this.failureHandler.onAuthenticationFailure(request, response, new InvalidCaptchaException(this.messages
				.getMessage("LoginCaptchaFilter.captchaVerificationFailed", "Captcha verification failed")));
		}
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
