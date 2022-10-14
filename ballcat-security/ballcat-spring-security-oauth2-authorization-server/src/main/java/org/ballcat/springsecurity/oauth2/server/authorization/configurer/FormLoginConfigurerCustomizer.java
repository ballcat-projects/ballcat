package org.ballcat.springsecurity.oauth2.server.authorization.configurer;

import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;

/**
 * 表单登录配置项
 *
 * @author hccake
 */
@RequiredArgsConstructor
public class FormLoginConfigurerCustomizer implements OAuth2AuthorizationServerConfigurerCustomizer {

	private final UserDetailsService userDetailsService;

	@Override
	public void customize(OAuth2AuthorizationServerConfigurer oAuth2AuthorizationServerConfigurer,
			HttpSecurity httpSecurity) throws Exception {
		// 开启表单登录
		httpSecurity.requestMatchers().antMatchers("/login").and().formLogin().and()
				.userDetailsService(userDetailsService);
	}

}
