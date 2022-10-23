package org.ballcat.springsecurity.oauth2.server.authorization.configurer;

import org.ballcat.springsecurity.oauth2.server.authorization.web.authentication.OAuth2LoginUrlAuthenticationEntryPoint;
import org.ballcat.springsecurity.oauth2.server.authorization.web.context.OAuth2SecurityContextRepository;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * OAuth2 授权码流程端点的扩展处理器
 *
 * @author hccake
 */
public class OAuth2AuthorizationEndpointConfigurerCustomizer implements OAuth2AuthorizationServerConfigurerCustomizer {

	private final String loginPage;

	private final String consentPage;

	private final OAuth2SecurityContextRepository oAuth2SecurityContextRepository;

	public OAuth2AuthorizationEndpointConfigurerCustomizer(String loginPage, String consentPage,
			OAuth2SecurityContextRepository oAuth2SecurityContextRepository) {
		this.loginPage = loginPage;
		this.consentPage = consentPage;
		this.oAuth2SecurityContextRepository = oAuth2SecurityContextRepository;
	}

	@Override
	public void customize(OAuth2AuthorizationServerConfigurer oAuth2AuthorizationServerConfigurer,
			HttpSecurity httpSecurity) throws Exception {

		// 不开启表单登录的情况下，应该使用无状态登录
		httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		httpSecurity.securityContext(security -> security.securityContextRepository(oAuth2SecurityContextRepository));

		// 设置鉴权失败时的跳转地址
		AuthorizationServerSettings authorizationServerSettings = OAuth2ConfigurerUtils
				.getAuthorizationServerSettings(httpSecurity);
		ExceptionHandlingConfigurer<?> exceptionHandling = httpSecurity
				.getConfigurer(ExceptionHandlingConfigurer.class);
		if (exceptionHandling != null) {
			exceptionHandling.defaultAuthenticationEntryPointFor(new OAuth2LoginUrlAuthenticationEntryPoint(loginPage),
					new AntPathRequestMatcher(authorizationServerSettings.getAuthorizationEndpoint(),
							HttpMethod.GET.name()));
		}

		// 设置 OAuth2 Consent 地址
		oAuth2AuthorizationServerConfigurer.authorizationEndpoint(configurer -> configurer.consentPage(consentPage));
	}

}
