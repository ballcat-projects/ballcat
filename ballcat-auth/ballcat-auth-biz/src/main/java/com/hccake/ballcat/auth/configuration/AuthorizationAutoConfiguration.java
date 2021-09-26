package com.hccake.ballcat.auth.configuration;

import com.hccake.ballcat.auth.CheckEndpointPostProcessor;
import com.hccake.ballcat.auth.CustomAccessTokenConverter;
import com.hccake.ballcat.auth.OAuth2AuthorizationServerProperties;
import com.hccake.ballcat.auth.authentication.TokenGrantBuilder;
import com.hccake.ballcat.auth.configurer.CustomAuthorizationServerConfigurer;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
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
	 * check_token 端点返回信息的处理类
	 * @return AccessTokenConverter
	 */
	@Bean
	@ConditionalOnMissingBean
	public AccessTokenConverter accessTokenConverter() {
		return new CustomAccessTokenConverter();
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
	 * 代理 CheckEndpoint，以便符合 OAuth2 规范
	 * @return CheckEndpointPostProcessor
	 */
	@Bean
	@ConditionalOnMissingBean
	public CheckEndpointPostProcessor checkEndpointPostProcessor() {
		return new CheckEndpointPostProcessor();
	}

	/**
	 * 授权类型建造者，默认处理了 OAuth2 规范的 5 种授权类型，用户可自定义添加其他授权类型，如手机号登录
	 * @param authenticationManager 认证管理器
	 * @return TokenGrantBuilder
	 */
	@Bean
	@ConditionalOnMissingBean
	public TokenGrantBuilder tokenGrantBuilder(AuthenticationManager authenticationManager) {
		return new TokenGrantBuilder(authenticationManager);
	}

}
