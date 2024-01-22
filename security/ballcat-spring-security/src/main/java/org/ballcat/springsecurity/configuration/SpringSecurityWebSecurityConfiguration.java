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

import org.ballcat.security.captcha.CaptchaValidator;
import org.ballcat.security.properties.SecurityProperties;
import org.ballcat.springsecurity.configuer.AuthorizeRequestsSpringSecurityConfigurerCustomizer;
import org.ballcat.springsecurity.configuer.ExceptionHandleSpringSecurityConfigurerCustomizer;
import org.ballcat.springsecurity.configuer.FormLoginSpringSecurityConfigurerCustomizer;
import org.ballcat.springsecurity.configuer.LoginCaptchaSpringSecurityConfigurerCustomizer;
import org.ballcat.springsecurity.configuer.LoginPasswordDecodeSpringSecurityConfigurerCustomizer;
import org.ballcat.springsecurity.configuer.SeparationLoginSpringSecurityConfigurerCustomizer;
import org.ballcat.springsecurity.configuer.SpringSecurityFilterChainConfiguration;
import org.ballcat.springsecurity.properties.SpringSecurityProperties;
import org.ballcat.springsecurity.web.FormLoginSuccessHandler;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@EnableConfigurationProperties(SecurityProperties.class)
@Configuration(proxyBeanMethods = false)
@ConditionalOnDefaultWebSecurity
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@Import(SpringSecurityFilterChainConfiguration.class)
public class SpringSecurityWebSecurityConfiguration {

	private final SpringSecurityProperties springSecurityProperties;

	public SpringSecurityWebSecurityConfiguration(SpringSecurityProperties springSecurityProperties) {
		this.springSecurityProperties = springSecurityProperties;
	}

	@Bean
	@ConditionalOnMissingBean
	public AuthorizeRequestsSpringSecurityConfigurerCustomizer springSecurityAnyRequestAuthenticatedCustomizer() {
		return new AuthorizeRequestsSpringSecurityConfigurerCustomizer(this.springSecurityProperties);
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
			ObjectProvider<FormLoginSuccessHandler> formLoginSuccessHandlerObjectProvider,
			ObjectProvider<LogoutSuccessHandler> logoutSuccessHandlerObjectProvider) {
		return new SeparationLoginSpringSecurityConfigurerCustomizer(this.springSecurityProperties,
				authenticationEntryPointObjectProvider.getIfAvailable(),
				formLoginSuccessHandlerObjectProvider.getIfAvailable(),
				logoutSuccessHandlerObjectProvider.getIfAvailable());
	}

	@Bean
	@ConditionalOnMissingBean({ SeparationLoginSpringSecurityConfigurerCustomizer.class,
			FormLoginSpringSecurityConfigurerCustomizer.class })
	@ConditionalOnProperty(prefix = "ballcat.springsecurity.form-login", name = "enabled", havingValue = "true")
	public FormLoginSpringSecurityConfigurerCustomizer formLoginSpringSecurityConfigurerCustomizer(
			ObjectProvider<AuthenticationEntryPoint> authenticationEntryPointObjectProvider,
			ObjectProvider<FormLoginSuccessHandler> formLoginSuccessHandlerObjectProvider,
			ObjectProvider<LogoutSuccessHandler> logoutSuccessHandlerObjectProvider) {
		return new FormLoginSpringSecurityConfigurerCustomizer(this.springSecurityProperties,
				authenticationEntryPointObjectProvider.getIfAvailable(),
				formLoginSuccessHandlerObjectProvider.getIfAvailable(),
				logoutSuccessHandlerObjectProvider.getIfAvailable());
	}

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(prefix = "ballcat.springsecurity.form-login", name = "login-captcha", havingValue = "true")
	public LoginCaptchaSpringSecurityConfigurerCustomizer loginCaptchaSpringSecurityConfigurerCustomizer(
			CaptchaValidator captchaValidator) {
		return new LoginCaptchaSpringSecurityConfigurerCustomizer(this.springSecurityProperties, captchaValidator);
	}

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(prefix = "ballcat.security", name = "password-secret-key")
	public LoginPasswordDecodeSpringSecurityConfigurerCustomizer loginPasswordDecodeSpringSecurityConfigurerCustomizer(
			@Value("${ballcat.security.password-secret-key}") String passwordSecretKey) {
		return new LoginPasswordDecodeSpringSecurityConfigurerCustomizer(this.springSecurityProperties,
				passwordSecretKey);
	}

}
