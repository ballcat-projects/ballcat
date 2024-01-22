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

package org.ballcat.springsecurity.oauth2.userdetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;

/**
 * OAuth2 Client 实体封装对象
 *
 * @author hccake
 */
public class ClientPrincipal implements OAuth2AuthenticatedPrincipal, Serializable {

	private final String clientId;

	private final Map<String, Object> attributes;

	private final Collection<? extends GrantedAuthority> authorities;

	private Set<String> scope = new HashSet<>();

	@NonNull
	public Set<String> getScope() {
		return this.scope;
	}

	public void setScope(Collection<String> scope) {
		this.scope = Collections.unmodifiableSet(scope == null ? new LinkedHashSet<>() : new LinkedHashSet<>(scope));
	}

	@Override
	public Map<String, Object> getAttributes() {
		return this.attributes;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	@Override
	public String getName() {
		return this.clientId;
	}

	public ClientPrincipal(String clientId, Map<String, Object> attributes,
			Collection<? extends GrantedAuthority> authorities) {
		this.clientId = clientId;
		this.attributes = attributes;
		this.authorities = (authorities != null) ? Collections.unmodifiableCollection(authorities)
				: AuthorityUtils.NO_AUTHORITIES;
	}

}
