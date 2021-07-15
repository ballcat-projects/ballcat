package com.hccake.ballcat.auth.configuration;

import com.hccake.ballcat.auth.CustomTokenEnhancer;
import com.hccake.ballcat.auth.OAuth2AuthorizationServerProperties;
import com.hccake.ballcat.auth.UserInfoCoordinator;
import com.hccake.ballcat.auth.confogurer.CustomAuthorizationServerConfigurer;
import com.hccake.ballcat.common.redis.config.CachePropertiesHolder;
import com.hccake.ballcat.common.security.component.CustomRedisTokenStore;
import com.hccake.ballcat.common.security.constant.SecurityConstants;
import com.hccake.ballcat.common.security.exception.CustomAuthenticationEntryPoint;
import com.hccake.ballcat.common.security.exception.CustomWebResponseExceptionTranslator;
import com.hccake.ballcat.common.security.properties.SecurityProperties;
import com.hccake.ballcat.common.security.util.PasswordUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * 授权服务器需要的一些 Bean 信息注册
 *
 * @author hccake
 */
@Import({ CustomAuthorizationServerConfigurer.class, AuthorizationFilterConfiguration.class })
@EnableConfigurationProperties({ SecurityProperties.class, OAuth2AuthorizationServerProperties.class })
public class AuthorizationAutoConfiguration {

	/**
	 * token 增强，追加一些自定义信息
	 * @return TokenEnhancer Token增强处理器
	 */
	@Bean
	@ConditionalOnMissingBean
	public TokenEnhancer tokenEnhancer() {
		return new CustomTokenEnhancer();
	}

	/**
	 * 自定义的认证时异常转换
	 * @return WebResponseExceptionTranslator
	 */
	@Bean
	@ConditionalOnMissingBean
	public WebResponseExceptionTranslator<OAuth2Exception> customWebResponseExceptionTranslator() {
		return new CustomWebResponseExceptionTranslator();
	}

	/**
	 * 密码解析器，只在授权服务器中进行配置
	 * @return BCryptPasswordEncoder
	 */
	@Bean
	@ConditionalOnMissingBean
	protected PasswordEncoder passwordEncoder() {
		return PasswordUtils.ENCODER;
	}

	/**
	 * 定义token的存储方式，默认使用 RedisTokenStore，只在授权服务器中进行配置
	 * @return TokenStore token存储
	 */
	@Bean
	@DependsOn("cachePropertiesHolder")
	@ConditionalOnMissingBean
	public TokenStore tokenStore(RedisConnectionFactory redisConnectionFactory) {
		CustomRedisTokenStore tokenStore = new CustomRedisTokenStore(redisConnectionFactory);
		tokenStore.setPrefix(CachePropertiesHolder.keyPrefix() + SecurityConstants.OAUTH_PREFIX);
		return tokenStore;
	}

	/**
	 * 自定义异常处理
	 * @return AuthenticationEntryPoint
	 */
	@Bean
	@ConditionalOnMissingBean
	public AuthenticationEntryPoint authenticationEntryPoint() {
		return new CustomAuthenticationEntryPoint();
	}

	/**
	 * 用户信息协调者
	 * @return UserInfoCoordinator
	 */
	@Bean
	@ConditionalOnMissingBean
	public UserInfoCoordinator userInfoCoordinator() {
		return new UserInfoCoordinator();
	}

}
