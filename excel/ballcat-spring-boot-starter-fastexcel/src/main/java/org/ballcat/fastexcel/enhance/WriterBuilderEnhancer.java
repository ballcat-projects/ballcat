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

package org.ballcat.fastexcel.enhance;

import javax.servlet.http.HttpServletResponse;

import cn.idev.excel.write.builder.ExcelWriterBuilder;
import cn.idev.excel.write.builder.ExcelWriterSheetBuilder;
import org.ballcat.fastexcel.annotation.ResponseExcel;
import org.ballcat.fastexcel.head.HeadGenerator;

/**
 * ExcelWriterBuilder 增强
 *
 * @author Hccake 2020/12/18
 *
 */
public interface WriterBuilderEnhancer {

	/**
	 * ExcelWriterBuilder 增强
	 * @param writerBuilder ExcelWriterBuilder
	 * @param response HttpServletResponse
	 * @param responseExcel ResponseExcel
	 * @param templatePath 模板地址
	 * @return ExcelWriterBuilder
	 */
	ExcelWriterBuilder enhanceExcel(ExcelWriterBuilder writerBuilder, HttpServletResponse response,
			ResponseExcel responseExcel, String templatePath);

	/**
	 * ExcelWriterSheetBuilder 增强
	 * @param writerSheetBuilder ExcelWriterSheetBuilder
	 * @param sheetNo sheet角标
	 * @param sheetName sheet名，有模板时为空
	 * @param dataClass 当前写入的数据所属类
	 * @param template 模板文件
	 * @param headEnhancerClass 当前指定的自定义头处理器
	 * @return ExcelWriterSheetBuilder
	 */
	ExcelWriterSheetBuilder enhanceSheet(ExcelWriterSheetBuilder writerSheetBuilder, Integer sheetNo, String sheetName,
			Class<?> dataClass, String template, Class<? extends HeadGenerator> headEnhancerClass);

}
