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

package org.ballcat.springsecurity.configuer;

import org.ballcat.security.captcha.CaptchaValidator;
import org.ballcat.springsecurity.properties.SpringSecurityProperties;
import org.ballcat.springsecurity.web.filter.LoginCaptchaFilter;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

/**
 * 登录时的密码解密配置
 *
 * @author hccake
 */
public class LoginCaptchaSpringSecurityConfigurerCustomizer
		implements SpringSecurityConfigurerCustomizer, MessageSourceAware {

	private final SpringSecurityProperties springSecurityProperties;

	private final CaptchaValidator captchaValidator;

	private MessageSource messageSource;

	public LoginCaptchaSpringSecurityConfigurerCustomizer(SpringSecurityProperties springSecurityProperties,
			CaptchaValidator captchaValidator) {
		Assert.notNull(springSecurityProperties, "springSecurityProperties can not be null");
		Assert.notNull(captchaValidator, "captchaValidator can not be null");
		this.springSecurityProperties = springSecurityProperties;
		this.captchaValidator = captchaValidator;
	}

	@Override
	public void customize(HttpSecurity httpSecurity) throws Exception {
		SpringSecurityProperties.FormLogin formLogin = this.springSecurityProperties.getFormLogin();

		String loginProcessingUrl = formLogin.getLoginProcessingUrl();
		if (loginProcessingUrl == null) {
			loginProcessingUrl = formLogin.getLoginPage();
		}
		if (loginProcessingUrl == null) {
			loginProcessingUrl = "/login";
		}

		// 只处理登录接口
		AntPathRequestMatcher requestMatcher = new AntPathRequestMatcher(loginProcessingUrl, HttpMethod.POST.name());

		// 创建过滤器
		LoginCaptchaFilter filter = new LoginCaptchaFilter(requestMatcher, this.captchaValidator);
		if (this.messageSource != null) {
			filter.setMessageSource(this.messageSource);
		}
		AuthenticationFailureHandler failureHandler = httpSecurity.getSharedObject(AuthenticationFailureHandler.class);
		if (failureHandler != null) {
			filter.setFailureHandler(failureHandler);
		}

		// 密码解密，必须在 UsernamePasswordAuthenticationFilter 之前
		httpSecurity.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
	}

	@Override
	public int getOrder() {
		return 200;
	}

	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

}
