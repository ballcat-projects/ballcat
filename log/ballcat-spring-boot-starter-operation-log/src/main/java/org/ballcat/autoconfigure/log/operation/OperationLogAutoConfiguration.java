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

package org.ballcat.autoconfigure.log.operation;

import org.ballcat.autoconfigure.log.operation.properties.OperationLogProperties;
import org.ballcat.log.operation.OperationLogAnnotationAdvisor;
import org.ballcat.log.operation.OperationLogMethodInterceptor;
import org.ballcat.log.operation.expression.OperationLogExpressionEvaluator;
import org.ballcat.log.operation.handler.DefaultOperationLogHandler;
import org.ballcat.log.operation.handler.OperationLogHandler;
import org.ballcat.log.operation.provider.DefaultHttpInfoProvider;
import org.ballcat.log.operation.provider.DefaultOperatorProvider;
import org.ballcat.log.operation.provider.DefaultTraceIdProvider;
import org.ballcat.log.operation.provider.HttpInfoProvider;
import org.ballcat.log.operation.provider.OperatorProvider;
import org.ballcat.log.operation.provider.TraceIdProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author Hccake 2019/10/15 18:20
 */
@EnableConfigurationProperties(OperationLogProperties.class)
@ConditionalOnProperty(prefix = OperationLogProperties.PREFIX, name = "enabled", matchIfMissing = true,
		havingValue = "true")
public class OperationLogAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean(OperationLogExpressionEvaluator.class)
	public OperationLogExpressionEvaluator operationLogExpressionEvaluator() {
		return new OperationLogExpressionEvaluator();
	}

	@Bean(name = "defaultOperatorProvider")
	@ConditionalOnMissingBean(name = "defaultOperatorProvider")
	public OperatorProvider defaultOperatorProvider() {
		return new DefaultOperatorProvider();
	}

	@Bean(name = "defaultTraceIdProvider")
	@ConditionalOnMissingBean(name = "defaultTraceIdProvider")
	public TraceIdProvider defaultTraceIdProvider() {
		return new DefaultTraceIdProvider();
	}

	@Bean
	@ConditionalOnMissingBean(DefaultHttpInfoProvider.class)
	public HttpInfoProvider defaultHttpInfoProvider() {
		return new DefaultHttpInfoProvider();
	}

	@Bean
	@ConditionalOnMissingBean(DefaultOperationLogHandler.class)
	public OperationLogHandler defaultOperationLogHandler() {
		return new DefaultOperationLogHandler();
	}

	/**
	 * 注册操作日志拦截器
	 * @return OperationLogMethodInterceptor
	 */
	@Bean
	public OperationLogMethodInterceptor operationLogMethodInterceptor(
			OperationLogExpressionEvaluator operationLogExpressionEvaluator) {
		return new OperationLogMethodInterceptor(operationLogExpressionEvaluator);
	}

	/**
	 * 注册操作日志增强器
	 * @return OperationLogAnnotationAdvisor
	 */
	@Bean
	public OperationLogAnnotationAdvisor operationLogAnnotationAdvisor(
			OperationLogMethodInterceptor operationLogMethodInterceptor) {
		return new OperationLogAnnotationAdvisor(operationLogMethodInterceptor);
	}

}
