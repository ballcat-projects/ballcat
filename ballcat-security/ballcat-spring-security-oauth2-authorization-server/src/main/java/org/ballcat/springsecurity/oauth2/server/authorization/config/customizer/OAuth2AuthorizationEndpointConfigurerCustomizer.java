package org.ballcat.springsecurity.oauth2.server.authorization.config.customizer;

import org.ballcat.springsecurity.oauth2.server.authorization.config.configurer.OAuth2ConfigurerUtils;
import org.ballcat.springsecurity.oauth2.server.authorization.properties.OAuth2AuthorizationServerProperties;
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

	private final OAuth2AuthorizationServerProperties properties;

	private final OAuth2SecurityContextRepository oAuth2SecurityContextRepository;

	public OAuth2AuthorizationEndpointConfigurerCustomizer(OAuth2AuthorizationServerProperties properties,
			OAuth2SecurityContextRepository oAuth2SecurityContextRepository) {
		this.properties = properties;
		this.oAuth2SecurityContextRepository = oAuth2SecurityContextRepository;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void customize(OAuth2AuthorizationServerConfigurer oAuth2AuthorizationServerConfigurer,
			HttpSecurity httpSecurity) throws Exception {

		// 使用无状态登录时，需要配合自定义的 SecurityContextRepository
		if (properties.isStateless()) {
			httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
			httpSecurity
				.securityContext(security -> security.securityContextRepository(oAuth2SecurityContextRepository));
		}

		// 设置鉴权失败时的跳转地址
		String loginPage = properties.getLoginPage();
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
		String consentPage = properties.getConsentPage();
		if (consentPage != null) {
			oAuth2AuthorizationServerConfigurer
				.authorizationEndpoint(configurer -> configurer.consentPage(consentPage));
		}
	}

}
