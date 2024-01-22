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

package org.ballcat.springsecurity.oauth2.server.authorization.config.configurer;

import java.util.List;
import java.util.function.Consumer;

import lombok.RequiredArgsConstructor;
import org.ballcat.springsecurity.oauth2.server.authorization.authentication.BallcatOAuth2TokenRevocationAuthenticationProvider;
import org.ballcat.springsecurity.oauth2.server.authorization.web.authentication.BallcatOAuth2TokenRevocationAuthenticationConverter;
import org.ballcat.springsecurity.oauth2.server.authorization.web.authentication.OAuth2TokenRevocationResponseHandler;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2TokenRevocationAuthenticationProvider;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2TokenRevocationAuthenticationConverter;
import org.springframework.security.web.authentication.AuthenticationConverter;

/**
 * 令牌撤销端点配置的自定义扩展
 *
 * @author hccake
 */
@RequiredArgsConstructor
public class OAuth2TokenRevocationEndpointConfigurerExtension implements OAuth2AuthorizationServerConfigurerExtension {

	private final OAuth2AuthorizationService authorizationService;

	private final OAuth2TokenRevocationResponseHandler oAuth2TokenRevocationResponseHandler;

	@Override
	public void customize(OAuth2AuthorizationServerConfigurer oAuth2AuthorizationServerConfigurer,
			HttpSecurity httpSecurity) {
		final Consumer<List<AuthenticationProvider>> authenticationProvidersConsumer = authenticationProviders -> {
			authenticationProviders
				.removeIf(x -> x.getClass().isAssignableFrom(OAuth2TokenRevocationAuthenticationProvider.class));
			authenticationProviders.add(0,
					new BallcatOAuth2TokenRevocationAuthenticationProvider(this.authorizationService));
		};

		final Consumer<List<AuthenticationConverter>> convertersConsumer = converters -> {
			converters.removeIf(x -> x.getClass().isAssignableFrom(OAuth2TokenRevocationAuthenticationConverter.class));
			converters.add(0, new BallcatOAuth2TokenRevocationAuthenticationConverter());
		};

		oAuth2AuthorizationServerConfigurer.tokenRevocationEndpoint(
				tokenRevocation -> tokenRevocation.revocationResponseHandler(this.oAuth2TokenRevocationResponseHandler)
					.revocationRequestConverters(convertersConsumer)
					.authenticationProviders(authenticationProvidersConsumer));
	}

}
