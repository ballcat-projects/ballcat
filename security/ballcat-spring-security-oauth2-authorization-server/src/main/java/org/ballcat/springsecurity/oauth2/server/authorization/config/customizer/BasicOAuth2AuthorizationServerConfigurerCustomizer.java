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

package org.ballcat.springsecurity.oauth2.server.authorization.config.customizer;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.ballcat.springsecurity.configuer.SpringSecurityConfigurerCustomizer;
import org.ballcat.springsecurity.oauth2.server.authorization.config.configurer.OAuth2AuthorizationServerConfigurerExtension;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.CollectionUtils;

/**
 * OAuth2 的授权服务配置
 * <p>
 * 当实例既是授权服务器又是资源服务器时，Order 必须高于资源服务器
 *
 * @author hccake
 */
@RequiredArgsConstructor
public class BasicOAuth2AuthorizationServerConfigurerCustomizer implements SpringSecurityConfigurerCustomizer {

	private final List<OAuth2AuthorizationServerConfigurerExtension> oAuth2AuthorizationServerConfigurerExtensionList;

	private final List<OAuth2AuthorizationServerConfigurerAdapter> OAuth2AuthorizationServerConfigurerAdapters;

	@Override
	public void customize(HttpSecurity http) throws Exception {
		// 授权服务器配置
		OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer();
		for (OAuth2AuthorizationServerConfigurerExtension customizer : this.oAuth2AuthorizationServerConfigurerExtensionList) {
			customizer.customize(authorizationServerConfigurer, http);
		}

		RequestMatcher endpointsMatcher = authorizationServerConfigurer.getEndpointsMatcher();
		http.requestMatchers().requestMatchers(endpointsMatcher);

		// 针对授权服务器端点，关闭跨站防护攻击
		@SuppressWarnings("unchecked")
		CsrfConfigurer<HttpSecurity> csrfConfigurer = http.getConfigurer(CsrfConfigurer.class);
		if (csrfConfigurer != null) {
			csrfConfigurer.ignoringRequestMatchers(endpointsMatcher);
		}

		http.apply(authorizationServerConfigurer);

		// 适配处理
		if (!CollectionUtils.isEmpty(this.OAuth2AuthorizationServerConfigurerAdapters)) {
			for (OAuth2AuthorizationServerConfigurerAdapter configurer : this.OAuth2AuthorizationServerConfigurerAdapters) {
				http.apply(configurer);
			}
		}
	}

	@Override
	public int getOrder() {
		return -1000;
	}

}
