package com.hccake.ballcat.auth.authentication;

import com.hccake.ballcat.common.security.userdetails.ClientPrincipal;
import lombok.EqualsAndHashCode;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Collections;

/**
 * An {@link Authentication} implementation used for OAuth 2.0 Client Authentication.
 *
 * @author hccake
 * @see AbstractAuthenticationToken
 */
@EqualsAndHashCode(callSuper = true)
public class OAuth2ClientAuthenticationToken extends AbstractAuthenticationToken {

	private static final long serialVersionUID = 1L;

	private final ClientPrincipal clientPrincipal;

	private final Object credentials;

	public OAuth2ClientAuthenticationToken(ClientPrincipal clientPrincipal, @Nullable Object credentials) {
		super(Collections.emptyList());
		this.clientPrincipal = clientPrincipal;
		this.credentials = credentials;
		setAuthenticated(true);
	}

	@Override
	public Object getPrincipal() {
		return this.clientPrincipal;
	}

	@Nullable
	@Override
	public Object getCredentials() {
		return this.credentials;
	}

}
