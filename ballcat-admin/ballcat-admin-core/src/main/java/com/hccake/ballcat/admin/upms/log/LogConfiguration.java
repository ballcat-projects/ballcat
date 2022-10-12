package com.hccake.ballcat.admin.upms.log;

import com.hccake.ballcat.common.log.access.handler.AccessLogHandler;
import com.hccake.ballcat.common.log.operation.handler.OperationLogHandler;
import com.hccake.ballcat.log.handler.CustomAccessLogHandler;
import com.hccake.ballcat.log.handler.CustomOperationLogHandler;
import com.hccake.ballcat.log.model.entity.AccessLog;
import com.hccake.ballcat.log.model.entity.OperationLog;
import com.hccake.ballcat.log.service.AccessLogService;
import com.hccake.ballcat.log.service.LoginLogService;
import com.hccake.ballcat.log.service.OperationLogService;
import com.hccake.ballcat.log.thread.AccessLogSaveThread;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;

/**
 * @author hccake
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(LoginLogService.class)
public class LogConfiguration {

	/**
	 * 访问日志保存
	 * @param accessLogService 访问日志Service
	 * @return CustomAccessLogHandler
	 */
	@Bean
	@ConditionalOnBean(AccessLogService.class)
	@ConditionalOnMissingBean(AccessLogHandler.class)
	public AccessLogHandler<AccessLog> customAccessLogHandler(AccessLogService accessLogService) {
		return new CustomAccessLogHandler(new AccessLogSaveThread(accessLogService));
	}

	/**
	 * 操作日志处理器
	 * @param operationLogService 操作日志Service
	 * @return CustomOperationLogHandler
	 */
	@Bean
	@ConditionalOnBean(OperationLogService.class)
	@ConditionalOnMissingBean(OperationLogHandler.class)
	public OperationLogHandler<OperationLog> customOperationLogHandler(OperationLogService operationLogService) {
		return new CustomOperationLogHandler(operationLogService);
	}

	@ConditionalOnClass(TokenEnhancer.class)
	@ConditionalOnBean(LoginLogService.class)
	@ConditionalOnMissingBean(LoginLogHandler.class)
	@Configuration(proxyBeanMethods = false)
	static class SpringOauth2LoginLogConfiguration {

		/**
		 * Spring OAuth2 组件的登录日志处理，监听登录事件记录登录登出
		 * @param loginLogService 操作日志Service
		 * @return SpringOauth2LoginLogHandler
		 */
		@Bean
		@Deprecated
		public LoginLogHandler springOauth2LoginLogHandler(LoginLogService loginLogService) {
			return new SpringOauth2LoginLogHandler(loginLogService);
		}

	}

	@ConditionalOnClass(OAuth2AuthorizationServerConfigurer.class)
	@ConditionalOnBean(LoginLogService.class)
	@ConditionalOnMissingBean(LoginLogHandler.class)
	@Configuration(proxyBeanMethods = false)
	static class SpringAuthorizationServerLoginLogConfiguration {

		/**
		 * Spring Authorization Server 的登录日志处理，监听登录事件记录登录登出
		 * @param loginLogService 操作日志Service
		 * @param authorizationServerSettings 授权服务器设置
		 * @return SpringAuthorizationServerLoginLogHandler
		 */
		@Bean
		public LoginLogHandler springAuthorizationServerLoginLogHandler(LoginLogService loginLogService,
				AuthorizationServerSettings authorizationServerSettings) {
			return new SpringAuthorizationServerLoginLogHandler(loginLogService, authorizationServerSettings);
		}

	}

}