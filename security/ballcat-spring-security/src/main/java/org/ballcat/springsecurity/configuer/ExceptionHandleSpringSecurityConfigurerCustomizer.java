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

package org.ballcat.springsecurity.configuer;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * 异常处理配置定制器
 *
 * @author Hccake
 * @since 2.0.0
 */
public class ExceptionHandleSpringSecurityConfigurerCustomizer implements SpringSecurityConfigurerCustomizer {

	private final AuthenticationEntryPoint authenticationEntryPoint;

	public ExceptionHandleSpringSecurityConfigurerCustomizer(AuthenticationEntryPoint authenticationEntryPoint) {
		this.authenticationEntryPoint = authenticationEntryPoint;
	}

	@Override
	public void customize(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.exceptionHandling().authenticationEntryPoint(this.authenticationEntryPoint);
	}

	@Override
	public int getOrder() {
		return 1000;
	}

}
