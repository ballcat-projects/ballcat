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
package org.ballcat.springsecurity.configuration;

import lombok.RequiredArgsConstructor;
import org.ballcat.springsecurity.web.DefaultLogoutSuccessHandler;
import org.ballcat.springsecurity.web.DefualtAuthenticationSuccessHandler;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationEntryPointFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.util.StringUtils;

/**
 * 前后端分离登录的配置定制器
 *
 * @author Hccake
 * @since 2.0.0
 */
@RequiredArgsConstructor
public class SeparationLoginSpringSecurityConfigurerCustomizer implements SpringSecurityConfigurerCustomizer {

	private final SpringSecurityProperties springSecurityProperties;

	private final AuthenticationEntryPoint authenticationEntryPoint;

	private final AuthenticationSuccessHandler authenticationSuccessHandler;

	private final LogoutSuccessHandler logoutSuccessHandler;

	private String passwordAesSecretKey = null;

	@Override
	public void customize(HttpSecurity httpSecurity) throws Exception {
		// 表单登录
		if (springSecurityProperties.isLoginPageEnabled()) {

			AuthenticationSuccessHandler successHandler = authenticationSuccessHandler == null
					? new DefualtAuthenticationSuccessHandler() : authenticationSuccessHandler;
			AuthenticationEntryPointFailureHandler failureHandler = new AuthenticationEntryPointFailureHandler(
					authenticationEntryPoint == null ? new Http403ForbiddenEntryPoint() : authenticationEntryPoint);

			SeparationLoginConfigurer<HttpSecurity> separationLoginConfigurer = httpSecurity
				.apply(new SeparationLoginConfigurer<>(this.passwordAesSecretKey))
				.successHandler(successHandler)
				.failureHandler(failureHandler);

			// 自定义了表单页面
			String loginPage = springSecurityProperties.getLoginPage();
			if (StringUtils.hasText(loginPage)) {
				httpSecurity.requestMatchers().antMatchers(loginPage);
				separationLoginConfigurer.loginPage(loginPage);
			}

			// 登出
			LogoutSuccessHandler logoutSuccessHandlerLocal = this.logoutSuccessHandler == null
					? new DefaultLogoutSuccessHandler() : this.logoutSuccessHandler;
			httpSecurity.logout().logoutSuccessHandler(logoutSuccessHandlerLocal);
		}
	}

	@Override
	public int getOrder() {
		return 0;
	}

	public void setPasswordAesSecretKey(String passwordAesSecretKey) {
		this.passwordAesSecretKey = passwordAesSecretKey;
	}

}
