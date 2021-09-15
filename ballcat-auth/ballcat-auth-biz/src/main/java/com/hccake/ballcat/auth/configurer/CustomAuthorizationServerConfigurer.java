package com.hccake.ballcat.auth.configurer;

import com.hccake.ballcat.auth.authentication.TokenGrantBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.sql.DataSource;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/9/27 16:14 OAuth2 授权服务器配置
 */
@RequiredArgsConstructor
public class CustomAuthorizationServerConfigurer implements AuthorizationServerConfigurer {

	private final AuthenticationManager authenticationManager;

	private final DataSource dataSource;

	private final TokenStore tokenStore;

	private final UserDetailsService userDetailsService;

	private final TokenEnhancer tokenEnhancer;

	private final AccessTokenConverter accessTokenConverter;

	private final WebResponseExceptionTranslator<OAuth2Exception> webResponseExceptionTranslator;

	private final AuthenticationEntryPoint authenticationEntryPoint;

	private final TokenGrantBuilder tokenGrantBuilder;

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
		endpoints.tokenStore(tokenStore).userDetailsService(userDetailsService)
				.authenticationManager(authenticationManager)
				// 自定义token
				.tokenEnhancer(tokenEnhancer)
				// 强制刷新token时，重新生成refreshToken
				.reuseRefreshTokens(false)
				// 自定义的认证时异常转换
				.exceptionTranslator(webResponseExceptionTranslator)
				// 自定义tokenGranter
				.tokenGranter(tokenGrantBuilder.build(endpoints))
				// 使用自定义的 TokenConverter，方便在 checkToken 时，返回更多的信息
				.accessTokenConverter(accessTokenConverter);
		// @formatter:on
	}

	/**
	 * authorize_code 模式支持
	 */
	@Order(1)
	@Configuration(proxyBeanMethods = false)
	private class AuthorizeServerConfigurerAdapter extends WebSecurityConfigurerAdapter {

		private static final String AUTHORIZE_ENDPOINT_PATH = "/oauth/authorize";

		@Override
		public void configure(HttpSecurity http) throws Exception {
			// @formatter:off
				http
					.formLogin()
					.and()
						.requestMatchers()
						.antMatchers(AUTHORIZE_ENDPOINT_PATH)
					.and()
						.authorizeRequests().antMatchers(AUTHORIZE_ENDPOINT_PATH).authenticated();
				// @formatter:on
		}

		@Override
		public void configure(AuthenticationManagerBuilder builder) {
			builder.parentAuthenticationManager(authenticationManager);
		}

	}

}
