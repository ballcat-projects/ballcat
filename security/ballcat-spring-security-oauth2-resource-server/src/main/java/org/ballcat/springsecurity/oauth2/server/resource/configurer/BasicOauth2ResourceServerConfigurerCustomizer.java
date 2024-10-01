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

package org.ballcat.springsecurity.oauth2.server.resource.configurer;

import org.ballcat.springsecurity.configuer.SpringSecurityConfigurerCustomizer;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * 基础的资源服务器配置定制器
 *
 * @author Hccake
 */
public class BasicOauth2ResourceServerConfigurerCustomizer implements SpringSecurityConfigurerCustomizer {

	public static final String BASIC_OAUTH2_RESOURCE_SERVER_CONFIGURER_CUSTOMIZER_BEAN_NAME = "basicOauth2ResourceServerConfigurerCustomizer";

	private final AuthenticationEntryPoint authenticationEntryPoint;

	private final BearerTokenResolver bearerTokenResolver;

	public BasicOauth2ResourceServerConfigurerCustomizer(
			ObjectProvider<AuthenticationEntryPoint> authenticationEntryPointObjectProvider,
			BearerTokenResolver bearerTokenResolver) {
		this.authenticationEntryPoint = authenticationEntryPointObjectProvider.getIfAvailable();
		this.bearerTokenResolver = bearerTokenResolver;
	}

	@Override
	public void customize(HttpSecurity httpSecurity) throws Exception {
		// 开启 OAuth2 资源服务
		OAuth2ResourceServerConfigurer<HttpSecurity> httpSecurityOAuth2ResourceServerConfigurer = httpSecurity
			.oauth2ResourceServer();

		// 认证错误处理
		if (this.authenticationEntryPoint != null) {
			httpSecurity.exceptionHandling().authenticationEntryPoint(this.authenticationEntryPoint);
		}

		httpSecurityOAuth2ResourceServerConfigurer
			// bearToken 解析器
			.bearerTokenResolver(this.bearerTokenResolver)
			// 不透明令牌，
			.opaqueToken();
	}

	@Override
	public int getOrder() {
		return -100;
	}

}
