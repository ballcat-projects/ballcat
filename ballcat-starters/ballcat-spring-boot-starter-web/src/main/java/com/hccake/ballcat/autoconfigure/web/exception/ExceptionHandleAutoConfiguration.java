package com.hccake.ballcat.autoconfigure.web.exception;

import com.hccake.ballcat.autoconfigure.web.exception.handler.DefaultGlobalExceptionHandler;
import com.hccake.ballcat.autoconfigure.web.exception.handler.DingTalkGlobalExceptionHandler;
import com.hccake.ballcat.autoconfigure.web.exception.handler.MailGlobalExceptionHandler;
import com.hccake.ballcat.common.core.exception.handler.GlobalExceptionHandler;
import com.hccake.ballcat.common.mail.sender.MailSender;
import com.hccake.extend.dingtalk.DingTalkSender;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/10/15 18:20
 */
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class ExceptionHandleAutoConfiguration {

	@Value("${spring.application.name}")
	private String applicationName;

	/**
	 * 默认的日志处理器
	 * @return DefaultExceptionHandler
	 */
	@Bean
	@ConditionalOnMissingBean(GlobalExceptionHandler.class)
	@ConditionalOnProperty(prefix = "ballcat.exception", matchIfMissing = true, name = "type", havingValue = "NONE")
	public GlobalExceptionHandler defaultGlobalExceptionHandler() {
		return new DefaultGlobalExceptionHandler();
	}

	/**
	 * 钉钉消息通知的日志处理器
	 *
	 * @author lingting 2020-06-12 00:32:40
	 */
	@Bean
	@ConditionalOnMissingBean(GlobalExceptionHandler.class)
	@ConditionalOnProperty(prefix = "ballcat.exception", name = "type", havingValue = "DING_TALK")
	public GlobalExceptionHandler dingTalkGlobalExceptionHandler(ExceptionHandleProperties exceptionHandleProperties,
			ApplicationContext context) {
		return new DingTalkGlobalExceptionHandler(exceptionHandleProperties, context.getBean(DingTalkSender.class),
				applicationName);
	}

	/**
	 * 邮件消息通知的日志处理器
	 *
	 * @author lingting 2020-06-12 00:32:40
	 */
	@Bean
	@ConditionalOnMissingBean(GlobalExceptionHandler.class)
	@ConditionalOnProperty(prefix = "ballcat.exception", name = "type", havingValue = "MAIL")
	public GlobalExceptionHandler mailGlobalExceptionHandler(ExceptionHandleProperties exceptionHandleProperties,
			ApplicationContext context) {
		return new MailGlobalExceptionHandler(exceptionHandleProperties, context.getBean(MailSender.class),
				applicationName);
	}

	/**
	 * 默认的日志处理器
	 * @return DefaultExceptionHandler
	 */
	@Bean
	@ConditionalOnMissingBean(GlobalExceptionHandlerResolver.class)
	public GlobalExceptionHandlerResolver globalExceptionHandlerResolver(
			GlobalExceptionHandler globalExceptionHandler) {
		return new GlobalExceptionHandlerResolver(globalExceptionHandler);
	}

}
