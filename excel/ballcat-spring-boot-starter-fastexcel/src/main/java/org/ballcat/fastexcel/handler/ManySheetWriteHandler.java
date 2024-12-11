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
import org.ballcat.fastexcel.fill.FillDataSupplier;
import org.ballcat.fastexcel.kit.ExcelException;
import org.springframework.beans.BeanUtils;
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
	 * @param obj 返回对象
	 * @return boolean
	 */
	@Override
	public boolean support(Object obj) {
		if (obj instanceof List) {
			List<?> objList = (List<?>) obj;
			return !objList.isEmpty() && objList.get(0) instanceof List;
		}
		else {
			throw new ExcelException("@ResponseExcel 返回值必须为List类型");
		}
	}

	@Override
	public void write(Object obj, HttpServletResponse response, ResponseExcel responseExcel) {
		List<?> objList = (List<?>) obj;
		int objListSize = objList.size();

		String template = responseExcel.template();

		ExcelWriter excelWriter = getExcelWriter(response, responseExcel);
		List<SheetBuildProperties> sheetBuildPropertiesList = getSheetBuildProperties(responseExcel, objListSize);

		for (int i = 0; i < sheetBuildPropertiesList.size(); i++) {
			SheetBuildProperties sheetBuildProperties = sheetBuildPropertiesList.get(i);
			// 创建sheet
			WriteSheet sheet;
			List<?> eleList;
			if (objListSize <= i) {
				eleList = new ArrayList<>();
				sheet = this.emptySheet(sheetBuildProperties, template);
			}
			else {
				eleList = (List<?>) objList.get(i);
				if (eleList.isEmpty()) {
					sheet = this.emptySheet(sheetBuildProperties, template);
				}
				else {
					Class<?> dataClass = eleList.get(0).getClass();
					sheet = this.emptySheet(sheetBuildProperties, dataClass, template, responseExcel.headGenerator());
				}
			}

			if (responseExcel.fill()) {
				// 填充 sheet
				excelWriter.fill(eleList, sheet);
				Class<? extends FillDataSupplier> fillDataSupplierClazz = responseExcel.fillDataSupplier();
				if (fillDataSupplierClazz != null && !fillDataSupplierClazz.isInterface()) {
					FillDataSupplier fillDataSupplier = BeanUtils.instantiateClass(fillDataSupplierClazz);
					Object fillData = fillDataSupplier.getFillData();
					if (fillData != null) {
						excelWriter.fill(fillData, sheet);
					}
				}
			}
			else {
				// 写入 sheet
				excelWriter.write(eleList, sheet);
			}
		}

		excelWriter.finish();
	}

	private static List<SheetBuildProperties> getSheetBuildProperties(ResponseExcel responseExcel, int objListSize) {
		List<SheetBuildProperties> sheetBuildPropertiesList = new ArrayList<>();
		Sheet[] sheets = responseExcel.sheets();
		if (sheets != null && sheets.length > 0) {
			for (Sheet sheet : sheets) {
				sheetBuildPropertiesList.add(new SheetBuildProperties(sheet));
			}
		}
		else {
			for (int i = 0; i < objListSize; i++) {
				sheetBuildPropertiesList.add(new SheetBuildProperties(i));
			}
		}
		return sheetBuildPropertiesList;
	}

}
