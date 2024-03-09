/*
 * Copyright 2023-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ballcat.autoconfigure.web.exception;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.ballcat.common.core.exception.handler.GlobalExceptionHandler;
import org.ballcat.web.exception.handler.DoNothingGlobalExceptionHandler;
import org.ballcat.web.exception.handler.NoticeGlobalExceptionHandler;
import org.ballcat.web.exception.notice.ExceptionNoticeConfig;
import org.ballcat.web.exception.notice.ExceptionNotifier;
import org.ballcat.web.exception.resolver.GlobalHandlerExceptionResolver;
import org.ballcat.web.exception.resolver.SecurityHandlerExceptionResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.access.AccessDeniedException;

/**
 * @author Hccake 2019/10/15 18:20
 */
@Import(ExceptionNoticeConfiguration.class)
@RequiredArgsConstructor
@AutoConfiguration
@EnableConfigurationProperties({ ExceptionNoticeProperties.class, WebExceptionProperties.class })
public class ExceptionAutoConfiguration {

	/**
	 * 发送异常通知的全局异常处理器
	 * @return {@link NoticeGlobalExceptionHandler}
	 */
	@Bean
	@ConditionalOnBean(ExceptionNotifier.class)
	@ConditionalOnMissingBean(GlobalExceptionHandler.class)
	public GlobalExceptionHandler noticeGlobalExceptionHandler(
			@Value("${spring.application.name: unknown-application}") String applicationName,
			ExceptionNoticeProperties exceptionNoticeProperties, List<ExceptionNotifier> exceptionNotifiers) {

		ExceptionNoticeConfig noticeConfig = new ExceptionNoticeConfig();
		noticeConfig.setTime(exceptionNoticeProperties.getTime());
		noticeConfig.setMax(exceptionNoticeProperties.getMax());
		noticeConfig.setLength(exceptionNoticeProperties.getLength());
		noticeConfig.setIgnoreExceptions(exceptionNoticeProperties.getIgnoreExceptions());
		noticeConfig.setIgnoreChild(exceptionNoticeProperties.getIgnoreChild());

		return new NoticeGlobalExceptionHandler(applicationName, noticeConfig, exceptionNotifiers);
	}

	/**
	 * 什么都不做的异常处理器
	 * @return {@link DoNothingGlobalExceptionHandler}
	 */
	@Bean
	@ConditionalOnMissingBean(GlobalExceptionHandler.class)
	public GlobalExceptionHandler doNothingGlobalExceptionHandler() {
		return new DoNothingGlobalExceptionHandler();
	}

	/**
	 * 默认的异常处理器
	 * @return GlobalHandlerExceptionResolver
	 */
	@Bean
	@ConditionalOnMissingBean(GlobalHandlerExceptionResolver.class)
	public GlobalHandlerExceptionResolver globalExceptionHandlerResolver(GlobalExceptionHandler globalExceptionHandler,
			WebExceptionProperties webExceptionProperties) {
		GlobalHandlerExceptionResolver globalHandlerExceptionResolver = new GlobalHandlerExceptionResolver(
				globalExceptionHandler);
		WebExceptionProperties.ExceptionResolverConfig resolverConfig = webExceptionProperties.getResolverConfig();
		globalHandlerExceptionResolver.setHideExceptionDetails(resolverConfig.getHideExceptionDetails());
		globalHandlerExceptionResolver.setHiddenMessage(resolverConfig.getHiddenMessage());
		globalHandlerExceptionResolver.setNpeErrorMessage(resolverConfig.getNpeErrorMessage());
		return globalHandlerExceptionResolver;
	}

	/**
	 * Security 异常处理，隔离出一个配置类
	 */
	@ConditionalOnClass(AccessDeniedException.class)
	static class SecurityExceptionConfiguration {

		/**
		 * security 相关的异常处理
		 * @return SecurityHandlerExceptionResolver
		 */
		@Bean
		@ConditionalOnMissingBean(SecurityHandlerExceptionResolver.class)
		public SecurityHandlerExceptionResolver securityHandlerExceptionResolver(
				GlobalExceptionHandler globalExceptionHandler) {
			return new SecurityHandlerExceptionResolver(globalExceptionHandler);
		}

	}

}
