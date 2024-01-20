/*
 * Copyright 2002-2022 the original author or authors.
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

package org.ballcat.springsecurity.configuer;

import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.web.authentication.ForwardAuthenticationFailureHandler;
import org.springframework.security.web.authentication.ForwardAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * 前后端分离登录配置类，区别于
 * {@link org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer},
 * 此配置类不会提供内置表单登录页
 *
 * @author Hccake
 * @since 2.0.0
 */
public final class SeparationLoginConfigurer<H extends HttpSecurityBuilder<H>> extends
		AbstractAuthenticationFilterConfigurer<H, SeparationLoginConfigurer<H>, UsernamePasswordAuthenticationFilter> {

	/**
	 * Creates a new instance
	 *
	 * @see HttpSecurity#formLogin()
	 */
	public SeparationLoginConfigurer() {
		super(new UsernamePasswordAuthenticationFilter(), null);
		usernameParameter("username");
		passwordParameter("password");
	}

	@Override
	public SeparationLoginConfigurer<H> loginPage(String loginPage) {
		return super.loginPage(loginPage);
	}

	/**
	 * The HTTP parameter to look for the username when performing authentication. Default
	 * is "username".
	 * @param usernameParameter the HTTP parameter to look for the username when
	 * performing authentication
	 * @return the {@link SeparationLoginConfigurer} for additional customization
	 */
	public SeparationLoginConfigurer<H> usernameParameter(String usernameParameter) {
		getAuthenticationFilter().setUsernameParameter(usernameParameter);
		return this;
	}

	/**
	 * The HTTP parameter to look for the password when performing authentication. Default
	 * is "password".
	 * @param passwordParameter the HTTP parameter to look for the password when
	 * performing authentication
	 * @return the {@link SeparationLoginConfigurer} for additional customization
	 */
	public SeparationLoginConfigurer<H> passwordParameter(String passwordParameter) {
		getAuthenticationFilter().setPasswordParameter(passwordParameter);
		return this;
	}

	/**
	 * Forward Authentication Failure Handler
	 * @param forwardUrl the target URL in case of failure
	 * @return the {@link SeparationLoginConfigurer} for additional customization
	 */
	public SeparationLoginConfigurer<H> failureForwardUrl(String forwardUrl) {
		failureHandler(new ForwardAuthenticationFailureHandler(forwardUrl));
		return this;
	}

	/**
	 * Forward Authentication Success Handler
	 * @param forwardUrl the target URL in case of success
	 * @return the {@link SeparationLoginConfigurer} for additional customization
	 */
	public SeparationLoginConfigurer<H> successForwardUrl(String forwardUrl) {
		successHandler(new ForwardAuthenticationSuccessHandler(forwardUrl));
		return this;
	}

	@Override
	public void init(H http) throws Exception {
		super.init(http);
	}

	@Override
	protected RequestMatcher createLoginProcessingUrlMatcher(String loginProcessingUrl) {
		return new AntPathRequestMatcher(loginProcessingUrl, "POST");
	}

	/**
	 * Gets the HTTP parameter that is used to submit the username.
	 * @return the HTTP parameter that is used to submit the username
	 */
	private String getUsernameParameter() {
		return getAuthenticationFilter().getUsernameParameter();
	}

	/**
	 * Gets the HTTP parameter that is used to submit the password.
	 * @return the HTTP parameter that is used to submit the password
	 */
	private String getPasswordParameter() {
		return getAuthenticationFilter().getPasswordParameter();
	}

}
