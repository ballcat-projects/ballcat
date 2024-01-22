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

package org.ballcat.springsecurity.oauth2.server.authorization.token;

import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import org.ballcat.springsecurity.oauth2.constant.TokenAttributeNameConstants;
import org.ballcat.springsecurity.oauth2.constant.UserInfoFiledNameConstants;
import org.ballcat.springsecurity.oauth2.server.authorization.authentication.OAuth2ResourceOwnerPasswordAuthenticationToken;
import org.ballcat.springsecurity.oauth2.server.authorization.client.TestRegisteredClients;
import org.ballcat.springsecurity.oauth2.server.authorization.context.TestAuthorizationServerContext;
import org.ballcat.springsecurity.oauth2.server.authorization.user.TestUsers;
import org.ballcat.springsecurity.oauth2.userdetails.DefaultOAuth2User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClaimAccessor;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContext;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2AccessTokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimAccessor;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link BallcatOAuth2TokenCustomizerTests}.
 *
 * @author hccake
 */
class BallcatOAuth2TokenCustomizerTests {

	private OAuth2TokenCustomizer<OAuth2TokenClaimsContext> accessTokenCustomizer;

	private OAuth2AccessTokenGenerator accessTokenGenerator;

	private AuthorizationServerContext authorizationServerContext;

	@BeforeEach
	public void setUp() {
		this.accessTokenCustomizer = new BallcatOAuth2TokenCustomizer();
		this.accessTokenGenerator = new OAuth2AccessTokenGenerator();
		this.accessTokenGenerator.setAccessTokenCustomizer(this.accessTokenCustomizer);
		AuthorizationServerSettings authorizationServerSettings = AuthorizationServerSettings.builder()
			.issuer("https://provider.com")
			.build();
		this.authorizationServerContext = new TestAuthorizationServerContext(authorizationServerSettings, null);
	}

	@Test
	void customizeWhenPrincipalIsUser() {
		// @formatter:off
		TokenSettings tokenSettings = TokenSettings.builder()
				.accessTokenFormat(OAuth2TokenFormat.REFERENCE)
				.build();
		RegisteredClient registeredClient = TestRegisteredClients.registeredClient()
				.authorizationGrantType(AuthorizationGrantType.PASSWORD)
				.tokenSettings(tokenSettings)
				.build();
		// @formatter:on

		Set<String> authorizedScopes = registeredClient.getScopes();

		OAuth2ClientAuthenticationToken clientPrincipal = new OAuth2ClientAuthenticationToken(registeredClient,
				ClientAuthenticationMethod.CLIENT_SECRET_BASIC, registeredClient.getClientSecret());

		DefaultOAuth2User defaultOAuth2User = TestUsers.user1().build();
		HashMap<String, Object> additionalParameters = new HashMap<>();
		additionalParameters.put(OAuth2ParameterNames.USERNAME, "user1");
		additionalParameters.put(OAuth2ParameterNames.PASSWORD, "password1");
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken
			.authenticated(defaultOAuth2User, defaultOAuth2User.getUsername(), defaultOAuth2User.getAuthorities());
		OAuth2ResourceOwnerPasswordAuthenticationToken resourceOwnerPasswordAuthentication = new OAuth2ResourceOwnerPasswordAuthenticationToken(
				defaultOAuth2User.getUsername(), clientPrincipal, additionalParameters, authorizedScopes);

		// @formatter:off
		OAuth2TokenContext tokenContext = DefaultOAuth2TokenContext.builder()
				.registeredClient(registeredClient)
				.principal(usernamePasswordAuthenticationToken)
				.authorizationServerContext(this.authorizationServerContext)
				.authorizedScopes(authorizedScopes)
				.authorizationGrantType(AuthorizationGrantType.PASSWORD)
				.authorizationGrant(resourceOwnerPasswordAuthentication)
				.tokenType(OAuth2TokenType.ACCESS_TOKEN)
				.build();
		// @formatter:on

		OAuth2AccessToken accessToken = this.accessTokenGenerator.generate(tokenContext);
		assertThat(accessToken).isNotNull();

		Instant issuedAt = Instant.now();
		Instant expiresAt = issuedAt
			.plus(tokenContext.getRegisteredClient().getTokenSettings().getAccessTokenTimeToLive());
		assertThat(accessToken.getIssuedAt()).isBetween(issuedAt.minusSeconds(1), issuedAt.plusSeconds(1));
		assertThat(accessToken.getExpiresAt()).isBetween(expiresAt.minusSeconds(1), expiresAt.plusSeconds(1));
		assertThat(accessToken.getScopes()).isEqualTo(tokenContext.getAuthorizedScopes());

		assertThat(accessToken).isInstanceOf(ClaimAccessor.class);
		OAuth2TokenClaimAccessor accessTokenClaims = ((ClaimAccessor) accessToken)::getClaims;

		assertThat(accessTokenClaims.getIssuer().toExternalForm())
			.isEqualTo(tokenContext.getAuthorizationServerContext().getIssuer());
		assertThat(accessTokenClaims.getSubject()).isEqualTo(tokenContext.getPrincipal().getName());
		assertThat(accessTokenClaims.getAudience())
			.isEqualTo(Collections.singletonList(tokenContext.getRegisteredClient().getClientId()));
		assertThat(accessTokenClaims.getIssuedAt()).isBetween(issuedAt.minusSeconds(1), issuedAt.plusSeconds(1));
		assertThat(accessTokenClaims.getExpiresAt()).isBetween(expiresAt.minusSeconds(1), expiresAt.plusSeconds(1));
		assertThat(accessTokenClaims.getNotBefore()).isBetween(issuedAt.minusSeconds(1), issuedAt.plusSeconds(1));
		assertThat(accessTokenClaims.getId()).isNotNull();

		assertThat(accessTokenClaims.getClaimAsBoolean(TokenAttributeNameConstants.IS_CLIENT)).isFalse();

		assertThat(accessTokenClaims.getClaimAsMap(TokenAttributeNameConstants.ATTRIBUTES))
			.isEqualTo(defaultOAuth2User.getAttributes());

		assertThat(accessTokenClaims.getClaimAsMap(TokenAttributeNameConstants.INFO))
			.containsEntry(UserInfoFiledNameConstants.USER_ID, defaultOAuth2User.getUserId())
			.containsEntry(UserInfoFiledNameConstants.TYPE, defaultOAuth2User.getType())
			.containsEntry(UserInfoFiledNameConstants.ORGANIZATION_ID, defaultOAuth2User.getOrganizationId())
			.containsEntry(UserInfoFiledNameConstants.USERNAME, defaultOAuth2User.getUsername())
			.containsEntry(UserInfoFiledNameConstants.NICKNAME, defaultOAuth2User.getNickname())
			.containsEntry(UserInfoFiledNameConstants.AVATAR, defaultOAuth2User.getAvatar());
	}

	@Test
	void customizeWhenPrincipalIsClient() {
		// @formatter:off
		TokenSettings tokenSettings = TokenSettings.builder()
				.accessTokenFormat(OAuth2TokenFormat.REFERENCE)
				.build();
		RegisteredClient registeredClient = TestRegisteredClients.registeredClient()
				.tokenSettings(tokenSettings)
				.build();
		// @formatter:on

		Set<String> authorizedScopes = registeredClient.getScopes();

		OAuth2ClientAuthenticationToken clientPrincipal = new OAuth2ClientAuthenticationToken(registeredClient,
				ClientAuthenticationMethod.CLIENT_SECRET_BASIC, registeredClient.getClientSecret());

		// @formatter:off
		OAuth2TokenContext tokenContext = DefaultOAuth2TokenContext.builder()
				.registeredClient(registeredClient)
				.principal(clientPrincipal)
				.authorizationServerContext(this.authorizationServerContext)
				.authorizedScopes(authorizedScopes)
				.tokenType(OAuth2TokenType.ACCESS_TOKEN)
				.authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
				.authorizationGrant(clientPrincipal)
				.build();
		// @formatter:on

		OAuth2AccessToken accessToken = this.accessTokenGenerator.generate(tokenContext);
		assertThat(accessToken).isNotNull();

		Instant issuedAt = Instant.now();
		Instant expiresAt = issuedAt
			.plus(tokenContext.getRegisteredClient().getTokenSettings().getAccessTokenTimeToLive());
		assertThat(accessToken.getIssuedAt()).isBetween(issuedAt.minusSeconds(1), issuedAt.plusSeconds(1));
		assertThat(accessToken.getExpiresAt()).isBetween(expiresAt.minusSeconds(1), expiresAt.plusSeconds(1));
		assertThat(accessToken.getScopes()).isEqualTo(tokenContext.getAuthorizedScopes());

		assertThat(accessToken).isInstanceOf(ClaimAccessor.class);
		OAuth2TokenClaimAccessor accessTokenClaims = ((ClaimAccessor) accessToken)::getClaims;

		assertThat(accessTokenClaims.getIssuer().toExternalForm())
			.isEqualTo(tokenContext.getAuthorizationServerContext().getIssuer());
		assertThat(accessTokenClaims.getSubject()).isEqualTo(tokenContext.getPrincipal().getName());
		assertThat(accessTokenClaims.getAudience())
			.isEqualTo(Collections.singletonList(tokenContext.getRegisteredClient().getClientId()));
		assertThat(accessTokenClaims.getIssuedAt()).isBetween(issuedAt.minusSeconds(1), issuedAt.plusSeconds(1));
		assertThat(accessTokenClaims.getExpiresAt()).isBetween(expiresAt.minusSeconds(1), expiresAt.plusSeconds(1));
		assertThat(accessTokenClaims.getNotBefore()).isBetween(issuedAt.minusSeconds(1), issuedAt.plusSeconds(1));
		assertThat(accessTokenClaims.getId()).isNotNull();

		assertThat(accessTokenClaims.getClaimAsBoolean(TokenAttributeNameConstants.IS_CLIENT)).isTrue();
	}

}
