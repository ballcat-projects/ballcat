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

import java.security.Principal;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClaimAccessor;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import static org.ballcat.springsecurity.oauth2.server.authorization.authentication.OAuth2AuthenticationProviderUtils.getAuthenticatedClientElseThrowInvalidClient;

/**
 * An {@link AuthenticationProvider} implementation for the OAuth 2.0 Resource Owner
 *
 * @author Hccake
 */
@Slf4j
public abstract class AbstractOAuth2ResourceOwnerAuthenticationProvider<T extends AbstractOAuth2ResourceOwnerAuthenticationToken>
		implements AuthenticationProvider {

	private static final String ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-5.2";

	private final OAuth2AuthorizationService authorizationService;

	private final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;

	/**
	 * Constructs an {@code AbstractOAuth2ResourceOwnerAuthenticationProvider} using the
	 * provided parameters.
	 * @param authorizationService the authorization service
	 * @param tokenGenerator the token generator
	 * @since 1.0.0
	 */
	protected AbstractOAuth2ResourceOwnerAuthenticationProvider(OAuth2AuthorizationService authorizationService,
			OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator) {
		Assert.notNull(authorizationService, "authorizationService cannot be null");
		Assert.notNull(tokenGenerator, "tokenGenerator cannot be null");
		this.authorizationService = authorizationService;
		this.tokenGenerator = tokenGenerator;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		@SuppressWarnings("unchecked")
		T resourceOwnerAuthentication = (T) authentication;

		OAuth2ClientAuthenticationToken clientPrincipal = getAuthenticatedClientElseThrowInvalidClient(
				resourceOwnerAuthentication);

		RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();

		if (registeredClient == null) {
			throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_CLIENT);
		}

		AuthorizationGrantType grantType = resourceOwnerAuthentication.getGrantType();
		if (!registeredClient.getAuthorizationGrantTypes().contains(grantType)) {
			throw new OAuth2AuthenticationException(OAuth2ErrorCodes.UNAUTHORIZED_CLIENT);
		}

		// 获取认证后的 Authentication
		Authentication authenticatedAuthentication;
		try {
			authenticatedAuthentication = getAuthenticatedAuthentication(resourceOwnerAuthentication);
		}
		catch (AuthenticationException ex) {
			log.error("OAuth2 authentication error by grant type: " + grantType.getValue() + ", reason: "
					+ ex.getMessage());
			throw new OAuth2AuthenticationException(ex.getMessage());
		}

		// Default to configured scopes
		Set<String> authorizedScopes = registeredClient.getScopes();
		Set<String> requestedScopes = resourceOwnerAuthentication.getScopes();
		if (!CollectionUtils.isEmpty(requestedScopes)) {
			Set<String> unauthorizedScopes = requestedScopes.stream()
				.filter(requestedScope -> !registeredClient.getScopes().contains(requestedScope))
				.collect(Collectors.toSet());
			if (!CollectionUtils.isEmpty(unauthorizedScopes)) {
				throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_SCOPE);
			}

			authorizedScopes = new LinkedHashSet<>(requestedScopes);
		}

		DefaultOAuth2TokenContext.Builder tokenContextBuilder = DefaultOAuth2TokenContext.builder()
			.registeredClient(registeredClient)
			.principal(authenticatedAuthentication)
			.authorizationServerContext(AuthorizationServerContextHolder.getContext())
			.authorizedScopes(authorizedScopes)
			.authorizationGrantType(grantType)
			.authorizationGrant(resourceOwnerAuthentication);

		OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization.withRegisteredClient(registeredClient)
			.principalName(authenticatedAuthentication.getName())
			.authorizationGrantType(grantType)
			.authorizedScopes(authorizedScopes)
			.attribute(Principal.class.getName(), authenticatedAuthentication);

		// ----- Access token -----
		OAuth2TokenContext tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.ACCESS_TOKEN).build();
		OAuth2Token generatedAccessToken = this.tokenGenerator.generate(tokenContext);
		if (generatedAccessToken == null) {
			OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
					"The token generator failed to generate the access token.", ERROR_URI);
			throw new OAuth2AuthenticationException(error);
		}

		OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
				generatedAccessToken.getTokenValue(), generatedAccessToken.getIssuedAt(),
				generatedAccessToken.getExpiresAt(), tokenContext.getAuthorizedScopes());

		if (generatedAccessToken instanceof ClaimAccessor) {
			authorizationBuilder.token(accessToken,
					metadata -> metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME,
							((ClaimAccessor) generatedAccessToken).getClaims()));
		}
		else {
			authorizationBuilder.accessToken(accessToken);
		}

		// ----- Refresh token -----
		OAuth2RefreshToken refreshToken = null;
		if (registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.REFRESH_TOKEN) &&
		// Do not issue refresh token to public client
				!clientPrincipal.getClientAuthenticationMethod().equals(ClientAuthenticationMethod.NONE)) {

			tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.REFRESH_TOKEN).build();
			OAuth2Token generatedRefreshToken = this.tokenGenerator.generate(tokenContext);
			if (!(generatedRefreshToken instanceof OAuth2RefreshToken)) {
				OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
						"The token generator failed to generate the refresh token.", ERROR_URI);
				throw new OAuth2AuthenticationException(error);
			}
			refreshToken = (OAuth2RefreshToken) generatedRefreshToken;
			authorizationBuilder.refreshToken(refreshToken);
		}

		OAuth2Authorization authorization = authorizationBuilder.build();

		this.authorizationService.save(authorization);
		log.debug("OAuth2Authorization saved successfully, then returning OAuth2AccessTokenAuthenticationToken");

		// 切换当前 Authentication 为 User
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(authenticatedAuthentication);
		SecurityContextHolder.setContext(context);

		Map<String, Object> additionalParameters = new HashMap<>(8);
		return new OAuth2AccessTokenAuthenticationToken(registeredClient, clientPrincipal, accessToken, refreshToken,
				additionalParameters);
	}

	/**
	 * 根据请求的 authentication 转换为认证后的 authentication
	 * @param authentication 认证前的 authentication
	 * @return 认证后的 authentication
	 * @throws AuthenticationException 当认证校验不通过时进行异常抛出
	 */
	protected abstract Authentication getAuthenticatedAuthentication(T authentication) throws AuthenticationException;

}
