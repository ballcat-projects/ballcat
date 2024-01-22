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

package org.ballcat.springsecurity.oauth2.server.authorization.authentication;

import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;

import org.ballcat.springsecurity.oauth2.server.authorization.client.TestRegisteredClients;
import org.ballcat.springsecurity.oauth2.server.authorization.context.TestAuthorizationServerContext;
import org.ballcat.springsecurity.oauth2.server.authorization.user.TestUsers;
import org.ballcat.springsecurity.oauth2.userdetails.DefaultOAuth2User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.DelegatingOAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.JwtGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2AccessTokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2RefreshTokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.when;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

/***
 * Tests for{@link OAuth2ResourceOwnerPasswordAuthenticationProvider}.
 *
 * @author Hccake
 */
class OAuth2ResourceOwnerPasswordAuthenticationProviderTests {

	private static final String AUTHORIZATION_URI = "https://provider.com/oauth2/authorize";

	private static final String STATE = "state";

	private UserDetailsService userDetailsService;

	private OAuth2AuthorizationService authorizationService;

	private JwtEncoder jwtEncoder;

	private OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer;

	private OAuth2TokenCustomizer<OAuth2TokenClaimsContext> accessTokenCustomizer;

	private OAuth2TokenGenerator<?> tokenGenerator;

	private OAuth2ResourceOwnerPasswordAuthenticationProvider authenticationProvider;

	@BeforeEach
	void setUp() {
		this.userDetailsService = mock(UserDetailsService.class);
		this.authorizationService = mock(OAuth2AuthorizationService.class);

		this.jwtEncoder = mock(JwtEncoder.class);
		this.jwtCustomizer = mock(OAuth2TokenCustomizer.class);
		JwtGenerator jwtGenerator = new JwtGenerator(this.jwtEncoder);
		jwtGenerator.setJwtCustomizer(this.jwtCustomizer);
		this.accessTokenCustomizer = mock(OAuth2TokenCustomizer.class);
		OAuth2AccessTokenGenerator accessTokenGenerator = new OAuth2AccessTokenGenerator();
		accessTokenGenerator.setAccessTokenCustomizer(this.accessTokenCustomizer);
		OAuth2RefreshTokenGenerator refreshTokenGenerator = new OAuth2RefreshTokenGenerator();
		OAuth2TokenGenerator<OAuth2Token> delegatingTokenGenerator = new DelegatingOAuth2TokenGenerator(jwtGenerator,
				accessTokenGenerator, refreshTokenGenerator);
		this.tokenGenerator = spy(new OAuth2TokenGenerator<OAuth2Token>() {
			@Override
			public OAuth2Token generate(OAuth2TokenContext context) {
				return delegatingTokenGenerator.generate(context);
			}
		});

		this.authenticationProvider = new OAuth2ResourceOwnerPasswordAuthenticationProvider(this.authorizationService,
				this.tokenGenerator, this.userDetailsService);
		AuthorizationServerSettings authorizationServerSettings = AuthorizationServerSettings.builder()
			.issuer("https://provider.com")
			.build();
		AuthorizationServerContextHolder
			.setContext(new TestAuthorizationServerContext(authorizationServerSettings, null));
	}

	@Test
	void constructorWhenAuthorizationServiceNullThenThrowIllegalArgumentException() {
		assertThatThrownBy(() -> new OAuth2ResourceOwnerPasswordAuthenticationProvider(null, this.tokenGenerator,
				this.userDetailsService))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("authorizationService cannot be null");
	}

	@Test
	void constructorWhenOAuth2TokenGeneratorNullThenThrowIllegalArgumentException() {
		assertThatThrownBy(() -> new OAuth2ResourceOwnerPasswordAuthenticationProvider(this.authorizationService, null,
				this.userDetailsService))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("tokenGenerator cannot be null");
	}

	@Test
	void constructorWhenUserServiceNullThenThrowIllegalArgumentException() {
		assertThatThrownBy(() -> new OAuth2ResourceOwnerPasswordAuthenticationProvider(this.authorizationService,
				this.tokenGenerator, null))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("userDetailsService cannot be null");
	}

	@Test
	void setWhenPasswordUtilsNullThenThrowIllegalArgumentException() {
		OAuth2ResourceOwnerPasswordAuthenticationProvider provider = new OAuth2ResourceOwnerPasswordAuthenticationProvider(
				this.authorizationService, this.tokenGenerator, this.userDetailsService);
		assertThatThrownBy(() -> provider.setPasswordEncoder(null)).isInstanceOf(IllegalArgumentException.class)
			.hasMessage("passwordEncoder cannot be null");
	}

	@Test
	void supportsWhenTypeOAuth2ResourceOwnerPasswordAuthenticationTokenThenReturnTrue() {
		assertThat(this.authenticationProvider.supports(OAuth2ResourceOwnerPasswordAuthenticationToken.class)).isTrue();
	}

	@Test
	void authenticateWhenClientNotAuthorizedToPasswordThenThrowOAuth2AuthenticationException() {
		RegisteredClient registeredClient = TestRegisteredClients.registeredClient().build();

		OAuth2ClientAuthenticationToken clientPrincipal = new OAuth2ClientAuthenticationToken(registeredClient,
				ClientAuthenticationMethod.CLIENT_SECRET_BASIC, registeredClient.getClientId());

		OAuth2ResourceOwnerPasswordAuthenticationToken authentication = new OAuth2ResourceOwnerPasswordAuthenticationToken(
				"user1", clientPrincipal, new HashMap<>(), new HashSet<>());
		assertThatThrownBy(() -> this.authenticationProvider.authenticate(authentication))
			.isInstanceOf(OAuth2AuthenticationException.class)
			.satisfies(ex -> assertAuthenticationException((OAuth2AuthenticationException) ex,
					OAuth2ErrorCodes.UNAUTHORIZED_CLIENT));
	}

	@Test
	void authenticateSuccess() {
		RegisteredClient registeredClient = TestRegisteredClients.registeredClient()
			.authorizationGrantType(AuthorizationGrantType.PASSWORD)
			.tokenSettings(TokenSettings.builder()
				// 使用不透明令牌
				.accessTokenFormat(OAuth2TokenFormat.REFERENCE)
				.accessTokenTimeToLive(Duration.ofDays(1))
				.refreshTokenTimeToLive(Duration.ofDays(3))
				.build())
			.build();

		OAuth2ClientAuthenticationToken clientPrincipal = new OAuth2ClientAuthenticationToken(registeredClient,
				ClientAuthenticationMethod.CLIENT_SECRET_BASIC, registeredClient.getClientId());

		DefaultOAuth2User defaultOAuth2User = TestUsers.user1().password("{noop}password1").build();

		HashMap<String, Object> additionalParameters = new HashMap<>();
		additionalParameters.put(OAuth2ParameterNames.USERNAME, "user1");
		additionalParameters.put(OAuth2ParameterNames.PASSWORD, "password1");
		OAuth2ResourceOwnerPasswordAuthenticationToken authentication = new OAuth2ResourceOwnerPasswordAuthenticationToken(
				"user1", clientPrincipal, additionalParameters, new HashSet<>());

		when(this.userDetailsService.loadUserByUsername("user1")).thenReturn(defaultOAuth2User);

		OAuth2AccessTokenAuthenticationToken authenticationResult = (OAuth2AccessTokenAuthenticationToken) this.authenticationProvider
			.authenticate(authentication);
		assertThat(authenticationResult.isAuthenticated()).isFalse();
		assertThat(authenticationResult.getRegisteredClient()).isEqualTo(registeredClient);
		assertThat(authenticationResult.getPrincipal()).isEqualTo(clientPrincipal);
		assertThat(authenticationResult.getAccessToken()).isNotNull();
		assertThat(authenticationResult.getRefreshToken()).isNotNull();
		assertThat(authenticationResult.getAdditionalParameters()).isEmpty();
	}

	private static void assertAuthenticationException(OAuth2AuthenticationException authenticationException,
			String errorCode) {
		OAuth2Error error = authenticationException.getError();
		assertThat(error.getErrorCode()).isEqualTo(errorCode);
	}

}
