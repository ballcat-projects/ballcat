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

package org.ballcat.springsecurity.authentication;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.PathContainer;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

/**
 * 于控制部分接口，需要同时支持匿名登录和授权登录的场景。
 * <p>
 * 与 SpringSecurity 提供的 AnonymousAuthenticationProvider 不同的是，该类不管任何类型的
 * Authentication，都返回匿名对象，以便用户携带了过期或者错误的 token 时转换为匿名用户身份兜底, 可配合
 * {@link GlobalAuthenticationConfigurerAdapter} 使用
 *
 * @see org.springframework.security.authentication.AnonymousAuthenticationProvider
 * @see org.springframework.security.web.authentication.AnonymousAuthenticationFilter
 * @author hccake
 */
@Slf4j
public class AnonymousForeverAuthenticationProvider implements AuthenticationProvider {

	private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new WebAuthenticationDetailsSource();

	private final String key;

	private final Object principal;

	private final List<GrantedAuthority> authorities;

	private final List<PathPattern> pathPatterns;

	public AnonymousForeverAuthenticationProvider(List<String> pathList) {
		if (CollectionUtils.isEmpty(pathList)) {
			this.pathPatterns = new ArrayList<>();
		}
		else {
			this.pathPatterns = pathList.stream()
				.map(PathPatternParser.defaultInstance::parse)
				.collect(Collectors.toList());
		}

		this.key = UUID.randomUUID().toString();
		this.principal = "anonymousUser";
		this.authorities = AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS");
	}

	public AnonymousForeverAuthenticationProvider(String key, Object principal, List<GrantedAuthority> authorities,
			List<PathPattern> pathPatterns) {
		this.key = key;
		this.principal = principal;
		this.authorities = authorities;
		this.pathPatterns = pathPatterns;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		HttpServletRequest request = Optional.ofNullable(RequestContextHolder.getRequestAttributes())
			.map(x -> (ServletRequestAttributes) x)
			.map(ServletRequestAttributes::getRequest)
			.orElse(null);
		if (request == null) {
			return null;
		}

		String requestUri = request.getRequestURI();
		PathContainer pathContainer = PathContainer.parsePath(requestUri);

		boolean anyMatch = this.pathPatterns.stream().anyMatch(x -> x.matches(pathContainer));
		if (anyMatch) {
			Authentication anonymousAuthentication = createAuthentication(request);
			log.debug("Set SecurityContextHolder to anonymous SecurityContext");
			return anonymousAuthentication;
		}

		return null;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return true;
	}

	protected Authentication createAuthentication(HttpServletRequest request) {
		AnonymousAuthenticationToken token = new AnonymousAuthenticationToken(this.key, this.principal,
				this.authorities);
		token.setDetails(this.authenticationDetailsSource.buildDetails(request));
		return token;
	}

	public void setAuthenticationDetailsSource(
			AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource) {
		Assert.notNull(authenticationDetailsSource, "AuthenticationDetailsSource required");
		this.authenticationDetailsSource = authenticationDetailsSource;
	}

}
