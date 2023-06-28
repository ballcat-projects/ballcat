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
package org.ballcat.springsecurity.oauth2.server.resource.configurer;

import lombok.RequiredArgsConstructor;
import org.ballcat.springsecurity.oauth2.server.resource.properties.OAuth2ResourceServerProperties;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collections;
import java.util.List;

/**
 * 资源服务器的配置
 *
 * @author hccake
 */
@RequiredArgsConstructor
public class BallcatOauth2ResourceServerSecurityFilterChainBuilder
		implements Oauth2ResourceServerSecurityFilterChainBuilder {

	private final OAuth2ResourceServerProperties oAuth2ResourceServerProperties;

	private final AuthenticationEntryPoint authenticationEntryPoint;

	private final BearerTokenResolver bearerTokenResolver;

	private final ObjectProvider<List<OAuth2ResourceServerConfigurerCustomizer>> configurerCustomizersProvider;

	@Override
	public SecurityFilterChain build(HttpSecurity http) throws Exception {
		// @formatter:off
		http
			// 拦截 url 配置
			.authorizeRequests()
			.antMatchers(oAuth2ResourceServerProperties.getIgnoreUrls().toArray(new String[0]))
			.permitAll()
			.anyRequest().authenticated()

			// 关闭 csrf 跨站攻击防护
			.and().csrf().disable()

			// 开启 OAuth2 资源服务
			.oauth2ResourceServer().authenticationEntryPoint(authenticationEntryPoint)
			// bearToken 解析器
			.bearerTokenResolver(bearerTokenResolver)
			// 不透明令牌，
			.opaqueToken();
		// @formatter:on

		// 允许嵌入iframe
		if (!oAuth2ResourceServerProperties.isIframeDeny()) {
			http.headers().frameOptions().disable();
		}

		// 自定义处理
		List<OAuth2ResourceServerConfigurerCustomizer> configurerCustomizers = configurerCustomizersProvider
			.getIfAvailable(Collections::emptyList);
		for (OAuth2ResourceServerConfigurerCustomizer configurerCustomizer : configurerCustomizers) {
			configurerCustomizer.customize(http);
		}

		return http.build();
	}

}