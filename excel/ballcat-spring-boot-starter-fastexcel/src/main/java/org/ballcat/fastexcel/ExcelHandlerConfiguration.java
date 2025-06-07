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

import java.util.List;

import javax.validation.Validator;

import cn.idev.excel.converters.Converter;
import lombok.RequiredArgsConstructor;
import org.ballcat.fastexcel.annotation.ResponseExcel;
import org.ballcat.fastexcel.annotation.ResponseExcelZip;
import org.ballcat.fastexcel.aop.ResponseExcelReturnValueHandler;
import org.ballcat.fastexcel.aop.ResponseExcelZipReturnValueHandler;
import org.ballcat.fastexcel.config.ExcelConfigProperties;
import org.ballcat.fastexcel.enhance.DefaultWriterBuilderEnhancer;
import org.ballcat.fastexcel.enhance.WriterBuilderEnhancer;
import org.ballcat.fastexcel.handler.IterableExcelDataSheetWriteHandler;
import org.ballcat.fastexcel.handler.ManySheetWriteHandler;
import org.ballcat.fastexcel.handler.NullDataSheetWriteHandler;
import org.ballcat.fastexcel.handler.SheetWriteHandler;
import org.ballcat.fastexcel.handler.SingleSheetWriteHandler;
import org.ballcat.fastexcel.head.I18nHeaderCellWriteHandler;
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
	 * ExcelBuild增强.
	 * @return DefaultWriterBuilderEnhancer 默认什么也不做的增强器
	 */
	@Bean
	@ConditionalOnMissingBean
	public WriterBuilderEnhancer writerBuilderEnhancer() {
		return new DefaultWriterBuilderEnhancer();
	}

	/**
	 * 单sheet 写入处理器.
	 */
	@Bean
	@ConditionalOnMissingBean
	public SingleSheetWriteHandler singleSheetWriteHandler() {
		return new SingleSheetWriteHandler(this.configProperties, this.converterProvider, writerBuilderEnhancer());
	}

	/**
	 * 多sheet 写入处理器.
	 */
	@Bean
	@ConditionalOnMissingBean
	public ManySheetWriteHandler manySheetWriteHandler() {
		return new ManySheetWriteHandler(this.configProperties, this.converterProvider, writerBuilderEnhancer());
	}

	/**
	 * null 值，excel 写入处理器.
	 */
	@Bean
	@ConditionalOnMissingBean
	public NullDataSheetWriteHandler nullDataSheetWriteHandler() {
		return new NullDataSheetWriteHandler(this.configProperties, this.converterProvider, writerBuilderEnhancer());
	}

	/**
	 * 迭代 excel 写入处理器。
	 */
	@Bean
	@ConditionalOnMissingBean
	public IterableExcelDataSheetWriteHandler iterableExcelDataSheetWriteHandler() {
		return new IterableExcelDataSheetWriteHandler(this.configProperties, this.converterProvider,
				writerBuilderEnhancer());
	}

	/**
	 * 创建 Excel 响应处理器 Bean
	 * <p>
	 * 用于处理带有 {@link ResponseExcel} 注解的控制器方法返回值，将数据写入 Excel 并返回 HTTP 响应
	 * </p>
	 * @param sheetWriteHandlerList 页签写入处理器集合（用于支持不同格式/样式的 Excel 写入）
	 * @return 配置好的 {@link ResponseExcelReturnValueHandler} 实例
	 * @see ResponseExcelReturnValueHandler
	 * @see ConditionalOnMissingBean 仅当容器中不存在同类型 Bean 时创建
	 */
	@Bean
	@ConditionalOnMissingBean
	public ResponseExcelReturnValueHandler responseExcelReturnValueHandler(
			List<SheetWriteHandler> sheetWriteHandlerList) {
		return new ResponseExcelReturnValueHandler(sheetWriteHandlerList);
	}

	/**
	 * 创建 Excel ZIP 响应处理器 Bean
	 * <p>
	 * 用于处理带有 {@link ResponseExcelZip} 注解的控制器方法返回值，将多个 Excel 文件打包为 ZIP 格式返回
	 * </p>
	 * @param sheetWriteHandlerList 页签写入处理器集合（用于支持不同格式/样式的 Excel 写入）
	 * @return 配置好的 {@link ResponseExcelZipReturnValueHandler} 实例
	 * @see ResponseExcelZipReturnValueHandler
	 * @see ConditionalOnMissingBean 仅当容器中不存在同类型 Bean 时创建
	 */
	@Bean
	@ConditionalOnMissingBean
	public ResponseExcelZipReturnValueHandler responseExcelZipReturnValueHandler(
			List<SheetWriteHandler> sheetWriteHandlerList) {
		return new ResponseExcelZipReturnValueHandler(sheetWriteHandlerList);
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
