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

import org.ballcat.springsecurity.oauth2.server.authorization.config.configurer.OAuth2ConfigurerUtils;
import org.ballcat.springsecurity.oauth2.server.authorization.web.filter.LoginPasswordDecoderFilter;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.web.OAuth2ClientAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

/**
 * 登录时的密码解密配置
 *
 * @author hccake
 */
public class OAuth2LoginPasswordDecoderConfigurerAdapter implements OAuth2AuthorizationServerConfigurerAdapter {

	private final String passwordSecretKey;

	public OAuth2LoginPasswordDecoderConfigurerAdapter(String passwordSecretKey) {
		Assert.hasText(passwordSecretKey, "passwordSecretKey can not be null");
		this.passwordSecretKey = passwordSecretKey;
	}

	@Override
	public void init(HttpSecurity builder) {

	}

	@Override
	public void configure(HttpSecurity httpSecurity) throws Exception {
		// 获取授权服务器配置
		AuthorizationServerSettings authorizationServerSettings = OAuth2ConfigurerUtils
			.getAuthorizationServerSettings(httpSecurity);

		// 只处理登录接口
		AntPathRequestMatcher requestMatcher = new AntPathRequestMatcher(authorizationServerSettings.getTokenEndpoint(),
				HttpMethod.POST.name());

		// 密码解密，必须在 OAuth2ClientAuthenticationFilter 过滤器之后，方便获取当前客户端
		httpSecurity.addFilterAfter(new LoginPasswordDecoderFilter(requestMatcher, this.passwordSecretKey),
				OAuth2ClientAuthenticationFilter.class);
	}

	@Override
	public int getOrder() {
		return 100;
	}

}
