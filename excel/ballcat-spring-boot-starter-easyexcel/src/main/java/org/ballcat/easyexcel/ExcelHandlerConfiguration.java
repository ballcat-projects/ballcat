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

package org.ballcat.easyexcel;

import java.util.List;

import javax.validation.Validator;

import com.alibaba.excel.converters.Converter;
import lombok.RequiredArgsConstructor;
import org.ballcat.easyexcel.aop.ResponseExcelReturnValueHandler;
import org.ballcat.easyexcel.config.ExcelConfigProperties;
import org.ballcat.easyexcel.enhance.DefaultWriterBuilderEnhancer;
import org.ballcat.easyexcel.enhance.WriterBuilderEnhancer;
import org.ballcat.easyexcel.handler.ManySheetWriteHandler;
import org.ballcat.easyexcel.handler.SheetWriteHandler;
import org.ballcat.easyexcel.handler.SingleSheetWriteHandler;
import org.ballcat.easyexcel.head.I18nHeaderCellWriteHandler;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.validation.MessageInterpolatorFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * @author Hccake 2020/10/28
 *
 */
@RequiredArgsConstructor
@Configuration
public class ExcelHandlerConfiguration {

	private final ExcelConfigProperties configProperties;

	private final ObjectProvider<List<Converter<?>>> converterProvider;

	/**
	 * ExcelBuild增强
	 * @return DefaultWriterBuilderEnhancer 默认什么也不做的增强器
	 */
	@Bean
	@ConditionalOnMissingBean
	public WriterBuilderEnhancer writerBuilderEnhancer() {
		return new DefaultWriterBuilderEnhancer();
	}

	/**
	 * 单sheet 写入处理器
	 */
	@Bean
	@ConditionalOnMissingBean
	public SingleSheetWriteHandler singleSheetWriteHandler() {
		return new SingleSheetWriteHandler(this.configProperties, this.converterProvider, writerBuilderEnhancer());
	}

	/**
	 * 多sheet 写入处理器
	 */
	@Bean
	@ConditionalOnMissingBean
	public ManySheetWriteHandler manySheetWriteHandler() {
		return new ManySheetWriteHandler(this.configProperties, this.converterProvider, writerBuilderEnhancer());
	}

	/**
	 * 返回Excel文件的 response 处理器
	 * @param sheetWriteHandlerList 页签写入处理器集合
	 * @return ResponseExcelReturnValueHandler
	 */
	@Bean
	@ConditionalOnMissingBean
	public ResponseExcelReturnValueHandler responseExcelReturnValueHandler(
			List<SheetWriteHandler> sheetWriteHandlerList) {
		return new ResponseExcelReturnValueHandler(sheetWriteHandlerList);
	}

	/**
	 * excel 头的国际化处理器
	 * @param messageSource 国际化源
	 */
	@Bean
	@ConditionalOnBean(MessageSource.class)
	@ConditionalOnMissingBean
	public I18nHeaderCellWriteHandler i18nHeaderCellWriteHandler(MessageSource messageSource) {
		return new I18nHeaderCellWriteHandler(messageSource);
	}

	@Bean
	@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
	@ConditionalOnMissingBean(Validator.class)
	public static LocalValidatorFactoryBean validator(ApplicationContext applicationContext) {
		LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean();
		MessageInterpolatorFactory interpolatorFactory = new MessageInterpolatorFactory(applicationContext);
		factoryBean.setMessageInterpolator(interpolatorFactory.getObject());
		return factoryBean;
	}

}
