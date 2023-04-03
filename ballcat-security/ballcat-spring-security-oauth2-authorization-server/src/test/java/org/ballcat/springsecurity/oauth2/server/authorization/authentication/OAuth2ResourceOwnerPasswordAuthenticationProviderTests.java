package org.ballcat.springsecurity.oauth2.server.authorization.authentication;

import com.hccake.ballcat.common.security.userdetails.User;
import org.ballcat.springsecurity.oauth2.server.authorization.client.TestRegisteredClients;
import org.ballcat.springsecurity.oauth2.server.authorization.context.TestAuthorizationServerContext;
import org.ballcat.springsecurity.oauth2.server.authorization.user.TestUsers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.core.*;
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
import org.springframework.security.oauth2.server.authorization.token.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests for {@link OAuth2ResourceOwnerPasswordAuthenticationProvider}.
 *
 * @author Hccake
 */
class OAuth2ResourceOwnerPasswordAuthenticationProviderTests {

	private static final String AUTHORIZATION_URI = "https://provider.com/oauth2/authorize";

	private static final String STATE = "state";

	private AuthenticationManager authenticationManager;

	private OAuth2AuthorizationService authorizationService;

	private JwtEncoder jwtEncoder;

	private OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer;

	private OAuth2TokenCustomizer<OAuth2TokenClaimsContext> accessTokenCustomizer;

	private OAuth2TokenGenerator<?> tokenGenerator;

	private OAuth2ResourceOwnerPasswordAuthenticationProvider authenticationProvider;

	@BeforeEach
	void setUp() {
		this.authenticationManager = mock(AuthenticationManager.class);
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

		this.authenticationProvider = new OAuth2ResourceOwnerPasswordAuthenticationProvider(this.authenticationManager,
				this.authorizationService, this.tokenGenerator);
		AuthorizationServerSettings authorizationServerSettings = AuthorizationServerSettings.builder()
			.issuer("https://provider.com")
			.build();
		AuthorizationServerContextHolder
			.setContext(new TestAuthorizationServerContext(authorizationServerSettings, null));
	}

	@Test
	void constructorWhenAuthenticationManagerNullThenThrowIllegalArgumentException() {
		assertThatThrownBy(() -> new OAuth2ResourceOwnerPasswordAuthenticationProvider(null, this.authorizationService,
				this.tokenGenerator))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("authenticationManager cannot be null");
	}

	@Test
	void constructorWhenAuthorizationServiceNullThenThrowIllegalArgumentException() {
		assertThatThrownBy(() -> new OAuth2ResourceOwnerPasswordAuthenticationProvider(this.authenticationManager, null,
				this.tokenGenerator))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("authorizationService cannot be null");
	}

	@Test
	void constructorWhenOAuth2TokenGeneratorNullThenThrowIllegalArgumentException() {
		assertThatThrownBy(() -> new OAuth2ResourceOwnerPasswordAuthenticationProvider(this.authenticationManager,
				this.authorizationService, null))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("tokenGenerator cannot be null");
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
				"user1", clientPrincipal, new HashSet<>(), new HashMap<>());
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

		User user = TestUsers.user1().build();

		HashMap<String, Object> additionalParameters = new HashMap<>();
		additionalParameters.put(OAuth2ParameterNames.USERNAME, "user1");
		additionalParameters.put(OAuth2ParameterNames.PASSWORD, "password1");
		OAuth2ResourceOwnerPasswordAuthenticationToken authentication = new OAuth2ResourceOwnerPasswordAuthenticationToken(
				"user1", clientPrincipal, new HashSet<>(), additionalParameters);

		when(this.authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
			.thenReturn(new UsernamePasswordAuthenticationToken(user, user.getUsername(), user.getAuthorities()));

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
