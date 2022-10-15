package org.ballcat.springsecurity.oauth2.server.authorization.configurer;

import lombok.RequiredArgsConstructor;
import org.ballcat.springsecurity.oauth2.server.authorization.properties.OAuth2AuthorizationServerProperties;
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

	private final OAuth2AuthorizationServerProperties oAuth2AuthorizationServerProperties;

	private final UserDetailsService userDetailsService;

	private static final String DEFAULT_LOGIN_URL = "/login";

	@Override
	public void customize(OAuth2AuthorizationServerConfigurer oAuth2AuthorizationServerConfigurer,
			HttpSecurity httpSecurity) throws Exception {

		if (oAuth2AuthorizationServerProperties.isEnableFormLogin()) {
			String formLoginPage = oAuth2AuthorizationServerProperties.getFormLoginPage();

			HttpSecurity.RequestMatcherConfigurer requestMatcherConfigurer = httpSecurity.requestMatchers();
			if (formLoginPage == null) {
				requestMatcherConfigurer.antMatchers(DEFAULT_LOGIN_URL);
				httpSecurity.formLogin();
			}
			else {
				requestMatcherConfigurer.antMatchers(formLoginPage);
				httpSecurity.formLogin(form -> form.loginPage(formLoginPage).permitAll());
			}

			// 需要 userDetailsService 对应生成 DaoAuthenticationProvider
			httpSecurity.userDetailsService(userDetailsService);
		}

	}

}
