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

import java.util.List;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.ballcat.springsecurity.configuration.SpringSecurityAutoConfiguration;
import org.ballcat.springsecurity.oauth2.authentication.OAuth2UserAuthenticationToken;
import org.ballcat.springsecurity.oauth2.jackson2.LongMixin;
import org.ballcat.springsecurity.oauth2.jackson2.OAuth2UserAuthenticationTokenMixin;
import org.ballcat.springsecurity.oauth2.jackson2.UserMixin;
import org.ballcat.springsecurity.oauth2.server.authorization.OAuth2AuthorizationObjectMapperCustomizer;
import org.ballcat.springsecurity.oauth2.server.authorization.config.configurer.OAuth2AuthorizationServerConfigurerExtension;
import org.ballcat.springsecurity.oauth2.server.authorization.config.customizer.BasicOAuth2AuthorizationServerConfigurerCustomizer;
import org.ballcat.springsecurity.oauth2.server.authorization.config.customizer.OAuth2AuthorizationServerConfigurerAdapter;
import org.ballcat.springsecurity.oauth2.server.authorization.properties.OAuth2AuthorizationServerProperties;
import org.ballcat.springsecurity.oauth2.server.authorization.token.BallcatOAuth2TokenCustomizer;
import org.ballcat.springsecurity.oauth2.userdetails.DefaultOAuth2User;
import org.ballcat.springsecurity.properties.SpringSecurityProperties;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

/**
 * OAuth2授权服务器自动配置类
 *
 * @author Hccake
 */
@AutoConfiguration(before = SpringSecurityAutoConfiguration.class)
@Import({ OAuth2AuthorizationServerConfigurerExtensionConfiguration.class,
		OAuth2AuthorizationServerConfigurerAdapterConfiguration.class })
@RequiredArgsConstructor
@EnableConfigurationProperties({ SpringSecurityProperties.class, OAuth2AuthorizationServerProperties.class })
public class OAuth2AuthorizationServerAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public BasicOAuth2AuthorizationServerConfigurerCustomizer basicOAuth2AuthorizationServerConfigurerCustomizer(
			List<OAuth2AuthorizationServerConfigurerExtension> oAuth2AuthorizationServerConfigurerExtensions,
			ObjectProvider<List<OAuth2AuthorizationServerConfigurerAdapter>> oAuth2AuthorizationServerConfigurersAdapterProvider) {
		return new BasicOAuth2AuthorizationServerConfigurerCustomizer(oAuth2AuthorizationServerConfigurerExtensions,
				oAuth2AuthorizationServerConfigurersAdapterProvider.getIfAvailable());
	}

	/**
	 * OAuth2 授权服务器中注册的 client 仓库
	 */
	@Bean
	@ConditionalOnMissingBean
	public RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate) {
		return new JdbcRegisteredClientRepository(jdbcTemplate);
	}

	/**
	 * OAuth2 授权管理Service
	 */
	@Bean
	@ConditionalOnMissingBean
	public OAuth2AuthorizationService authorizationService(JdbcTemplate jdbcTemplate,
			RegisteredClientRepository registeredClientRepository,
			ObjectProvider<OAuth2AuthorizationObjectMapperCustomizer> objectMapperCustomizerObjectProvider) {
		JdbcOAuth2AuthorizationService oAuth2AuthorizationService = new JdbcOAuth2AuthorizationService(jdbcTemplate,
				registeredClientRepository);

		// 需要注册自己的 mixin 来处理类型转换
		// link
		// https://github.com/spring-projects/spring-authorization-server/issues/397#issuecomment-900148920
		JdbcOAuth2AuthorizationService.OAuth2AuthorizationRowMapper rowMapper = new JdbcOAuth2AuthorizationService.OAuth2AuthorizationRowMapper(
				registeredClientRepository);

		ObjectMapper objectMapper = new ObjectMapper();
		ClassLoader classLoader = JdbcOAuth2AuthorizationService.class.getClassLoader();
		List<Module> securityModules = SecurityJackson2Modules.getModules(classLoader);
		objectMapper.registerModules(securityModules);
		objectMapper.registerModule(new OAuth2AuthorizationServerJackson2Module());

		// You will need to write the Mixin for your class so Jackson can marshall it.
		objectMapper.addMixIn(Long.class, LongMixin.class);
		objectMapper.addMixIn(DefaultOAuth2User.class, UserMixin.class);
		objectMapper.addMixIn(OAuth2UserAuthenticationToken.class, OAuth2UserAuthenticationTokenMixin.class);

		// 定制 objectMapper
		objectMapperCustomizerObjectProvider.ifAvailable(customizer -> customizer.customize(objectMapper));

		rowMapper.setObjectMapper(objectMapper);

		oAuth2AuthorizationService.setAuthorizationRowMapper(rowMapper);

		return oAuth2AuthorizationService;
	}

	/**
	 * OAuth2AuthorizationConsentService
	 */
	@Bean
	@ConditionalOnMissingBean
	public OAuth2AuthorizationConsentService authorizationConsentService(JdbcTemplate jdbcTemplate,
			RegisteredClientRepository registeredClientRepository) {
		return new JdbcOAuth2AuthorizationConsentService(jdbcTemplate, registeredClientRepository);
	}

	/**
	 * 授权服务器基本端点地址配置
	 * @return AuthorizationServerSettings
	 */
	@Bean
	@ConditionalOnMissingBean
	public AuthorizationServerSettings authorizationServerSettings() {
		return AuthorizationServerSettings.builder().build();
	}

	/**
	 * 对于使用不透明令牌的 client，需要存储对应的用户信息，以便在后续的请求中获取用户信息
	 */
	@Bean
	@ConditionalOnMissingBean(OAuth2TokenCustomizer.class)
	public OAuth2TokenCustomizer<OAuth2TokenClaimsContext> oAuth2TokenCustomizer() {
		return new BallcatOAuth2TokenCustomizer();
	}

}
