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

package org.ballcat.springsecurity.oauth2.server.authorization.web.context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ballcat.springsecurity.oauth2.server.authorization.web.CookieBearerTokenResolver;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.util.StringUtils;

/**
 * OAuth2 下使用的 SecurityContextRepository，从 bearerTokenResolver 中解析对应的 access_token, 然后利用
 * oAuth2AuthorizationService 来获取对应的 SecurityContext
 *
 * @author hccake
 */
public class OAuth2SecurityContextRepository implements SecurityContextRepository {

	private final BearerTokenResolver bearerTokenResolver;

	private final OAuth2AuthorizationService authorizationService;

	public OAuth2SecurityContextRepository(OAuth2AuthorizationService authorizationService) {
		this.bearerTokenResolver = new CookieBearerTokenResolver();
		this.authorizationService = authorizationService;
	}

	public OAuth2SecurityContextRepository(BearerTokenResolver bearerTokenResolver,
			OAuth2AuthorizationService authorizationService) {
		this.bearerTokenResolver = bearerTokenResolver;
		this.authorizationService = authorizationService;
	}

	@Override
	public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
		HttpServletRequest request = requestResponseHolder.getRequest();
		String bearerToken = this.bearerTokenResolver.resolve(request);
		if (!StringUtils.hasText(bearerToken)) {
			return SecurityContextHolder.createEmptyContext();
		}
		OAuth2Authorization oAuth2Authorization = this.authorizationService.findByToken(bearerToken,
				OAuth2TokenType.ACCESS_TOKEN);
		if (oAuth2Authorization == null) {
			return SecurityContextHolder.createEmptyContext();
		}
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) oAuth2Authorization
			.getAttributes()
			.get("java.security.Principal");
		return new SecurityContextImpl(usernamePasswordAuthenticationToken);
	}

	@Override
	public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {

	}

	@Override
	public boolean containsContext(HttpServletRequest request) {
		return this.loadContext(request) != null;
	}

}
