package com.hccake.ballcat.common.security.authentication;

import lombok.EqualsAndHashCode;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

import java.util.Collection;

/**
 * @author chengbohua
 */
@EqualsAndHashCode(callSuper = true)
public class OAuth2UserAuthenticationToken extends AbstractAuthenticationToken {

	private final Object principal;

	/**
	 * Constructs an {@code Oauth2UserInfoAuthenticationToken} using the provided
	 * parameters.
	 * @param principal the principal
	 */
	public OAuth2UserAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		Assert.notNull(principal, "principal cannot be null");
		this.principal = principal;
		setAuthenticated(true);
	}

	@Override
	public Object getPrincipal() {
		return this.principal;
	}

	@Override
	public Object getCredentials() {
		return "";
	}

}
