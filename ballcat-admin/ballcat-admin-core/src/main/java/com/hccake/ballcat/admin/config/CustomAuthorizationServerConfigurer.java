package com.hccake.ballcat.admin.config;

import com.hccake.ballcat.common.redis.config.CachePropertiesHolder;
import com.hccake.ballcat.common.security.component.CustomRedisTokenStore;
import com.hccake.ballcat.common.security.constant.SecurityConstants;
import com.hccake.ballcat.oauth.CustomTokenEnhancer;
import com.hccake.ballcat.oauth.SysUserDetailsServiceImpl;
import com.hccake.ballcat.common.security.exception.CustomWebResponseExceptionTranslator;
import com.hccake.ballcat.oauth.mobile.MobileTokenGranter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;
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
@Configuration
@EnableAuthorizationServer
@RequiredArgsConstructor
public class CustomAuthorizationServerConfigurer implements AuthorizationServerConfigurer {

	private final DataSource dataSource;

	private final SysUserDetailsServiceImpl sysUserDetailsService;

	private final AuthenticationManager authenticationManager;

	private final RedisConnectionFactory redisConnectionFactory;

	private final AuthenticationEntryPoint authenticationEntryPoint;

	private final CustomWebResponseExceptionTranslator customWebResponseExceptionTranslator;

	/**
	 * 定义资源权限控制的配置
	 * @param security
	 * @throws Exception
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
	 * @param clients
	 * @throws Exception
	 */
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		// 启用 jdbc 方式获取客户端配置信息
		clients.jdbc(dataSource);
	}

	/**
	 * 授权服务的访问路径相关配置
	 * @param endpoints
	 * @throws Exception
	 */
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		// @formatter:off
		endpoints.tokenStore(tokenStore()).userDetailsService(sysUserDetailsService)
				.authenticationManager(authenticationManager)
				// 自定义token
				.tokenEnhancer(tokenEnhancer())
				// 强制刷新token时，重新生成refreshToken
				.reuseRefreshTokens(false)
				// 自定义的认证时异常转换
				.exceptionTranslator(customWebResponseExceptionTranslator)
				// 自定义tokenGranter
				.tokenGranter(tokenGranter(endpoints));
		// @formatter:on
	}

	/**
	 * 定义token的存储方式
	 * @return TokenStore token存储
	 */
	@Bean
	@DependsOn("cachePropertiesHolder")
	@ConditionalOnMissingBean
	public TokenStore tokenStore() {
		CustomRedisTokenStore tokenStore = new CustomRedisTokenStore(redisConnectionFactory);
		tokenStore.setPrefix(CachePropertiesHolder.keyPrefix() + SecurityConstants.OAUTH_PREFIX);
		return tokenStore;
	}

	/**
	 * token 增强，追加一些自定义信息
	 * @return TokenEnhancer Token增强处理器
	 */
	@Bean
	@ConditionalOnMissingBean
	public TokenEnhancer tokenEnhancer() {
		return new CustomTokenEnhancer();
	}

	private TokenGranter tokenGranter(final AuthorizationServerEndpointsConfigurer endpoints) {
		// 获取默认的granter集合
		List<TokenGranter> granters = new ArrayList<>(Collections.singletonList(endpoints.getTokenGranter()));
		granters.add(new MobileTokenGranter(authenticationManager, endpoints.getTokenServices(),
				endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory()));
		return new CompositeTokenGranter(granters);
	}

}
