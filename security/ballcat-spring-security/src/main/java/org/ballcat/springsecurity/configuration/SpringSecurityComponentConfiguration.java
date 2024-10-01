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

package org.ballcat.springsecurity.configuration;

import org.ballcat.security.authorization.SecurityChecker;
import org.ballcat.springsecurity.authorization.SpringSecurityChecker;
import org.ballcat.springsecurity.component.CustomPermissionEvaluator;
import org.ballcat.springsecurity.util.PasswordUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Hccake
 * @since 2.0.0
 */
@Configuration(proxyBeanMethods = false)
public class SpringSecurityComponentConfiguration {

	/**
	 * 密码管理器
	 */
	@Bean
	@ConditionalOnBean(UserDetailsService.class)
	@ConditionalOnMissingBean
	public PasswordEncoder passwordEncoder() {
		return PasswordUtils.createDelegatingPasswordEncoder();
	}

	/**
	 * login 登录支持。
	 * <p>
	 * 注册为 spring bean 是为了解决 spring security 默认配置行为是 new 了一个 DaoAuthenticationProvider，没有被
	 * spring 管理，导致 messageSource 没有被替换，国际化失效。
	 * @see org.springframework.security.config.annotation.authentication.configuration.InitializeUserDetailsBeanManagerConfigurer
	 */
	@Bean
	@ConditionalOnBean(UserDetailsService.class)
	@ConditionalOnMissingBean
	public DaoAuthenticationProvider daoAuthenticationProvider(UserDetailsService userDetailsService,
			PasswordEncoder passwordEncoder) {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(userDetailsService);
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
		return daoAuthenticationProvider;
	}

	/**
	 * 基于 spring security 的权限判断组件
	 * @return SpringSecurityChecker
	 */
	@Bean
	@ConditionalOnMissingBean
	public SecurityChecker springSecurityChecker() {
		return new SpringSecurityChecker();
	}

	/**
	 * 自定义的权限判断组件
	 * @return CustomPermissionEvaluator
	 */
	@Bean(name = "per")
	@ConditionalOnMissingBean(CustomPermissionEvaluator.class)
	public CustomPermissionEvaluator customPermissionEvaluator() {
		return new CustomPermissionEvaluator();
	}

}
