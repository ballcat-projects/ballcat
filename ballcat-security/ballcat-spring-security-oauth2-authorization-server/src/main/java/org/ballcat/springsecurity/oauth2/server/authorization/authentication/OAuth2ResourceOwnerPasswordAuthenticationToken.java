package org.ballcat.springsecurity.oauth2.server.authorization.authentication;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.Set;

/**
 * An {@link Authentication} implementation used for the OAuth 2.0 Resource Owner Password
 * Grant.
 *
 * @author Hccake
 * @since 1.0.0
 * @see AbstractOAuth2ResourceOwnerAuthenticationToken
 * @see OAuth2ResourceOwnerPasswordAuthenticationProvider
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class OAuth2ResourceOwnerPasswordAuthenticationToken extends AbstractOAuth2ResourceOwnerAuthenticationToken {

	private final String username;

	/**
	 * Constructs an {@code OAuth2ClientCredentialsAuthenticationToken} using the provided
	 * parameters.
	 * @param clientPrincipal the authenticated client principal
	 */
	public OAuth2ResourceOwnerPasswordAuthenticationToken(String username, Authentication clientPrincipal,
			@Nullable Map<String, Object> additionalParameters, @Nullable Set<String> scopes) {
		super(AuthorizationGrantType.PASSWORD, clientPrincipal, additionalParameters, scopes);
		Assert.hasText(username, "username cannot be empty");
		this.username = username;
	}

	@Override
	public String getName() {
		return username;
	}

}
