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

package org.ballcat.security.configuration;

import org.ballcat.security.access.expression.DefaultSecurityExpressionHandler;
import org.ballcat.security.access.expression.SecurityExpressionHandler;
import org.ballcat.security.authorization.AuthorizeInterceptor;
import org.ballcat.security.authorization.AuthorizeManager;
import org.ballcat.security.authorization.DefaultAuthorizeManager;
import org.ballcat.security.authorization.SecurityChecker;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * 权限自动配置
 *
 * @author ballcat
 * @since 2.0.0
 */
@AutoConfiguration
public class BallcatSecurityAutoConfiguration {

	/**
	 * 默认的权限表达式处理器
	 * @param securityChecker 权限检查器
	 * @return SecurityExpressionHandler
	 */
	@Bean
	@ConditionalOnMissingBean(SecurityExpressionHandler.class)
	public SecurityExpressionHandler securityExpressionHandler(SecurityChecker securityChecker) {
		return new DefaultSecurityExpressionHandler(securityChecker);
	}

	/**
	 * 权限管理器
	 * @param securityExpressionHandler 权限表达式处理器
	 * @return AuthorizeManager
	 */
	@Bean
	@ConditionalOnMissingBean(AuthorizeManager.class)
	public AuthorizeManager authorizeManager(SecurityExpressionHandler securityExpressionHandler) {
		return new DefaultAuthorizeManager(securityExpressionHandler);
	}

	/**
	 * 权限注解拦截器
	 * @param authorizeManager 权限管理器
	 * @return AuthorizeInterceptor
	 */
	@Bean
	@ConditionalOnMissingBean(AuthorizeInterceptor.class)
	public AuthorizeInterceptor authorizeInterceptor(AuthorizeManager authorizeManager) {
		return new AuthorizeInterceptor(authorizeManager);
	}

}
