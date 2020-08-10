package com.hccake.ballcat.admin.oauth.config;

import cn.hutool.core.util.ArrayUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/9/27 17:28 资源服务器配置
 */
@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class CustomResourceServerConfigurer extends ResourceServerConfigurerAdapter {

	private final AuthenticationEntryPoint authenticationEntryPoint;

	private final PermitAllUrlProperties permitAllUrlProperties;

	/**
	 * Add resource-server specific properties (like a resource id). The defaults should
	 * work for many applications, but you might want to change at least the resource id.
	 * @param resources configurer for the resource server
	 * @throws Exception if there is a problem
	 */
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		// 配置自定义异常处理
		resources.authenticationEntryPoint(authenticationEntryPoint);
	}

	/**
	 * 通过重载，配置如何通过拦截器保护请求
	 * @param httpSecurity HttpSecurity
	 * @throws Exception 异常信息
	 */
	@Override
	public void configure(HttpSecurity httpSecurity) throws Exception {
		// @formatter:off
		httpSecurity
				// 拦截 url 配置
				.authorizeRequests()
				.antMatchers(ArrayUtil.toArray(permitAllUrlProperties.getIgnoreUrls(), String.class))
				.permitAll()
				.anyRequest().authenticated()

				// 使用token鉴权时 关闭 session 缓存
				.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

				// 关闭 csrf 跨站攻击防护
				.and().csrf().disable();
		// @formatter:on

		// 允许嵌入iframe
		if (!permitAllUrlProperties.isIframeDeny()) {
			httpSecurity.headers().frameOptions().disable();
		}
	}

}
