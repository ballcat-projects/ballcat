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

import lombok.RequiredArgsConstructor;
import org.ballcat.springsecurity.properties.SpringSecurityProperties;
import org.ballcat.springsecurity.web.FormLoginSuccessHandler;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationEntryPointFailureHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

/**
 * 前后端不分离登录的配置定制器
 *
 * @author Hccake
 * @since 2.0.0
 */
@RequiredArgsConstructor
public class FormLoginSpringSecurityConfigurerCustomizer implements SpringSecurityConfigurerCustomizer {

	private final SpringSecurityProperties springSecurityProperties;

	private final AuthenticationEntryPoint authenticationEntryPoint;

	private final FormLoginSuccessHandler formLoginSuccessHandler;

	private final LogoutSuccessHandler logoutSuccessHandler;

	@Override
	public void customize(HttpSecurity httpSecurity) throws Exception {
		// 表单登录
		SpringSecurityProperties.FormLogin formLogin = this.springSecurityProperties.getFormLogin();
		if (formLogin.isEnabled()) {

			FormLoginConfigurer<HttpSecurity> formLoginConfigurer = httpSecurity.formLogin();

			if (this.formLoginSuccessHandler != null) {
				formLoginConfigurer.successHandler(this.formLoginSuccessHandler);
			}

			if (this.authenticationEntryPoint != null) {
				AuthenticationEntryPointFailureHandler authenticationFailureHandler = new AuthenticationEntryPointFailureHandler(
						this.authenticationEntryPoint);
				formLoginConfigurer.failureHandler(authenticationFailureHandler);
				httpSecurity.setSharedObject(AuthenticationFailureHandler.class, authenticationFailureHandler);
			}

			// 自定义了表单页面
			String loginPage = formLogin.getLoginPage();
			if (loginPage != null) {
				httpSecurity.requestMatchers().antMatchers(loginPage);
				formLoginConfigurer.loginPage(loginPage);
			}

			String loginProcessingUrl = formLogin.getLoginProcessingUrl();
			if (loginProcessingUrl != null) {
				formLoginConfigurer.loginProcessingUrl(loginProcessingUrl);
			}

			// 登出
			LogoutConfigurer<HttpSecurity> logout = httpSecurity.logout();
			if (this.logoutSuccessHandler != null) {
				logout.logoutSuccessHandler(this.logoutSuccessHandler);
			}
		}
	}

	@Override
	public int getOrder() {
		return 0;
	}

}
