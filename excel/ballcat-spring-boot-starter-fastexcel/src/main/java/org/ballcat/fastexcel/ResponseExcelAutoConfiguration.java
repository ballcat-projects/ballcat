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

package org.ballcat.fastexcel;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.validation.Validator;

import lombok.RequiredArgsConstructor;
import org.ballcat.fastexcel.aop.DynamicNameAspect;
import org.ballcat.fastexcel.aop.RequestExcelArgumentResolver;
import org.ballcat.fastexcel.aop.ResponseExcelReturnValueHandler;
import org.ballcat.fastexcel.config.ExcelConfigProperties;
import org.ballcat.fastexcel.head.EmptyHeadGenerator;
import org.ballcat.fastexcel.processor.NameProcessor;
import org.ballcat.fastexcel.processor.NameSpelExpressionProcessor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

/**
 * 配置初始化
 *
 * @author lengleng 2020/3/29
 */
@AutoConfiguration
@Import(ExcelHandlerConfiguration.class)
@RequiredArgsConstructor
@EnableConfigurationProperties(ExcelConfigProperties.class)
public class ResponseExcelAutoConfiguration {

	private final RequestMappingHandlerAdapter requestMappingHandlerAdapter;

	private final ResponseExcelReturnValueHandler responseExcelReturnValueHandler;

	private final Validator validator;

	/**
	 * SPEL 解析处理器
	 * @return NameProcessor excel名称解析器
	 */
	@Bean
	@ConditionalOnMissingBean
	public NameProcessor nameProcessor() {
		return new NameSpelExpressionProcessor();
	}

	/**
	 * Excel名称解析处理切面
	 * @param nameProcessor SPEL 解析处理器
	 * @return DynamicNameAspect
	 */
	@Bean
	@ConditionalOnMissingBean
	public DynamicNameAspect dynamicNameAspect(NameProcessor nameProcessor) {
		return new DynamicNameAspect(nameProcessor);
	}

	/**
	 * 空的 Excel 头生成器
	 * @return EmptyHeadGenerator
	 */
	@Bean
	@ConditionalOnMissingBean
	public EmptyHeadGenerator emptyHeadGenerator() {
		return new EmptyHeadGenerator();
	}

	/**
	 * 追加 Excel返回值处理器 到 springmvc 中
	 */
	@PostConstruct
	public void setReturnValueHandlers() {
		List<HandlerMethodReturnValueHandler> returnValueHandlers = this.requestMappingHandlerAdapter
			.getReturnValueHandlers();

		List<HandlerMethodReturnValueHandler> newHandlers = new ArrayList<>();
		newHandlers.add(this.responseExcelReturnValueHandler);
		assert returnValueHandlers != null;
		newHandlers.addAll(returnValueHandlers);
		this.requestMappingHandlerAdapter.setReturnValueHandlers(newHandlers);
	}

	/**
	 * 追加 Excel 请求处理器 到 springmvc 中
	 */
	@PostConstruct
	public void setRequestExcelArgumentResolver() {
		List<HandlerMethodArgumentResolver> argumentResolvers = this.requestMappingHandlerAdapter
			.getArgumentResolvers();
		List<HandlerMethodArgumentResolver> resolverList = new ArrayList<>();
		resolverList.add(new RequestExcelArgumentResolver(this.validator));
		resolverList.addAll(argumentResolvers);
		this.requestMappingHandlerAdapter.setArgumentResolvers(resolverList);
	}

}
