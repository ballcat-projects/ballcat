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

import org.ballcat.springsecurity.properties.SpringSecurityProperties;
import org.springframework.core.Ordered;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;

/**
 * 除了指定的一些 url 之外，其他的全部需要认证后才可访问。
 * <p>
 * Order 必须为最低优先级
 *
 * @author Hccake
 * @since 2.0.0
 */
public class AuthorizeRequestsSpringSecurityConfigurerCustomizer implements SpringSecurityConfigurerCustomizer {

	private final SpringSecurityProperties springSecurityProperties;

	public AuthorizeRequestsSpringSecurityConfigurerCustomizer(SpringSecurityProperties springSecurityProperties) {
		this.springSecurityProperties = springSecurityProperties;
	}

	@Override
	public void customize(HttpSecurity httpSecurity) throws Exception {
		// 拦截 url 配置
		httpSecurity.requestMatcher(AnyRequestMatcher.INSTANCE)
			.authorizeRequests()
			.antMatchers(this.springSecurityProperties.getIgnoreUrls().toArray(new String[0]))
			.permitAll()
			.anyRequest()
			.authenticated();
	}

	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE;
	}

}
