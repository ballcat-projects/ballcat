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

package org.ballcat.springsecurity.oauth2.server.authorization.autoconfigure;

import lombok.RequiredArgsConstructor;
import org.ballcat.springsecurity.oauth2.server.authorization.config.configurer.OAuth2ResourceOwnerPasswordConfigurerExtension;
import org.ballcat.springsecurity.oauth2.server.authorization.config.configurer.OAuth2TokenResponseEnhanceConfigurerExtension;
import org.ballcat.springsecurity.oauth2.server.authorization.config.configurer.OAuth2TokenRevocationEndpointConfigurerExtension;
import org.ballcat.springsecurity.oauth2.server.authorization.properties.OAuth2AuthorizationServerProperties;
import org.ballcat.springsecurity.oauth2.server.authorization.web.authentication.OAuth2TokenResponseEnhancer;
import org.ballcat.springsecurity.oauth2.server.authorization.web.authentication.OAuth2TokenRevocationResponseHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;

/**
 * OAuth2 授权服务器配置定制器的配置类
 *
 * @author Hccake
 */
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class OAuth2AuthorizationServerConfigurerExtensionConfiguration {

	private final OAuth2AuthorizationService oAuth2AuthorizationService;

	/**
	 * 添加 resource owner password 模式支持配置定制器
	 * @param applicationContext spring 容器
	 * @return OAuth2ResourceOwnerPasswordConfigurerCustomizer
	 */
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(prefix = OAuth2AuthorizationServerProperties.PREFIX, name = "password-grant-type.enabled",
			havingValue = "true")
	public OAuth2ResourceOwnerPasswordConfigurerExtension oAuth2ResourceOwnerPasswordConfigurerCustomizer(
			ApplicationContext applicationContext) {
		return new OAuth2ResourceOwnerPasswordConfigurerExtension(applicationContext);
	}

	/**
	 * token endpoint 响应增强配置定制器
	 * @param oauth2TokenResponseEnhancer OAuth2TokenResponseEnhancer
	 * @return OAuth2TokenResponseEnhanceConfigurerCustomizer
	 */
	@Bean
	@ConditionalOnBean(OAuth2TokenResponseEnhancer.class)
	public OAuth2TokenResponseEnhanceConfigurerExtension oAuth2TokenResponseEnhanceConfigurerCustomizer(
			OAuth2TokenResponseEnhancer oauth2TokenResponseEnhancer) {
		return new OAuth2TokenResponseEnhanceConfigurerExtension(oauth2TokenResponseEnhancer);
	}

	/**
	 * OAuth2 Token 撤销响应处理器
	 * @param publisher 事件发布器
	 * @return OAuth2TokenRevocationResponseHandler
	 */
	@Bean
	@ConditionalOnMissingBean
	public OAuth2TokenRevocationResponseHandler oAuth2TokenRevocationResponseHandler(
			ApplicationEventPublisher publisher) {
		return new OAuth2TokenRevocationResponseHandler(publisher);
	}

	/**
	 * token 撤销响应处理器配置定制器
	 * @param oAuth2TokenRevocationResponseHandler token 撤销响应处理器
	 * @return OAuth2TokenRevocationResponseHandler
	 */
	@Bean
	@ConditionalOnBean(OAuth2TokenRevocationResponseHandler.class)
	public OAuth2TokenRevocationEndpointConfigurerExtension oAuth2TokenRevocationEndpointConfigurerCustomizer(
			OAuth2TokenRevocationResponseHandler oAuth2TokenRevocationResponseHandler) {
		return new OAuth2TokenRevocationEndpointConfigurerExtension(this.oAuth2AuthorizationService,
				oAuth2TokenRevocationResponseHandler);
	}

}
