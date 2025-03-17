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
import org.ballcat.fastexcel.annotation.Sheet;
import org.ballcat.fastexcel.config.ExcelConfigProperties;
import org.ballcat.fastexcel.domain.SheetBuildProperties;
import org.ballcat.fastexcel.enhance.WriterBuilderEnhancer;
import org.springframework.beans.factory.ObjectProvider;

/**
 * @author lengleng 2020/3/29
 */
public class ManySheetWriteHandler extends AbstractSheetWriteHandler {

	public ManySheetWriteHandler(ExcelConfigProperties configProperties,
			ObjectProvider<List<Converter<?>>> converterProvider, WriterBuilderEnhancer excelWriterBuilderEnhance) {
		super(configProperties, converterProvider, excelWriterBuilderEnhance);
	}

	/**
	 * 当且仅当List不为空且List中的元素也是List 才返回true
	 * @param resultObject 返回对象
	 * @param responseExcel 注解
	 * @return boolean
	 */
	@Override
	public boolean support(Object resultObject, ResponseExcel responseExcel) {
		if (!(resultObject instanceof List)) {
			return false;
		}

		List<?> list = (List<?>) resultObject;
		if (list.isEmpty()) {
			Sheet[] sheets = responseExcel.sheets();
			return sheets != null && sheets.length > 1;
		}
		else {
			return list.get(0) instanceof List;
		}
	}

	@Override
	public void write(Object resultObject, HttpServletResponse response, ResponseExcel responseExcel) {
		List<List<?>> returnValue = (List<List<?>>) resultObject;

		int objListSize = returnValue.size();

		String template = responseExcel.template();

		ExcelWriter excelWriter = getExcelWriter(response, responseExcel);
		List<SheetBuildProperties> sheetBuildPropertiesList = SheetWriteHandlerUtils
			.getSheetBuildPropertiesList(responseExcel, objListSize);

		for (int i = 0; i < sheetBuildPropertiesList.size(); i++) {
			SheetBuildProperties sheetBuildProperties = sheetBuildPropertiesList.get(i);
			// 创建sheet
			WriteSheet sheet;
			List<?> eleList;

			if (objListSize <= i || returnValue.get(i).isEmpty()) {
				eleList = new ArrayList<>();
				sheet = this.emptySheet(sheetBuildProperties, null, template);
			}
			else {
				eleList = returnValue.get(i);
				Class<?> dataClass = eleList.get(0).getClass();
				sheet = this.emptySheet(sheetBuildProperties, dataClass, template);
			}

			SheetWriteHandlerUtils.writeOrFillExcel(responseExcel, excelWriter, eleList, sheet);
		}

		excelWriter.finish();
	}

}
