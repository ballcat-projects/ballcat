package com.hccake.ballcat.common.security.userdetails;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;

import java.util.*;

/**
 * OAuth2 Client 实体封装对象
 *
 * @author hccake
 */
public class ClientPrincipal implements OAuth2AuthenticatedPrincipal {

	private final String clientId;

	private final Map<String, Object> attributes;

	private final Collection<? extends GrantedAuthority> authorities;

	private Set<String> scope = new HashSet<>();

	public Set<String> getScope() {
		return scope;
	}

	public void setScope(Collection<String> scope) {
		this.scope = Collections.unmodifiableSet(scope == null ? new LinkedHashSet<>() : new LinkedHashSet<>(scope));
	}

	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getName() {
		return clientId;
	}

	public ClientPrincipal(String clientId, Map<String, Object> attributes,
			Collection<? extends GrantedAuthority> authorities) {
		this.clientId = clientId;
		this.attributes = attributes;
		this.authorities = (authorities != null) ? Collections.unmodifiableCollection(authorities)
				: AuthorityUtils.NO_AUTHORITIES;
	}

}
