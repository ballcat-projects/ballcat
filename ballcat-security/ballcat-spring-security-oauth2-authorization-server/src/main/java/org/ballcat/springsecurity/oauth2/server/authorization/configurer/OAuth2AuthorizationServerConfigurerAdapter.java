package org.ballcat.springsecurity.oauth2.server.authorization.configurer;

import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.RequestMatcher;

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

	private final OAuth2AuthorizationServerConfigurerWrapper oAuth2AuthorizationServerConfigurerWrapper;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// @formatter:off
		RequestMatcher endpointsMatcher = oAuth2AuthorizationServerConfigurerWrapper.getEndpointsMatcher();
		http.requestMatcher(endpointsMatcher)
				.authorizeRequests(authorizeRequests -> authorizeRequests.anyRequest().authenticated())
				.csrf(csrf -> csrf.ignoringRequestMatchers(endpointsMatcher))
				.apply(oAuth2AuthorizationServerConfigurerWrapper);

		// @formatter:off
	}
}
