/*
 * Copyright 2023 the original author or authors.
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

import org.ballcat.security.properties.SecurityProperties;
import org.ballcat.springsecurity.properties.SpringSecurityProperties;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.ConditionalOnDefaultWebSecurity;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@EnableConfigurationProperties(SecurityProperties.class)
@Configuration(proxyBeanMethods = false)
@ConditionalOnDefaultWebSecurity
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@Import(SpringSecurityFilterChainConfiguration.class)
public class SpringSecurityWebSecurityConfiguration {

	private final SecurityProperties securityProperties;

	private final SpringSecurityProperties springSecurityProperties;

	public SpringSecurityWebSecurityConfiguration(SecurityProperties securityProperties,
			SpringSecurityProperties springSecurityProperties) {
		this.securityProperties = securityProperties;
		this.springSecurityProperties = springSecurityProperties;
	}

	@Bean
	@ConditionalOnMissingBean
	public AuthorizeRequestsSpringSecurityConfigurerCustomizer springSecurityAnyRequestAuthenticatedCustomizer() {
		return new AuthorizeRequestsSpringSecurityConfigurerCustomizer(springSecurityProperties);
	}

	@Bean
	@ConditionalOnBean(AuthenticationEntryPoint.class)
	@ConditionalOnMissingBean
	public ExceptionHandleSpringSecurityConfigurerCustomizer exceptionHandleSpringSecurityConfigurerCustomizer(
			AuthenticationEntryPoint authenticationEntryPoint) {
		return new ExceptionHandleSpringSecurityConfigurerCustomizer(authenticationEntryPoint);
	}

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(prefix = "ballcat.springsecurity.form-login", name = { "enabled", "separated" },
			havingValue = "true")
	public SeparationLoginSpringSecurityConfigurerCustomizer springSecurityFormLoginConfigurerCustomizer(
			ObjectProvider<AuthenticationEntryPoint> authenticationEntryPointObjectProvider,
			ObjectProvider<AuthenticationSuccessHandler> authenticationSuccessHandlerObjectProvider,
			ObjectProvider<LogoutSuccessHandler> logoutSuccessHandlerObjectProvider) {
		SeparationLoginSpringSecurityConfigurerCustomizer customizer = new SeparationLoginSpringSecurityConfigurerCustomizer(
				springSecurityProperties, authenticationEntryPointObjectProvider.getIfAvailable(),
				authenticationSuccessHandlerObjectProvider.getIfAvailable(),
				logoutSuccessHandlerObjectProvider.getIfAvailable());
		customizer.setPasswordAesSecretKey(securityProperties.getPasswordSecretKey());
		return customizer;
	}

}