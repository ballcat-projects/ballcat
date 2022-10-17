package com.hccake.ballcat.auth.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

/**
 * @author hccake
 */
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class AuthenticationManagerConfiguration {

	private final List<AuthenticationProvider> authenticationProviders;

	/**
	 * 授权管理器
	 */
	@Bean
	public AuthenticationManager authenticationManager(UserDetailsService userDetailsService,
			PasswordEncoder passwordEncoder, AuthenticationConfiguration authenticationConfiguration,
			ApplicationContext applicationContext) throws Exception {

		AuthenticationManagerBuilder authBuilder = applicationContext.getBean(AuthenticationManagerBuilder.class);

		// 添加多种授权模式
		for (AuthenticationProvider authenticationProvider : authenticationProviders) {
			authBuilder.authenticationProvider(authenticationProvider);
		}
		// 注册 DaoAuthenticationProvider
		if (userDetailsService != null && passwordEncoder != null) {
			authBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
		}
		return authenticationConfiguration.getAuthenticationManager();
	}

}
