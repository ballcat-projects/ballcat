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

import java.util.Collections;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.ballcat.springsecurity.properties.SpringSecurityProperties;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * spring security filter chain 的默认配置类，用户可以通过自定义 SecurityFilterChain 覆盖其默认注册行为, 也可以通过注册
 * {@link SpringSecurityConfigurerCustomizer} 类型的定制器，扩展默认配置。
 *
 * @author Hccake
 * @since 2.0.0
 */
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class SpringSecurityFilterChainConfiguration {

	private final SpringSecurityProperties springSecurityProperties;

	private final ObjectProvider<List<SpringSecurityConfigurerCustomizer>> configurerCustomizersProvider;

	@Bean
	@Order(SecurityProperties.BASIC_AUTH_ORDER)
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
		// 关闭 csrf 跨站攻击防护
		httpSecurity.csrf().disable();

		// 允许嵌入iframe
		if (!this.springSecurityProperties.isIframeDeny()) {
			httpSecurity.headers().frameOptions().disable();
		}

		// 使用无状态登录时，需要配合自定义的 SecurityContextRepository
		SessionCreationPolicy sessionCreationPolicy = this.springSecurityProperties.getSessionCreationPolicy();
		httpSecurity.sessionManagement().sessionCreationPolicy(sessionCreationPolicy);

		// 自定义处理
		List<SpringSecurityConfigurerCustomizer> configurerCustomizers = this.configurerCustomizersProvider
			.getIfAvailable(Collections::emptyList);
		for (SpringSecurityConfigurerCustomizer configurerCustomizer : configurerCustomizers) {
			configurerCustomizer.customize(httpSecurity);
		}

		return httpSecurity.build();
	}

}
