package com.hccake.ballcat.auth.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeTokenGranter;
import org.springframework.security.oauth2.provider.implicit.ImplicitTokenGranter;
import org.springframework.security.oauth2.provider.password.ResourceOwnerPasswordTokenGranter;
import org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.util.ArrayList;
import java.util.List;

/**
 * TokenGrant 构造器，用户自定义此类，可以扩展 grant_type 支持，比如 grant_type=mobile，用以支持手机号登录
 *
 * @author hccake
 */
@RequiredArgsConstructor
public class TokenGrantBuilder {

	private final AuthenticationManager authenticationManager;

	public TokenGranter build(final AuthorizationServerEndpointsConfigurer endpoints) {
		List<TokenGranter> tokenGranters = defaultTokenGranters(endpoints);
		return new CompositeTokenGranter(tokenGranters);
	}

	/**
	 * OAuth2 规范的 5 种授权类型
	 * @param endpoints AuthorizationServerEndpointsConfigurer
	 * @return List<TokenGranter>
	 */
	private List<TokenGranter> defaultTokenGranters(AuthorizationServerEndpointsConfigurer endpoints) {
		ClientDetailsService clientDetailsService = endpoints.getClientDetailsService();
		AuthorizationServerTokenServices tokenServices = endpoints.getTokenServices();
		AuthorizationCodeServices authorizationCodeServices = endpoints.getAuthorizationCodeServices();
		OAuth2RequestFactory requestFactory = endpoints.getOAuth2RequestFactory();

		List<TokenGranter> tokenGranters = new ArrayList<>();
		// 授权码模式
		tokenGranters.add(new AuthorizationCodeTokenGranter(tokenServices, authorizationCodeServices,
				clientDetailsService, requestFactory));
		// 刷新令牌
		tokenGranters.add(new RefreshTokenGranter(tokenServices, clientDetailsService, requestFactory));
		// 隐式授权
		ImplicitTokenGranter implicit = new ImplicitTokenGranter(tokenServices, clientDetailsService, requestFactory);
		tokenGranters.add(implicit);
		// 客户端凭证，这里自定了修改了默认的客户端凭证处理
		tokenGranters.add(new CustomClientCredentialsTokenGranter(tokenServices, clientDetailsService, requestFactory));
		// 用户名密码模式
		if (authenticationManager != null) {
			tokenGranters.add(new ResourceOwnerPasswordTokenGranter(authenticationManager, tokenServices,
					clientDetailsService, requestFactory));
		}
		return tokenGranters;
	}

}
