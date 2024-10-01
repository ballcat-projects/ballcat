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

package org.ballcat.springsecurity.oauth2.server.resource;

import lombok.RequiredArgsConstructor;
import org.ballcat.springsecurity.configuration.SpringSecurityAutoConfiguration;
import org.ballcat.springsecurity.configuration.SpringSecurityComponentConfiguration;
import org.ballcat.springsecurity.oauth2.server.resource.configurer.BasicOauth2ResourceServerConfigurerCustomizer;
import org.ballcat.springsecurity.oauth2.server.resource.introspection.BallcatRemoteOpaqueTokenIntrospector;
import org.ballcat.springsecurity.oauth2.server.resource.introspection.SpringAuthorizationServerSharedStoredOpaqueTokenIntrospector;
import org.ballcat.springsecurity.oauth2.server.resource.properties.OAuth2ResourceServerProperties;
import org.ballcat.springsecurity.properties.SpringSecurityProperties;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.resource.authentication.OpaqueTokenAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * 资源服务需要的一些 bean 配置
 *
 * @author hccake
 */
@RequiredArgsConstructor
@AutoConfiguration(before = SpringSecurityAutoConfiguration.class)
@EnableConfigurationProperties({ SpringSecurityProperties.class, OAuth2ResourceServerProperties.class })
public class OAuth2ResourceServerAutoConfiguration {

	@Import(SpringSecurityComponentConfiguration.class)
	@Configuration(proxyBeanMethods = false)
	static class OAuth2ResourceServerConfiguration {

		/**
		 * BearTokenResolve 允许使用 url 传参，方便 ws 连接 ps: 使用 url 传参不安全，待改进
		 * @return DefaultBearerTokenResolver
		 */
		@Bean
		@ConditionalOnMissingBean
		public BearerTokenResolver bearerTokenResolver() {
			DefaultBearerTokenResolver defaultBearerTokenResolver = new DefaultBearerTokenResolver();
			defaultBearerTokenResolver.setAllowUriQueryParameter(true);
			return defaultBearerTokenResolver;
		}

		/**
		 * 资源服务器的定制器
		 */
		@Bean
		@ConditionalOnMissingBean(
				name = BasicOauth2ResourceServerConfigurerCustomizer.BASIC_OAUTH2_RESOURCE_SERVER_CONFIGURER_CUSTOMIZER_BEAN_NAME)
		public BasicOauth2ResourceServerConfigurerCustomizer basicOauth2ResourceServerConfigurerCustomizer(
				ObjectProvider<AuthenticationEntryPoint> authenticationEntryPointObjectProvider,
				BearerTokenResolver bearerTokenResolver) {
			return new BasicOauth2ResourceServerConfigurerCustomizer(authenticationEntryPointObjectProvider,
					bearerTokenResolver);
		}

		/**
		 * spring-security 5.x 中开启资源服务器功能，需要的不透明令牌的支持
		 * @return OpaqueTokenAuthenticationProvider
		 */
		@Bean
		@ConditionalOnMissingBean
		public OpaqueTokenAuthenticationProvider opaqueTokenAuthenticationProvider(
				OpaqueTokenIntrospector opaqueTokenIntrospector) {
			return new OpaqueTokenAuthenticationProvider(opaqueTokenIntrospector);
		}

	}

	/**
	 * 共享令牌配置
	 */
	@ConditionalOnClass(OAuth2AuthorizationService.class)
	@Configuration(proxyBeanMethods = false)
	static class SharedStoredOpaqueTokenIntrospectorConfiguration {

		/**
		 * 当资源服务器和授权服务器的 token 共享存储时，直接使用 OAuth2AuthorizationService 读取 token 信息
		 * @return SpringAuthorizationServerSharedStoredOpaqueTokenIntrospector
		 */
		@Bean
		@ConditionalOnMissingBean(OpaqueTokenIntrospector.class)
		@ConditionalOnProperty(prefix = "ballcat.springsecurity.oauth2.resourceserver", name = "shared-stored-token",
				havingValue = "true")
		public OpaqueTokenIntrospector sharedStoredOpaqueTokenIntrospector(
				OAuth2AuthorizationService authorizationService) {
			return new SpringAuthorizationServerSharedStoredOpaqueTokenIntrospector(authorizationService);
		}

	}

	/**
	 * 不透明令牌处理配置类
	 */
	@Configuration(proxyBeanMethods = false)
	static class OAuth2ResourceServerOpaqueTokenConfiguration {

		/**
		 * 当资源服务器和授权服务器的 token 存储无法共享时，通过远程调用的方式，向授权服务鉴定 token，并同时获取对应的授权信息
		 * @return NimbusOpaqueTokenIntrospector
		 */
		@Bean
		@ConditionalOnMissingBean(OpaqueTokenIntrospector.class)
		@ConditionalOnProperty(prefix = "ballcat.springsecurity.oauth2.resourceserver", name = "shared-stored-token",
				havingValue = "false", matchIfMissing = true)
		public OpaqueTokenIntrospector remoteOpaqueTokenIntrospector(
				OAuth2ResourceServerProperties oAuth2ResourceServerProperties) {
			OAuth2ResourceServerProperties.Opaquetoken opaqueToken = oAuth2ResourceServerProperties.getOpaqueToken();
			return new BallcatRemoteOpaqueTokenIntrospector(opaqueToken.getIntrospectionUri(),
					opaqueToken.getClientId(), opaqueToken.getClientSecret());
		}

	}

}
