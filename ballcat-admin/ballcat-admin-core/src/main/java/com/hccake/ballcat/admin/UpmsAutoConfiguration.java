package com.hccake.ballcat.admin;

import com.hccake.ballcat.auth.annotation.EnableOauth2AuthorizationServer;
import com.hccake.ballcat.common.security.annotation.EnableOauth2ResourceServer;
import com.hccake.ballcat.common.security.properties.SecurityProperties;
import com.hccake.ballcat.system.authentication.CustomTokenEnhancer;
import com.hccake.ballcat.system.authentication.SysUserDetailsServiceImpl;
import com.hccake.ballcat.system.authentication.UserInfoCoordinator;
import com.hccake.ballcat.system.properties.UpmsProperties;
import com.hccake.ballcat.system.service.SysUserService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

/**
 * @author Hccake
 * @version 1.0
 * @date 2020/5/25 21:01
 */
@EnableAsync
@MapperScan("com.hccake.ballcat.**.mapper")
@ComponentScan({ "com.hccake.ballcat.admin", "com.hccake.ballcat.auth", "com.hccake.ballcat.system",
		"com.hccake.ballcat.log", "com.hccake.ballcat.file", "com.hccake.ballcat.notify" })
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({ UpmsProperties.class, SecurityProperties.class })
@EnableOauth2AuthorizationServer
@EnableOauth2ResourceServer
public class UpmsAutoConfiguration {

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
	 * 用户详情处理类
	 *
	 * @author hccake
	 */
	@Configuration(proxyBeanMethods = false)
	@ConditionalOnClass(SysUserService.class)
	@ConditionalOnMissingBean(UserDetailsService.class)
	static class UserDetailsServiceConfiguration {

		/**
		 * 用户详情处理类
		 * @return SysUserDetailsServiceImpl
		 */
		@Bean
		@ConditionalOnMissingBean
		public UserDetailsService userDetailsService(SysUserService sysUserService,
				UserInfoCoordinator userInfoCoordinator) {
			return new SysUserDetailsServiceImpl(sysUserService, userInfoCoordinator);
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

}
