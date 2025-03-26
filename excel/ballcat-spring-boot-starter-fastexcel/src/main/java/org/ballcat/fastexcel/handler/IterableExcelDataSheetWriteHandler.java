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

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import cn.idev.excel.ExcelWriter;
import cn.idev.excel.converters.Converter;
import cn.idev.excel.write.metadata.WriteSheet;
import org.ballcat.fastexcel.annotation.ResponseExcel;
import org.ballcat.fastexcel.config.ExcelConfigProperties;
import org.ballcat.fastexcel.domain.SheetBuildProperties;
import org.ballcat.fastexcel.enhance.WriterBuilderEnhancer;
import org.springframework.beans.factory.ObjectProvider;

/**
 * 迭代数据写入处理器。用于大批量的数据导出，数据通过实现 {@link IterableSheetDataProvider} 接口来提供。
 *
 * @author Hccake
 */
public class IterableExcelDataSheetWriteHandler extends AbstractSheetWriteHandler {

	public IterableExcelDataSheetWriteHandler(ExcelConfigProperties configProperties,
			ObjectProvider<List<Converter<?>>> converterProvider, WriterBuilderEnhancer excelWriterBuilderEnhance) {
		super(configProperties, converterProvider, excelWriterBuilderEnhance);
	}

	/**
	 * 仅支持返回类型是 IterableSheetDataProvider 的方法。
	 * @param resultObject 返回对象
	 * @param responseExcel 注解
	 * @return boolean
	 */
	@Override
	public boolean support(Object resultObject, ResponseExcel responseExcel) {
		return resultObject instanceof IterableSheetDataProvider;
	}

	@Override
	public void write(Object resultObject, HttpServletResponse response, ResponseExcel responseExcel) {
		IterableSheetDataProvider<?> sheetDataProvider = (IterableSheetDataProvider<?>) resultObject;

		ExcelWriter excelWriter = getExcelWriter(response, responseExcel);

		// 获取 Sheet 配置
		SheetBuildProperties sheetBuildProperties = new SheetBuildProperties(0);

		// 模板信息
		String template = responseExcel.template();

		// 数据类型
		Class<?> dataClass = sheetDataProvider.getDataClass();

		// 创建sheet
		WriteSheet sheet = this.emptySheet(sheetBuildProperties, dataClass, template);

		while (sheetDataProvider.hasNext()) {
			List<?> eleList = sheetDataProvider.next();
			SheetWriteHandlerUtils.writeOrFillExcel(responseExcel, excelWriter, eleList, sheet);
		}

		excelWriter.finish();
	}

}
