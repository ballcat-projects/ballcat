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
package org.ballcat.springsecurity.oauth2.server.authorization.config.configurer;

import org.ballcat.springsecurity.configuration.SpringSecurityProperties;
import org.ballcat.springsecurity.oauth2.server.authorization.properties.OAuth2AuthorizationServerProperties;
import org.ballcat.springsecurity.oauth2.server.authorization.web.authentication.OAuth2LoginUrlAuthenticationEntryPoint;
import org.ballcat.springsecurity.oauth2.server.authorization.web.context.OAuth2SecurityContextRepository;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * OAuth2 授权码流程端点的扩展处理器
 *
 * @author hccake
 */
public class OAuth2AuthorizationEndpointConfigurerExtension implements OAuth2AuthorizationServerConfigurerExtension {

	private final SpringSecurityProperties springSecurityProperties;

	private final OAuth2AuthorizationServerProperties oAuth2AuthorizationServerProperties;

	private final OAuth2SecurityContextRepository oAuth2SecurityContextRepository;

	public OAuth2AuthorizationEndpointConfigurerExtension(SpringSecurityProperties springSecurityProperties,
			OAuth2AuthorizationServerProperties oAuth2AuthorizationServerProperties,
			OAuth2SecurityContextRepository oAuth2SecurityContextRepository) {
		this.springSecurityProperties = springSecurityProperties;
		this.oAuth2AuthorizationServerProperties = oAuth2AuthorizationServerProperties;
		this.oAuth2SecurityContextRepository = oAuth2SecurityContextRepository;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void customize(OAuth2AuthorizationServerConfigurer oAuth2AuthorizationServerConfigurer,
			HttpSecurity httpSecurity) throws Exception {

		// 使用无状态登录时，需要配合自定义的 SecurityContextRepository
		if (springSecurityProperties.isStateless()) {
			httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
			httpSecurity
				.securityContext(security -> security.securityContextRepository(oAuth2SecurityContextRepository));
		}

		// 设置鉴权失败时的跳转地址
		String loginPage = springSecurityProperties.getLoginPage();
		AuthorizationServerSettings authorizationServerSettings = OAuth2ConfigurerUtils
			.getAuthorizationServerSettings(httpSecurity);
		ExceptionHandlingConfigurer<?> exceptionHandling = httpSecurity
			.getConfigurer(ExceptionHandlingConfigurer.class);
		if (exceptionHandling != null) {
			exceptionHandling.defaultAuthenticationEntryPointFor(new OAuth2LoginUrlAuthenticationEntryPoint(loginPage),
					new AntPathRequestMatcher(authorizationServerSettings.getAuthorizationEndpoint(),
							HttpMethod.GET.name()));
		}

		// 设置 OAuth2 Consent 地址
		String consentPage = oAuth2AuthorizationServerProperties.getConsentPage();
		if (consentPage != null) {
			oAuth2AuthorizationServerConfigurer
				.authorizationEndpoint(configurer -> configurer.consentPage(consentPage));
		}
	}

}
