package com.hccake.ballcat.common.security.oauth2.server.resource;

import cn.hutool.core.util.ArrayUtil;
import com.hccake.ballcat.common.security.properties.OAuth2ResourceServerProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.OpaqueTokenAuthenticationProvider;
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

	private final OpaqueTokenAuthenticationProvider opaqueTokenAuthenticationProvider;

	private final AuthenticationEntryPoint authenticationEntryPoint;

	@Autowired(required = false)
	private UserDetailsService userDetailsService;

	@Autowired(required = false)
	private PasswordEncoder passwordEncoder;

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/login.html", "/css/**", "/js/**", "/images/**", "/venrdor/**", "/fonts/**",
				"/favicon.ico", "/error");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// @formatter:off
        http
			// 表单登录
			.formLogin()
			.and().rememberMe()

			// 拦截 url 配置
			.and()
				.authorizeRequests()
				.antMatchers(ArrayUtil.toArray(oAuth2ResourceServerProperties.getIgnoreUrls(), String.class))
				.permitAll()
				.anyRequest().authenticated()

			// 关闭 csrf 跨站攻击防护
			.and().csrf().disable()

			// 添加不透明令牌的 provider
			.authenticationProvider(opaqueTokenAuthenticationProvider)

			// 开启 OAuth2 资源服务
			.oauth2ResourceServer().authenticationEntryPoint(authenticationEntryPoint)
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

	/**
	 * 当和授权服务器一起时，需要注册 userDetailService，以便支持 UsernamePasswordAuthenticationToken
	 * @param auth AuthenticationManagerBuilder
	 * @throws Exception 异常
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		if (userDetailsService != null && passwordEncoder != null) {
			auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
		}
	}

	@Bean(BeanIds.AUTHENTICATION_MANAGER)
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

}