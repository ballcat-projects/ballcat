package com.hccake.ballcat.admin.upms;

import com.hccake.ballcat.auth.annotation.EnableOauth2AuthorizationServer;
import com.hccake.ballcat.common.log.operation.enums.LogStatusEnum;
import com.hccake.ballcat.common.security.util.SecurityUtils;
import com.hccake.ballcat.log.enums.LoginEventTypeEnum;
import com.hccake.ballcat.log.model.entity.LoginLog;
import com.hccake.ballcat.log.service.LoginLogService;
import com.hccake.ballcat.system.authentication.CustomTokenEnhancer;
import com.hccake.ballcat.system.authentication.DefaultUserInfoCoordinatorImpl;
import com.hccake.ballcat.system.authentication.SysUserDetailsServiceImpl;
import com.hccake.ballcat.system.authentication.UserInfoCoordinator;
import com.hccake.ballcat.system.properties.SystemProperties;
import com.hccake.ballcat.system.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.ballcat.security.properties.SecurityProperties;
import org.ballcat.springsecurity.oauth2.server.authorization.authentication.OAuth2TokenRevocationResultAuthenticationToken;
import org.ballcat.springsecurity.oauth2.server.resource.SharedStoredOpaqueTokenIntrospector;
import org.ballcat.springsecurity.oauth2.server.resource.annotation.EnableOauth2ResourceServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;

import static com.hccake.ballcat.log.handler.LoginLogUtils.prodLoginLog;

/**
 * @author Hccake
 * @version 1.0
 * @date 2020/5/25 21:01
 */
@EnableAsync
@AutoConfiguration
@MapperScan("com.hccake.ballcat.**.mapper")
@ComponentScan({ "com.hccake.ballcat.admin.upms", "com.hccake.ballcat.auth", "com.hccake.ballcat.system",
		"com.hccake.ballcat.log", "com.hccake.ballcat.file", "com.hccake.ballcat.notify" })
@EnableConfigurationProperties({ SystemProperties.class, SecurityProperties.class })
@EnableOauth2AuthorizationServer
@EnableOauth2ResourceServer
public class UpmsAutoConfiguration {

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
			return new DefaultUserInfoCoordinatorImpl();
		}

	}

	/**
	 * 老版本 spring-oauth2 使用配置类
	 */
	@Deprecated
	@Configuration(proxyBeanMethods = false)
	@ConditionalOnClass(TokenEnhancer.class)
	static class SpringOAuth2Configuration {

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

	}

	@Configuration(proxyBeanMethods = false)
	@ConditionalOnClass(value = { OAuth2AccessTokenAuthenticationToken.class, LoginLogService.class })
	@ConditionalOnBean(LoginLogService.class)
	@RequiredArgsConstructor
	static class LoginLogConfiguration {

		private final LoginLogService loginLogService;

		/**
		 * 登陆成功时间监听 记录用户登录日志
		 * @param event 登陆成功 event
		 */
		@EventListener(AuthenticationSuccessEvent.class)
		public void onAuthenticationSuccessEvent(AuthenticationSuccessEvent event) {
			Object source = event.getSource();
			if (source instanceof OAuth2AccessTokenAuthenticationToken) {
				String username = SecurityUtils.getAuthentication().getName();
				LoginLog loginLog = prodLoginLog(username).setMsg("登陆成功").setStatus(LogStatusEnum.SUCCESS.getValue())
						.setEventType(LoginEventTypeEnum.LOGIN.getValue());
				loginLogService.save(loginLog);
			}
		}

		/**
		 * 登出成功事件监听
		 * @param event the event
		 */
		@EventListener(LogoutSuccessEvent.class)
		public void onLogoutSuccessEvent(LogoutSuccessEvent event) {
			Object source = event.getSource();
			String username = null;
			if (source instanceof OAuth2TokenRevocationResultAuthenticationToken) {
				OAuth2Authorization authorization = ((OAuth2TokenRevocationResultAuthenticationToken) source)
						.getAuthorization();
				username = authorization.getPrincipalName();
			}
			else if (source instanceof AbstractAuthenticationToken) {
				username = ((AbstractAuthenticationToken) source).getName();
			}
			if (username != null) {
				LoginLog loginLog = prodLoginLog(username).setMsg("登出成功")
						.setEventType(LoginEventTypeEnum.LOGOUT.getValue());
				loginLogService.save(loginLog);
			}
		}

	}

}
