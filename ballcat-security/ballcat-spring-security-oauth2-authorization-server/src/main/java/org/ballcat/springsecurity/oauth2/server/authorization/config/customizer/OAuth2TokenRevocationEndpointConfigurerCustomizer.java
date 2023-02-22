package org.ballcat.springsecurity.oauth2.server.authorization.config.customizer;

import lombok.RequiredArgsConstructor;
import org.ballcat.springsecurity.oauth2.server.authorization.authentication.OAuth2TokenRevocationAuthenticationProvider;
import org.ballcat.springsecurity.oauth2.server.authorization.web.authentication.OAuth2TokenRevocationResponseHandler;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;

/**
 * 令牌撤销端点配置的自定义扩展
 *
 * @author hccake
 */
@RequiredArgsConstructor
public class OAuth2TokenRevocationEndpointConfigurerCustomizer
		implements OAuth2AuthorizationServerConfigurerCustomizer {

	private final OAuth2AuthorizationService authorizationService;

	private final OAuth2TokenRevocationResponseHandler oAuth2TokenRevocationResponseHandler;

	@Override
	public void customize(OAuth2AuthorizationServerConfigurer oAuth2AuthorizationServerConfigurer,
			HttpSecurity httpSecurity) {
		oAuth2AuthorizationServerConfigurer.tokenRevocationEndpoint(
				tokenRevocation -> tokenRevocation.revocationResponseHandler(oAuth2TokenRevocationResponseHandler)
					.authenticationProvider(new OAuth2TokenRevocationAuthenticationProvider(authorizationService)));
	}

}
