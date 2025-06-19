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

package org.ballcat.fastexcel.handler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletOutputStream;

import cn.idev.excel.ExcelWriter;
import cn.idev.excel.FastExcel;
import cn.idev.excel.converters.Converter;
import cn.idev.excel.write.builder.AbstractExcelWriterParameterBuilder;
import cn.idev.excel.write.builder.ExcelWriterBuilder;
import cn.idev.excel.write.builder.ExcelWriterSheetBuilder;
import cn.idev.excel.write.handler.WriteHandler;
import cn.idev.excel.write.metadata.WriteSheet;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import org.ballcat.fastexcel.annotation.ResponseExcel;
import org.ballcat.fastexcel.config.ExcelConfigProperties;
import org.ballcat.fastexcel.context.ExcelExportInfo;
import org.ballcat.fastexcel.domain.SheetBuildProperties;
import org.ballcat.fastexcel.enhance.WriterBuilderEnhancer;
import org.ballcat.fastexcel.head.HeadGenerator;
import org.ballcat.fastexcel.head.HeadMeta;
import org.ballcat.fastexcel.head.I18nHeaderCellWriteHandler;
import org.ballcat.fastexcel.kit.ExcelException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.ClassPathResource;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * @author lengleng
 * @author L.cm
 * @author Hccake 2020/3/31
 */
@RequiredArgsConstructor
public abstract class AbstractSheetWriteHandler implements SheetWriteHandler, ApplicationContextAware {

	private final ExcelConfigProperties configProperties;

	private final ObjectProvider<List<Converter<?>>> converterProvider;

	private final WriterBuilderEnhancer excelWriterBuilderEnhance;

	private ApplicationContext applicationContext;

	@Getter
	@Setter
	@Autowired(required = false)
	private I18nHeaderCellWriteHandler i18nHeaderCellWriteHandler;

	@Override
	public void check(ResponseExcel responseExcel) {
		if (responseExcel.fill() && !StringUtils.hasText(responseExcel.template())) {
			throw new ExcelException("@ResponseExcel fill 必须配合 template 使用");
		}
	}

	@Override
	public void export(Object resultObject, ServletOutputStream outputStream, ResponseExcel responseExcel,
			ExcelExportInfo excelExportInfo) {
		ExcelWriter excelWriter = getExcelWriter(outputStream, responseExcel, excelExportInfo);
		this.write(resultObject, responseExcel, excelWriter, excelExportInfo);
	}

	@Override
	public void export(Object resultObject, ZipOutputStream outputStream, ResponseExcel responseExcel,
			ExcelExportInfo excelExportInfo) throws IOException {
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
			ExcelWriter excelWriter = getExcelWriter(bos, responseExcel, excelExportInfo);
			this.write(resultObject, responseExcel, excelWriter, excelExportInfo);

			// 将内存中的 Excel 数据写入 ZIP 条目
			String excelFileName = excelExportInfo.getFileName();
			ZipEntry zipEntry = new ZipEntry(excelFileName);
			outputStream.putNextEntry(zipEntry);
			outputStream.write(bos.toByteArray());
			outputStream.closeEntry();
		}
	}

	/**
	 * 通用的获取ExcelWriter方法
	 * @param outputStream OutputStream
	 * @param responseExcel ResponseExcel注解
	 * @return ExcelWriter
	 */
	@SneakyThrows(IOException.class)
	public ExcelWriter getExcelWriter(OutputStream outputStream, ResponseExcel responseExcel,
			ExcelExportInfo excelExportInfo) {
		ExcelWriterBuilder writerBuilder = FastExcel.write(outputStream)
			.autoCloseStream(true)
			.excelType(responseExcel.suffix())
			.inMemory(responseExcel.inMemory());

		if (StringUtils.hasText(responseExcel.password())) {
			writerBuilder.password(responseExcel.password());
		}

		if (responseExcel.include().length != 0) {
			writerBuilder.includeColumnFieldNames(Arrays.asList(responseExcel.include()));
		}

		if (responseExcel.exclude().length != 0) {
			writerBuilder.excludeColumnFieldNames(Arrays.asList(responseExcel.exclude()));
		}

		for (Class<? extends WriteHandler> clazz : responseExcel.writeHandler()) {
			writerBuilder.registerWriteHandler(BeanUtils.instantiateClass(clazz));
		}

		// 开启国际化头信息处理
		if (responseExcel.i18nHeader() && this.i18nHeaderCellWriteHandler != null) {
			writerBuilder.registerWriteHandler(this.i18nHeaderCellWriteHandler);
		}

		// 自定义注入的转换器
		registerCustomConverter(writerBuilder);

		for (Class<? extends Converter> clazz : responseExcel.converter()) {
			writerBuilder.registerConverter(BeanUtils.instantiateClass(clazz));
		}

		String templatePath = this.configProperties.getTemplatePath();
		if (StringUtils.hasText(responseExcel.template())) {
			ClassPathResource classPathResource = new ClassPathResource(
					templatePath + File.separator + excelExportInfo.getTemplate());
			InputStream inputStream = classPathResource.getInputStream();
			writerBuilder.withTemplate(inputStream);
		}

		// 头信息增强
		Class<? extends HeadGenerator> headGenerateClass = null;
		if (isNotInterface(responseExcel.headGenerateClass())) {
			headGenerateClass = responseExcel.headGenerateClass();
		}
		// 优先使用注解中的 headClass，其次使用使用实际数据类型 dataClass
		Class<?> headClass = responseExcel.headClass();
		// 定义头信息增强则使用其生成头信息，否则使用 headClass 来自动获取
		if (headGenerateClass != null) {
			fillCustomHeadInfo(headClass, headGenerateClass, writerBuilder);
		}
		else if (headClass != null && !Object.class.equals(headClass)) {
			writerBuilder.head(headClass);
		}

		writerBuilder = this.excelWriterBuilderEnhance.enhanceExcel(writerBuilder, responseExcel, templatePath);

		return writerBuilder.build();
	}

	/**
	 * 自定义注入转换器 如果有需要，子类自己重写
	 * @param builder ExcelWriterBuilder
	 */
	public void registerCustomConverter(ExcelWriterBuilder builder) {
		this.converterProvider.ifAvailable(converters -> converters.forEach(builder::registerConverter));
	}

	/**
	 * 获取 WriteSheet 对象
	 * @param sheetBuildProperties sheet annotation info
	 * @param dataClass 数据类型
	 * @param template 模板
	 * @return WriteSheet
	 */
	public WriteSheet emptySheet(SheetBuildProperties sheetBuildProperties, Class<?> dataClass, String template) {

		// Sheet 编号和名称
		Integer sheetNo = sheetBuildProperties.getSheetNo() >= 0 ? sheetBuildProperties.getSheetNo() : null;
		String sheetName = sheetBuildProperties.getSheetName();

		// 是否模板写入
		boolean hasTemplate = StringUtils.hasText(template);
		ExcelWriterSheetBuilder writerSheetBuilder = hasTemplate ? FastExcel.writerSheet(sheetNo)
				: FastExcel.writerSheet(sheetNo, sheetName);

		// 头信息增强
		Class<? extends HeadGenerator> headGenerateClass = null;
		if (!hasTemplate) {
			if (isNotInterface(sheetBuildProperties.getHeadGenerateClass())) {
				headGenerateClass = sheetBuildProperties.getHeadGenerateClass();
			}

			// 优先使用注解中的 headClass，其次使用使用实际数据类型 dataClass
			Class<?> headClass = sheetBuildProperties.getHeadClass();
			if (headClass == null || Object.class.equals(headClass)) {
				headClass = dataClass;
			}

			// 定义头信息增强则使用其生成头信息，否则使用 headClass 来自动获取
			if (headGenerateClass != null) {
				fillCustomHeadInfo(headClass, headGenerateClass, writerSheetBuilder);
			}
			else if (headClass != null) {
				writerSheetBuilder.head(headClass);
			}
		}

		if (sheetBuildProperties.getExcludes().length > 0) {
			writerSheetBuilder.excludeColumnFieldNames(Arrays.asList(sheetBuildProperties.getExcludes()));
		}
		if (sheetBuildProperties.getIncludes().length > 0) {
			writerSheetBuilder.includeColumnFieldNames(Arrays.asList(sheetBuildProperties.getIncludes()));
		}

		// sheetBuilder 增强
		writerSheetBuilder = this.excelWriterBuilderEnhance.enhanceSheet(writerSheetBuilder, sheetNo, sheetName,
				dataClass, template, headGenerateClass);

		return writerSheetBuilder.build();
	}

	private void fillCustomHeadInfo(Class<?> dataClass, Class<? extends HeadGenerator> headEnhancerClass,
			AbstractExcelWriterParameterBuilder<?, ?> writerSheetBuilder) {
		HeadGenerator headGenerator = this.applicationContext.getBean(headEnhancerClass);
		Assert.notNull(headGenerator, "The header generated bean does not exist.");
		HeadMeta head = headGenerator.head(dataClass);
		writerSheetBuilder.head(head.getHead());
		writerSheetBuilder.excludeColumnFieldNames(head.getIgnoreHeadFields());
	}

	/**
	 * 是否为Null Head Generator
	 * @param headGeneratorClass 头生成器类型
	 * @return true 已指定 false 未指定(默认值)
	 */
	private boolean isNotInterface(Class<? extends HeadGenerator> headGeneratorClass) {
		return !Modifier.isInterface(headGeneratorClass.getModifiers());
	}

	@Override
	public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}
