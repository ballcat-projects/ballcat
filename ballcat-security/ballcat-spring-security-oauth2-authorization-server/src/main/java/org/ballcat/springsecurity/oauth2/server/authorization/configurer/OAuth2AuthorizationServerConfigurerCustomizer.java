package org.ballcat.springsecurity.oauth2.server.authorization.configurer;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;

/**
 * 授权服务器配置的自定义器
 *
 * @author hccake
 */
@FunctionalInterface
public interface OAuth2AuthorizationServerConfigurerCustomizer {

	/**
	 * 对授权服务器配置进行自定义
	 * @param oAuth2AuthorizationServerConfigurer OAuth2AuthorizationServerConfigurer
	 * @param httpSecurity security configuration
	 */
	void customize(OAuth2AuthorizationServerConfigurer oAuth2AuthorizationServerConfigurer, HttpSecurity httpSecurity)
			throws Exception;

}
