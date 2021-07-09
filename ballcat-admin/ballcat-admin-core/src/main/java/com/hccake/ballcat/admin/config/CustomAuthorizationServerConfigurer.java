package com.hccake.ballcat.admin.config;

import com.hccake.ballcat.oauth.CustomAccessTokenConverter;
import com.hccake.ballcat.oauth.SysUserDetailsServiceImpl;
import com.hccake.ballcat.oauth.mobile.MobileTokenGranter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/9/27 16:14 OAuth2 授权服务器配置
 */
@Configuration(proxyBeanMethods = false)
@EnableAuthorizationServer
@RequiredArgsConstructor
public class CustomAuthorizationServerConfigurer implements AuthorizationServerConfigurer {

	private final AuthenticationManager authenticationManager;

	private final DataSource dataSource;

	private final TokenStore tokenStore;

	private final SysUserDetailsServiceImpl sysUserDetailsService;

	private final TokenEnhancer tokenEnhancer;

	private final WebResponseExceptionTranslator webResponseExceptionTranslator;

	private final AuthenticationEntryPoint authenticationEntryPoint;

	/**
	 * 定义资源权限控制的配置
	 * @param security AuthorizationServerSecurityConfigurer
	 * @throws Exception 异常
	 */
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		// @formatter:off
		security.tokenKeyAccess("permitAll()")
			.checkTokenAccess("isAuthenticated()")
			.authenticationEntryPoint(authenticationEntryPoint)
			.allowFormAuthenticationForClients();
		// @formatter:on
	}

	/**
	 * 客户端的信息服务类配置
	 * @param clients ClientDetailsServiceConfigurer
	 * @throws Exception 异常
	 */
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		// 启用 jdbc 方式获取客户端配置信息
		clients.jdbc(dataSource);
	}

	/**
	 * 授权服务的访问路径相关配置
	 * @param endpoints AuthorizationServerEndpointsConfigurer
	 * @throws Exception 异常
	 */
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		// @formatter:off
		endpoints.tokenStore(tokenStore).userDetailsService(sysUserDetailsService)
				.authenticationManager(authenticationManager)
				// 自定义token
				.tokenEnhancer(tokenEnhancer)
				// 强制刷新token时，重新生成refreshToken
				.reuseRefreshTokens(false)
				// 自定义的认证时异常转换
				.exceptionTranslator(webResponseExceptionTranslator)
				// 自定义tokenGranter
				.tokenGranter(tokenGranter(endpoints));
		// @formatter:on
	}

	private TokenGranter tokenGranter(final AuthorizationServerEndpointsConfigurer endpoints) {
		// 使用自定义的 TokenConverter，方便在 checkToken 时，返回更多的信息
		endpoints.accessTokenConverter(new CustomAccessTokenConverter());
		// 获取默认的granter集合
		List<TokenGranter> granters = new ArrayList<>(Collections.singletonList(endpoints.getTokenGranter()));
		granters.add(new MobileTokenGranter(authenticationManager, endpoints.getTokenServices(),
				endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory()));
		return new CompositeTokenGranter(granters);
	}

}
