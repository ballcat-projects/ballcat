package com.hccake.ballcat.common.security.oauth2.server.resource;

import com.hccake.ballcat.common.security.SecurityConfiguration;
import com.hccake.ballcat.common.security.component.CustomPermissionEvaluator;
import com.hccake.ballcat.common.security.properties.SecurityProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.server.resource.authentication.OpaqueTokenAuthenticationProvider;

/**
 * 资源服务需要的一些 bean 配置
 *
 * @author hccake
 */
@RequiredArgsConstructor
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableConfigurationProperties(SecurityProperties.class)
@Import({ SecurityConfiguration.class, ResourceServerWebSecurityConfigurerAdapter.class })
public class ResourceServerAutoConfiguration {

	private final TokenStore tokenStore;

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
	 * 配合 Spring-security-oauth2 的授权服务器，用于解析其生成的不透明令牌
	 * @return CustomOpaqueTokenIntrospector
	 */
	@Bean
	public SharedStoredOpaqueTokenIntrospector customOpaqueTokenIntrospector() {
		return new SharedStoredOpaqueTokenIntrospector(tokenStore);
	}

	/**
	 * spring-security 5.x 中开启资源服务器功能，需要的不透明令牌的支持
	 * @return OpaqueTokenAuthenticationProvider
	 */
	@Bean
	@ConditionalOnMissingBean
	public OpaqueTokenAuthenticationProvider opaqueTokenAuthenticationProvider() {
		return new OpaqueTokenAuthenticationProvider(customOpaqueTokenIntrospector());
	}

}
