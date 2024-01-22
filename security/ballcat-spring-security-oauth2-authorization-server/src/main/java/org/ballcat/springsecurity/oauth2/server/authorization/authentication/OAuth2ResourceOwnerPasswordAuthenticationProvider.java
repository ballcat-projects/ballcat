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

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.util.Assert;

/**
 * @author Hccake
 */
@Slf4j
public class OAuth2ResourceOwnerPasswordAuthenticationProvider
		extends AbstractOAuth2ResourceOwnerAuthenticationProvider<OAuth2ResourceOwnerPasswordAuthenticationToken> {

	private final DaoAuthenticationProvider daoAuthenticationProvider;

	/**
	 * Constructs an {@code OAuth2ResourceOwnerPasswordAuthenticationProviderNew} using
	 * the provided parameters.
	 * @param userDetailsService the userDetails service
	 * @param authorizationService the authorization service
	 * @param tokenGenerator the token generator
	 * @since 1.0.0
	 */
	public OAuth2ResourceOwnerPasswordAuthenticationProvider(OAuth2AuthorizationService authorizationService,
			OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator, UserDetailsService userDetailsService) {
		super(authorizationService, tokenGenerator);
		Assert.notNull(userDetailsService, "userDetailsService cannot be null");
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService);
		this.daoAuthenticationProvider = provider;
	}

	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		Assert.notNull(passwordEncoder, "passwordEncoder cannot be null");
		this.daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		boolean supports = OAuth2ResourceOwnerPasswordAuthenticationToken.class.isAssignableFrom(authentication);
		log.debug("supports authentication={}} returning {}", authentication, supports);
		return supports;
	}

	@Override
	protected Authentication getAuthenticatedAuthentication(
			OAuth2ResourceOwnerPasswordAuthenticationToken resourceOwnerPasswordAuthentication) {
		Map<String, Object> additionalParameters = resourceOwnerPasswordAuthentication.getAdditionalParameters();

		String username = (String) additionalParameters.get(OAuth2ParameterNames.USERNAME);
		String password = (String) additionalParameters.get(OAuth2ParameterNames.PASSWORD);

		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
				username, password);
		log.debug("got usernamePasswordAuthenticationToken={}", usernamePasswordAuthenticationToken);

		return this.daoAuthenticationProvider.authenticate(usernamePasswordAuthenticationToken);
	}

}
