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
package org.ballcat.springsecurity.oauth2.server.authorization.config.customizer;

import lombok.RequiredArgsConstructor;
import org.ballcat.springsecurity.oauth2.server.authorization.properties.OAuth2AuthorizationServerProperties;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;

import static org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter.DEFAULT_LOGIN_PAGE_URL;

/**
 * 表单登录配置项
 *
 * @author hccake
 */
@RequiredArgsConstructor
public class FormLoginConfigurerCustomizer implements OAuth2AuthorizationServerConfigurerCustomizer {

	private final OAuth2AuthorizationServerProperties oAuth2AuthorizationServerProperties;

	private final UserDetailsService userDetailsService;

	@Override
	public void customize(OAuth2AuthorizationServerConfigurer oAuth2AuthorizationServerConfigurer,
			HttpSecurity httpSecurity) throws Exception {

		if (oAuth2AuthorizationServerProperties.isLoginPageEnabled()) {
			String loginPage = oAuth2AuthorizationServerProperties.getLoginPage();

			HttpSecurity.RequestMatcherConfigurer requestMatcherConfigurer = httpSecurity.requestMatchers();
			if (loginPage == null) {
				requestMatcherConfigurer.antMatchers(DEFAULT_LOGIN_PAGE_URL);
				httpSecurity.formLogin();
			}
			else {
				requestMatcherConfigurer.antMatchers(loginPage);
				httpSecurity.formLogin(form -> form.loginPage(loginPage).permitAll());
			}

			// 需要 userDetailsService 对应生成 DaoAuthenticationProvider
			httpSecurity.userDetailsService(userDetailsService);
		}

	}

}
