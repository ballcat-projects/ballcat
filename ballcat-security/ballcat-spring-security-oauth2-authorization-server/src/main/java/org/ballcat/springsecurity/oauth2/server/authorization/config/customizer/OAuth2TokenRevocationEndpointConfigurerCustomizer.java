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
import org.ballcat.springsecurity.oauth2.server.authorization.authentication.OAuth2TokenRevocationAuthenticationProvider;
import org.ballcat.springsecurity.oauth2.server.authorization.web.authentication.OAuth2TokenRevocationResponseHandler;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;

/**
 * 令牌撤销端点配置的自定义扩展
 *
 * @author hccake
 */
@RequiredArgsConstructor
public class OAuth2TokenRevocationEndpointConfigurerCustomizer
		implements OAuth2AuthorizationServerConfigurerCustomizer {

	private final OAuth2AuthorizationService authorizationService;

	private final OAuth2TokenRevocationResponseHandler oAuth2TokenRevocationResponseHandler;

	@Override
	public void customize(OAuth2AuthorizationServerConfigurer oAuth2AuthorizationServerConfigurer,
			HttpSecurity httpSecurity) {
		oAuth2AuthorizationServerConfigurer.tokenRevocationEndpoint(
				tokenRevocation -> tokenRevocation.revocationResponseHandler(oAuth2TokenRevocationResponseHandler)
					.authenticationProvider(new OAuth2TokenRevocationAuthenticationProvider(authorizationService)));
	}

}
