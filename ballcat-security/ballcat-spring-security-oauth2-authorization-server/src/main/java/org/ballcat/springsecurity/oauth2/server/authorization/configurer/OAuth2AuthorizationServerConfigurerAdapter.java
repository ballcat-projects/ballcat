package org.ballcat.springsecurity.oauth2.server.authorization.configurer;

import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.List;

/**
 * OAuth2 的授权服务配置
 * <p>
 * 当实例既是授权服务器又是资源服务器时，Order 必须高于资源服务器
 *
 * @author hccake
 */
@RequiredArgsConstructor
@Order(99)
public class OAuth2AuthorizationServerConfigurerAdapter extends WebSecurityConfigurerAdapter {

	private final List<OAuth2AuthorizationServerConfigurerCustomizer> oAuth2AuthorizationServerConfigurerCustomizerList;

	private final List<OAuth2AuthorizationServerExtensionConfigurer> oAuth2AuthorizationServerExtensionConfigurers;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// 授权服务器配置
		OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer();
		for (OAuth2AuthorizationServerConfigurerCustomizer customizer : oAuth2AuthorizationServerConfigurerCustomizerList) {
			customizer.customize(authorizationServerConfigurer, http);
		}

		// @formatter:off
		RequestMatcher endpointsMatcher = authorizationServerConfigurer.getEndpointsMatcher();
		http.requestMatchers()
				.requestMatchers(endpointsMatcher)
				.and()
				.authorizeRequests(authorizeRequests -> authorizeRequests.anyRequest().authenticated())
				.csrf(csrf -> csrf.ignoringRequestMatchers(endpointsMatcher))
				.apply(authorizationServerConfigurer);
		// @formatter:off

		for (OAuth2AuthorizationServerExtensionConfigurer configurer : oAuth2AuthorizationServerExtensionConfigurers) {
			http.apply(configurer);
		}
	}
}
