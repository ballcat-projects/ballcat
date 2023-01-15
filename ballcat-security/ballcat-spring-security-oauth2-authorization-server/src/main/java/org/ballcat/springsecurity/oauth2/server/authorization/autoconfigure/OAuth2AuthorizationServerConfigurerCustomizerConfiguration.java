package org.ballcat.springsecurity.oauth2.server.authorization.autoconfigure;

import lombok.RequiredArgsConstructor;
import org.ballcat.springsecurity.oauth2.server.authorization.config.customizer.FormLoginConfigurerCustomizer;
import org.ballcat.springsecurity.oauth2.server.authorization.config.customizer.OAuth2ResourceOwnerPasswordConfigurerCustomizer;
import org.ballcat.springsecurity.oauth2.server.authorization.config.customizer.OAuth2TokenResponseEnhanceConfigurerCustomizer;
import org.ballcat.springsecurity.oauth2.server.authorization.config.customizer.OAuth2TokenRevocationEndpointConfigurerCustomizer;
import org.ballcat.springsecurity.oauth2.server.authorization.properties.OAuth2AuthorizationServerProperties;
import org.ballcat.springsecurity.oauth2.server.authorization.web.authentication.OAuth2TokenResponseEnhancer;
import org.ballcat.springsecurity.oauth2.server.authorization.web.authentication.OAuth2TokenRevocationResponseHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;

/**
 * OAuth2 授权服务器配置定制器的配置类
 *
 * @author Hccake
 */
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class OAuth2AuthorizationServerConfigurerCustomizerConfiguration {

	private final OAuth2AuthorizationServerProperties oAuth2AuthorizationServerProperties;

	private final OAuth2AuthorizationService oAuth2AuthorizationService;

	/**
	 * 表单登陆支持
	 * @return FormLoginConfigurerCustomizer
	 */
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(prefix = OAuth2AuthorizationServerProperties.PREFIX, name = "form-login-enabled",
			havingValue = "true")
	public FormLoginConfigurerCustomizer formLoginConfigurerCustomizer(UserDetailsService userDetailsService) {
		return new FormLoginConfigurerCustomizer(oAuth2AuthorizationServerProperties, userDetailsService);
	}

	/**
	 * 添加 resource owner password 模式支持配置定制器
	 * @param authenticationManager 授权管理器
	 * @return OAuth2ResourceOwnerPasswordConfigurerCustomizer
	 */
	@Bean
	public OAuth2ResourceOwnerPasswordConfigurerCustomizer oAuth2ResourceOwnerPasswordConfigurerCustomizer(
			AuthenticationManager authenticationManager) {
		return new OAuth2ResourceOwnerPasswordConfigurerCustomizer(authenticationManager, oAuth2AuthorizationService);
	}

	/**
	 * token endpoint 响应增强配置定制器
	 * @param oauth2TokenResponseEnhancer OAuth2TokenResponseEnhancer
	 * @return OAuth2TokenResponseEnhanceConfigurerCustomizer
	 */
	@Bean
	@ConditionalOnBean(OAuth2TokenResponseEnhancer.class)
	public OAuth2TokenResponseEnhanceConfigurerCustomizer oAuth2TokenResponseEnhanceConfigurerCustomizer(
			OAuth2TokenResponseEnhancer oauth2TokenResponseEnhancer) {
		return new OAuth2TokenResponseEnhanceConfigurerCustomizer(oauth2TokenResponseEnhancer);
	}

	/**
	 * token 撤销响应处理器配置定制器
	 * @param oAuth2TokenRevocationResponseHandler token 撤销响应处理器
	 * @return OAuth2TokenRevocationResponseHandler
	 */
	@Bean
	@ConditionalOnBean(OAuth2TokenRevocationResponseHandler.class)
	public OAuth2TokenRevocationEndpointConfigurerCustomizer oAuth2TokenRevocationEndpointConfigurerCustomizer(
			OAuth2TokenRevocationResponseHandler oAuth2TokenRevocationResponseHandler) {
		return new OAuth2TokenRevocationEndpointConfigurerCustomizer(oAuth2AuthorizationService,
				oAuth2TokenRevocationResponseHandler);
	}

}
