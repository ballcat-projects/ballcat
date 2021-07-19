package com.hccake.ballcat.common.security.oauth2.server.resource;

import com.hccake.ballcat.common.security.component.CustomPermissionEvaluator;
import com.hccake.ballcat.common.security.exception.CustomAuthenticationEntryPoint;
import com.hccake.ballcat.common.security.properties.OAuth2ResourceServerProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.server.resource.authentication.OpaqueTokenAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * 资源服务需要的一些 bean 配置
 *
 * @author hccake
 */
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableConfigurationProperties(OAuth2ResourceServerProperties.class)
@Import(ResourceServerWebSecurityConfigurerAdapter.class)
public class ResourceServerAutoConfiguration {

	/**
	 * 自定义的权限判断组件
	 * @return CustomPermissionEvaluator
	 */
	@Bean(name = "per")
	@ConditionalOnMissingBean(CustomPermissionEvaluator.class)
	public CustomPermissionEvaluator customPermissionEvaluator() {
		return new CustomPermissionEvaluator();
	}

	/**
	 * 当资源服务器和授权服务器的 token 共享存储时，配合 Spring-security-oauth2 的 TokenStore，用于解析其生成的不透明令牌
	 * @see org.springframework.security.oauth2.provider.token.TokenStore
	 * @return SharedStoredOpaqueTokenIntrospector
	 */
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(prefix = "ballcat.security.oauth2.resourceserver", name = "shared-stored-token",
			havingValue = "true", matchIfMissing = true)
	public OpaqueTokenIntrospector sharedStoredOpaqueTokenIntrospector(TokenStore tokenStore) {
		return new SharedStoredOpaqueTokenIntrospector(tokenStore);
	}

	/**
	 * 当资源服务器和授权服务器的 token 存储无法共享时，通过远程调用的方式，向授权服务鉴定 token，并同时获取对应的授权信息
	 * @return NimbusOpaqueTokenIntrospector
	 */
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(prefix = "ballcat.security.oauth2.resourceserver", name = "shared-stored-token",
			havingValue = "false")
	public OpaqueTokenIntrospector opaqueTokenIntrospector(
			OAuth2ResourceServerProperties oAuth2ResourceServerProperties) {
		OAuth2ResourceServerProperties.Opaquetoken opaqueToken = oAuth2ResourceServerProperties.getOpaqueToken();
		return new RemoteOpaqueTokenIntrospector(opaqueToken.getIntrospectionUri(), opaqueToken.getClientId(),
				opaqueToken.getClientSecret());
	}

	/**
	 * spring-security 5.x 中开启资源服务器功能，需要的不透明令牌的支持
	 * @return OpaqueTokenAuthenticationProvider
	 */
	@Bean
	@ConditionalOnMissingBean
	public OpaqueTokenAuthenticationProvider opaqueTokenAuthenticationProvider(
			OpaqueTokenIntrospector opaqueTokenIntrospector) {
		return new OpaqueTokenAuthenticationProvider(opaqueTokenIntrospector);
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
	 * BearTokenResolve 允许使用 url 传参，方便 ws 连接 ps: 使用 url 传参不安全，待改进
	 * @return DefaultBearerTokenResolver
	 */
	@Bean
	@ConditionalOnMissingBean
	public BearerTokenResolver bearerTokenResolver() {
		DefaultBearerTokenResolver defaultBearerTokenResolver = new DefaultBearerTokenResolver();
		defaultBearerTokenResolver.setAllowUriQueryParameter(true);
		return defaultBearerTokenResolver;
	}

}
