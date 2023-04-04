package org.ballcat.springsecurity.oauth2.server.authorization.authentication;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
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

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

import static org.ballcat.springsecurity.oauth2.server.authorization.authentication.OAuth2AuthenticationProviderUtils.getAuthenticatedClientElseThrowInvalidClient;

/**
 * @author Hccake
 */
@Slf4j
public class OAuth2ResourceOwnerPasswordAuthenticationProvider implements AuthenticationProvider {

	private static final String ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-5.2";

	private final AuthenticationManager authenticationManager;

	private final OAuth2AuthorizationService authorizationService;

	private final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;

	/**
	 * Constructs an {@code OAuth2ResourceOwnerPasswordAuthenticationProviderNew} using
	 * the provided parameters.
	 * @param authenticationManager the authentication manager
	 * @param authorizationService the authorization service
	 * @param tokenGenerator the token generator
	 * @since 1.0.0
	 */
	public OAuth2ResourceOwnerPasswordAuthenticationProvider(AuthenticationManager authenticationManager,
			OAuth2AuthorizationService authorizationService,
			OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator) {
		Assert.notNull(authenticationManager, "authenticationManager cannot be null");
		Assert.notNull(authorizationService, "authorizationService cannot be null");
		Assert.notNull(tokenGenerator, "tokenGenerator cannot be null");
		this.authenticationManager = authenticationManager;
		this.authorizationService = authorizationService;
		this.tokenGenerator = tokenGenerator;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		OAuth2ResourceOwnerPasswordAuthenticationToken resourceOwnerPasswordAuthentication = (OAuth2ResourceOwnerPasswordAuthenticationToken) authentication;

		OAuth2ClientAuthenticationToken clientPrincipal = getAuthenticatedClientElseThrowInvalidClient(
				resourceOwnerPasswordAuthentication);

		RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();

		if (registeredClient == null) {
			throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_CLIENT);
		}

		if (!registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.PASSWORD)) {
			throw new OAuth2AuthenticationException(OAuth2ErrorCodes.UNAUTHORIZED_CLIENT);
		}

		Authentication usernamePasswordAuthentication = getUsernamePasswordAuthentication(
				resourceOwnerPasswordAuthentication);

		// Default to configured scopes
		Set<String> authorizedScopes = registeredClient.getScopes();
		Set<String> requestedScopes = resourceOwnerPasswordAuthentication.getScopes();
		if (!CollectionUtils.isEmpty(requestedScopes)) {
			Set<String> unauthorizedScopes = requestedScopes.stream()
				.filter(requestedScope -> !registeredClient.getScopes().contains(requestedScope))
				.collect(Collectors.toSet());
			if (!CollectionUtils.isEmpty(unauthorizedScopes)) {
				throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_SCOPE);
			}

			authorizedScopes = new LinkedHashSet<>(requestedScopes);
		}

		// @formatter:off
		DefaultOAuth2TokenContext.Builder tokenContextBuilder = DefaultOAuth2TokenContext.builder()
				.registeredClient(registeredClient)
				.principal(usernamePasswordAuthentication)
				.authorizationServerContext(AuthorizationServerContextHolder.getContext())
				.authorizedScopes(authorizedScopes)
				.authorizationGrantType(AuthorizationGrantType.PASSWORD)
				.authorizationGrant(resourceOwnerPasswordAuthentication);
		// @formatter:on

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

		}

		// @formatter:off
		OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization.withRegisteredClient(registeredClient)
				.principalName(usernamePasswordAuthentication.getName())
				.authorizationGrantType(AuthorizationGrantType.PASSWORD)
				.authorizedScopes(authorizedScopes)
				.attribute(Principal.class.getName(), usernamePasswordAuthentication);
		// @formatter:on
		if (generatedAccessToken instanceof ClaimAccessor) {
			authorizationBuilder.token(accessToken,
					metadata -> metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME,
							((ClaimAccessor) generatedAccessToken).getClaims()));
		}
		else {
			authorizationBuilder.accessToken(accessToken);
		}

		OAuth2Authorization authorization = authorizationBuilder.build();

		this.authorizationService.save(authorization);
		log.debug("OAuth2Authorization saved successfully, then returning OAuth2AccessTokenAuthenticationToken");

		// 切换当前 Authentication 为 User
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(usernamePasswordAuthentication);
		SecurityContextHolder.setContext(context);

		Map<String, Object> additionalParameters = new HashMap<>(8);
		return new OAuth2AccessTokenAuthenticationToken(registeredClient, clientPrincipal, accessToken, refreshToken,
				additionalParameters);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		boolean supports = OAuth2ResourceOwnerPasswordAuthenticationToken.class.isAssignableFrom(authentication);
		log.debug("supports authentication={}} returning {}", authentication, supports);
		return supports;
	}

	private Authentication getUsernamePasswordAuthentication(
			OAuth2ResourceOwnerPasswordAuthenticationToken resourceOwnerPasswordAuthentication) {

		Map<String, Object> additionalParameters = resourceOwnerPasswordAuthentication.getAdditionalParameters();

		String username = (String) additionalParameters.get(OAuth2ParameterNames.USERNAME);
		String password = (String) additionalParameters.get(OAuth2ParameterNames.PASSWORD);

		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
				username, password);
		log.debug("got usernamePasswordAuthenticationToken={}", usernamePasswordAuthenticationToken);

		return authenticationManager.authenticate(usernamePasswordAuthenticationToken);
	}

}
