package com.hccake.ballcat.common.security;

import com.hccake.ballcat.common.redis.config.CachePropertiesHolder;
import com.hccake.ballcat.common.security.component.CustomRedisTokenStore;
import com.hccake.ballcat.common.security.constant.SecurityConstants;
import com.hccake.ballcat.common.security.exception.CustomAuthenticationEntryPoint;
import com.hccake.ballcat.common.security.util.PasswordUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 *
 */
@RequiredArgsConstructor
@Configuration
public class SecurityConfiguration {

	private final RedisConnectionFactory redisConnectionFactory;

	/**
	 * 密码解析器
	 * @return BCryptPasswordEncoder
	 */
	@Bean
	@ConditionalOnMissingBean
	protected PasswordEncoder passwordEncoder() {
		return PasswordUtils.ENCODER;
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
	 * 定义token的存储方式，默认使用 RedisTokenStore
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

}
