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

import java.util.ArrayList;
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
 * 处理返回值为 null 时的 excel 处理。
 *
 * @author Hccake
 * @since 2.0.0
 */
public class NullDataSheetWriteHandler extends AbstractSheetWriteHandler {

	public NullDataSheetWriteHandler(ExcelConfigProperties configProperties,
			ObjectProvider<List<Converter<?>>> converterProvider, WriterBuilderEnhancer excelWriterBuilderEnhance) {
		super(configProperties, converterProvider, excelWriterBuilderEnhance);
	}

	/**
	 * obj 是List 且list不为空同时list中的元素不是是List 才返回true
	 * @param resultObject 返回对象
	 * @param responseExcel 注解
	 * @return boolean
	 */
	@Override
	public boolean support(Object resultObject, ResponseExcel responseExcel) {
		return resultObject == null;
	}

	@Override
	public void write(Object resultObject, HttpServletResponse response, ResponseExcel responseExcel) {
		String template = responseExcel.template();
		ExcelWriter excelWriter = getExcelWriter(response, responseExcel);

		List<SheetBuildProperties> sheetBuildPropertiesList = SheetWriteHandlerUtils
			.getSheetBuildPropertiesList(responseExcel, 1);

		List<?> eleList = new ArrayList<>();

		for (SheetBuildProperties sheetBuildProperties : sheetBuildPropertiesList) {
			// 创建sheet
			WriteSheet sheet = this.emptySheet(sheetBuildProperties, null, template);

			SheetWriteHandlerUtils.writeOrFillExcel(responseExcel, excelWriter, eleList, sheet);
		}

	}

}
