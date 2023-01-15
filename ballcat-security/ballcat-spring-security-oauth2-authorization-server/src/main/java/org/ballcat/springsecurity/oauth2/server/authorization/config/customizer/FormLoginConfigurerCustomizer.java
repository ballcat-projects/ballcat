package org.ballcat.springsecurity.oauth2.server.authorization.config.customizer;

import lombok.RequiredArgsConstructor;
import org.ballcat.springsecurity.oauth2.server.authorization.properties.OAuth2AuthorizationServerProperties;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;

import static org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter.DEFAULT_LOGIN_PAGE_URL;

/**
 * 表单登录配置项
 *
 * @author hccake
 */
@RequiredArgsConstructor
public class FormLoginConfigurerCustomizer implements OAuth2AuthorizationServerConfigurerCustomizer {

	private final OAuth2AuthorizationServerProperties oAuth2AuthorizationServerProperties;

	private final UserDetailsService userDetailsService;

	@Override
	public void customize(OAuth2AuthorizationServerConfigurer oAuth2AuthorizationServerConfigurer,
			HttpSecurity httpSecurity) throws Exception {

		if (oAuth2AuthorizationServerProperties.isLoginPageEnabled()) {
			String loginPage = oAuth2AuthorizationServerProperties.getLoginPage();

			HttpSecurity.RequestMatcherConfigurer requestMatcherConfigurer = httpSecurity.requestMatchers();
			if (loginPage == null) {
				requestMatcherConfigurer.antMatchers(DEFAULT_LOGIN_PAGE_URL);
				httpSecurity.formLogin();
			}
			else {
				requestMatcherConfigurer.antMatchers(loginPage);
				httpSecurity.formLogin(form -> form.loginPage(loginPage).permitAll());
			}

			// 需要 userDetailsService 对应生成 DaoAuthenticationProvider
			httpSecurity.userDetailsService(userDetailsService);
		}

	}

}
