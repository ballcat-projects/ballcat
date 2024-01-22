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

package org.ballcat.springsecurity.authorization;

import java.util.Collection;
import java.util.Set;

import org.ballcat.security.authorization.SecurityChecker;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * ballcat 定义的 SecurityChecker 的 spring security 实现
 *
 * @author Hccake
 * @see DefaultMethodSecurityExpressionHandler
 * @since 2.0.0
 */
public class SpringSecurityChecker implements SecurityChecker, ApplicationContextAware {

	private RoleHierarchy roleHierarchy;

	private String defaultRolePrefix = "ROLE_";

	private AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();

	@Override
	public boolean hasPermission(String permission) {
		return hasAnyPermission(permission);
	}

	@Override
	public boolean hasAnyPermission(String... permissions) {
		return hasAnyAuthorityName(null, permissions);
	}

	@Override
	public boolean hasRole(String role) {
		return hasAnyRole(role);
	}

	@Override
	public boolean hasAnyRole(String... roles) {
		return hasAnyAuthorityName(this.defaultRolePrefix, roles);
	}

	@Override
	public boolean isAnonymous() {
		return this.trustResolver.isAnonymous(getAuthentication());
	}

	@Override
	public boolean isAuthenticated() {
		return !isAnonymous();
	}

	private Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	private Set<String> getAuthoritySet() {
		Collection<? extends GrantedAuthority> userAuthorities = getAuthentication().getAuthorities();
		if (this.roleHierarchy != null) {
			userAuthorities = this.roleHierarchy.getReachableGrantedAuthorities(userAuthorities);
		}
		return AuthorityUtils.authorityListToSet(userAuthorities);
	}

	private boolean hasAnyAuthorityName(String prefix, String... roles) {
		Set<String> roleSet = getAuthoritySet();
		for (String role : roles) {
			// 如果有设置前缀，则需要校验角色前缀
			if (roleSet.contains(role)) {
				if (prefix != null && prefix.length() > 0) {
					return role.startsWith(prefix);
				}
				else {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 使属性和 security 配置一致
	 * @param applicationContext spring 上下文容器
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.roleHierarchy = getSingleBeanOrNull(applicationContext, RoleHierarchy.class);
		GrantedAuthorityDefaults grantedAuthorityDefaults = getSingleBeanOrNull(applicationContext,
				GrantedAuthorityDefaults.class);
		if (grantedAuthorityDefaults != null) {
			this.defaultRolePrefix = grantedAuthorityDefaults.getRolePrefix();
		}
		AuthenticationTrustResolver trustResolver = getSingleBeanOrNull(applicationContext,
				AuthenticationTrustResolver.class);
		if (trustResolver != null) {
			this.trustResolver = trustResolver;
		}
	}

	private <T> T getSingleBeanOrNull(ApplicationContext applicationContext, Class<T> type) {
		try {
			return applicationContext.getBean(type);
		}
		catch (NoSuchBeanDefinitionException ignored) {
		}
		return null;
	}

}
