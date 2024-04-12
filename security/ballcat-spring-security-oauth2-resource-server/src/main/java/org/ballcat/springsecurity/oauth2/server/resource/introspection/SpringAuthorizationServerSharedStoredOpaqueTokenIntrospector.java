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

package org.ballcat.springsecurity.oauth2.server.resource.introspection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;
import org.ballcat.springsecurity.exception.InternalServiceException;
import org.ballcat.springsecurity.oauth2.userdetails.ClientPrincipal;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.resource.introspection.BadOpaqueTokenException;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.util.CollectionUtils;

/**
 * 共享存储的不透明令牌的处理器
 * <p>
 * 当资源服务器与授权服务器共享了 TokenStore, 比如使用 RedisTokenStore 时共享了 redis，或者 jdbcTokenStore 时，共享了数据库。
 * 此时可以直接使用 spring-authorization-server 中的 OAuth2AuthorizationService 进行读取。
 *
 * @author hccake
 */
@Slf4j
public class SpringAuthorizationServerSharedStoredOpaqueTokenIntrospector implements OpaqueTokenIntrospector {

	private static final String AUTHORITY_SCOPE_PREFIX = "SCOPE_";

	private final OAuth2AuthorizationService authorizationService;

	public SpringAuthorizationServerSharedStoredOpaqueTokenIntrospector(
			OAuth2AuthorizationService authorizationService) {
		this.authorizationService = authorizationService;
	}

	/**
	 * @param accessTokenValue token
	 * @return OAuth2AuthenticatedPrincipal
	 */
	@Override
	public OAuth2AuthenticatedPrincipal introspect(String accessTokenValue) {
		OAuth2Authorization authorization;
		try {
			authorization = this.authorizationService.findByToken(accessTokenValue, null);
		}
		catch (Exception ex) {
			log.error("An error occurred while attempting to find OAuth2 Authorization by token: {}", accessTokenValue,
					ex);
			throw new InternalServiceException(
					"An error occurred while attempting to find OAuth2 Authorization by token");
		}
		if (authorization == null) {
			if (log.isTraceEnabled()) {
				log.trace("Did not authenticate token introspection request since token was not found");
			}
			throw new BadOpaqueTokenException("Invalid access token: " + accessTokenValue);
		}

		OAuth2Authorization.Token<OAuth2Token> authorizedToken = authorization.getToken(accessTokenValue);
		if (authorizedToken == null || !authorizedToken.isActive()) {
			if (log.isTraceEnabled()) {
				log.trace("Did not validate token since it is inactive");
			}
			throw new BadOpaqueTokenException("Provided token isn't active");
		}

		AuthorizationGrantType authorizationGrantType = authorization.getAuthorizationGrantType();
		if (AuthorizationGrantType.CLIENT_CREDENTIALS.equals(authorizationGrantType)) {
			return getClientPrincipal(authorization);
		}

		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) authorization
			.getAttributes()
			.get("java.security.Principal");
		return (OAuth2AuthenticatedPrincipal) usernamePasswordAuthenticationToken.getPrincipal();
	}

	private ClientPrincipal getClientPrincipal(OAuth2Authorization oAuth2Authentication) {
		ClientPrincipal clientPrincipal;

		Set<String> authorizedScopes = oAuth2Authentication.getAuthorizedScopes();

		Collection<GrantedAuthority> authorities = new ArrayList<>();
		if (!CollectionUtils.isEmpty(authorizedScopes)) {
			for (String scope : authorizedScopes) {
				authorities.add(new SimpleGrantedAuthority(AUTHORITY_SCOPE_PREFIX + scope));
			}
		}
		clientPrincipal = new ClientPrincipal(oAuth2Authentication.getPrincipalName(), new HashMap<>(8), authorities);
		clientPrincipal.setScope(authorizedScopes);

		return clientPrincipal;
	}

}
