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

import java.util.Map;
import java.util.Set;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.util.Assert;

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
		return this.username;
	}

}
