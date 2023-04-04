package org.ballcat.springsecurity.oauth2.server.authorization.authentication;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken;
import org.springframework.util.Assert;

import java.util.*;

/**
 * An {@link Authentication} implementation used for the OAuth 2.0 Resource Owner Password
 * Grant.
 *
 * @author Hccake
 * @since 1.0.0
 * @see OAuth2AuthorizationGrantAuthenticationToken
 * @see OAuth2ResourceOwnerPasswordAuthenticationProvider
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class OAuth2ResourceOwnerPasswordAuthenticationToken extends OAuth2AuthorizationGrantAuthenticationToken {

	private final String username;

	private final Set<String> scopes;

	/**
	 * Constructs an {@code OAuth2ClientCredentialsAuthenticationToken} using the provided
	 * parameters.
	 * @param clientPrincipal the authenticated client principal
	 */
	public OAuth2ResourceOwnerPasswordAuthenticationToken(String username, Authentication clientPrincipal,
			@Nullable Set<String> scopes, @Nullable Map<String, Object> additionalParameters) {
		super(AuthorizationGrantType.PASSWORD, clientPrincipal, additionalParameters);
		Assert.hasText(username, "username cannot be empty");
		this.username = username;
		this.scopes = Collections.unmodifiableSet(scopes != null ? new HashSet<>(scopes) : Collections.emptySet());
	}

	/**
	 * Returns the requested scope(s).
	 * @return the requested scope(s), or an empty {@code Set} if not available
	 */
	public Set<String> getScopes() {
		return this.scopes;
	}

	@Override
	public String getName() {
		return username;
	}

}
