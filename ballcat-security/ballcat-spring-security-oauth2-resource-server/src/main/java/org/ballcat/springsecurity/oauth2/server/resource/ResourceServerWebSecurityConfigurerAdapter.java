package org.ballcat.springsecurity.oauth2.server.resource;

import cn.hutool.core.util.ArrayUtil;
import lombok.RequiredArgsConstructor;
import org.ballcat.springsecurity.oauth2.server.resource.properties.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * 资源服务器的配置
 *
 * @author hccake
 */
@Configuration
@RequiredArgsConstructor
public class ResourceServerWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

	private final OAuth2ResourceServerProperties oAuth2ResourceServerProperties;

	private final AuthenticationEntryPoint authenticationEntryPoint;

	private final BearerTokenResolver bearerTokenResolver;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// @formatter:off
        http
			// 记住我
			.rememberMe()

			// 拦截 url 配置
			.and()
				.authorizeRequests()
				.antMatchers(ArrayUtil.toArray(oAuth2ResourceServerProperties.getIgnoreUrls(), String.class))
				.permitAll()
				.anyRequest().authenticated()

			// 关闭 csrf 跨站攻击防护
			.and().csrf().disable()

			// 开启 OAuth2 资源服务
			.oauth2ResourceServer().authenticationEntryPoint(authenticationEntryPoint)
			// bearToken 解析器
			.bearerTokenResolver(bearerTokenResolver)
			// 不透明令牌，
			.opaqueToken()
			// 鉴权管理器
			.authenticationManager(authenticationManagerBean());
		// @formatter:on

		// 允许嵌入iframe
		if (!oAuth2ResourceServerProperties.isIframeDeny()) {
			http.headers().frameOptions().disable();
		}
	}

}