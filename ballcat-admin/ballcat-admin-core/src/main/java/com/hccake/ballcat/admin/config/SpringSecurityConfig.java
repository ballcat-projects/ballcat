package com.hccake.ballcat.admin.config;

import com.hccake.ballcat.common.security.CustomSecurityConfiguration;
import com.hccake.ballcat.common.util.PasswordUtils;
import com.hccake.ballcat.oauth.CustomAuthenticationEntryPoint;
import com.hccake.ballcat.oauth.mobile.MobileAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/9/25 20:13
 */
@Import(CustomSecurityConfiguration.class)
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

	private final UserDetailsService userDetailsService;

	/**
	 * 密码解析器
	 * @return
	 */
	@Bean
	protected PasswordEncoder passwordEncoder() {
		return PasswordUtils.ENCODER;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		MobileAuthenticationProvider mobileAuthenticationProvider = new MobileAuthenticationProvider();
		mobileAuthenticationProvider.setUserDetailsService(userDetailsService);
		http.authenticationProvider(mobileAuthenticationProvider);
	}

	/**
	 * 解决无法注入 authenticationManagerBean 的问题
	 * @return
	 * @throws Exception
	 */
	@Override
	@Bean(name = BeanIds.AUTHENTICATION_MANAGER)
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public AuthenticationEntryPoint authenticationEntryPoint() {
		return new CustomAuthenticationEntryPoint();
	}

}
