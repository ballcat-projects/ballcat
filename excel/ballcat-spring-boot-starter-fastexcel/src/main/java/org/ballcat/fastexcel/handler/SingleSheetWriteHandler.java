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
import org.ballcat.fastexcel.annotation.Sheet;
import org.ballcat.fastexcel.config.ExcelConfigProperties;
import org.ballcat.fastexcel.domain.SheetBuildProperties;
import org.ballcat.fastexcel.enhance.WriterBuilderEnhancer;
import org.ballcat.fastexcel.fill.FillDataSupplier;
import org.ballcat.fastexcel.kit.ExcelException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.ObjectProvider;

/**
 * 处理单sheet 页面
 *
 * @author lengleng 2020/3/29
 */
public class SingleSheetWriteHandler extends AbstractSheetWriteHandler {

	public SingleSheetWriteHandler(ExcelConfigProperties configProperties,
			ObjectProvider<List<Converter<?>>> converterProvider, WriterBuilderEnhancer excelWriterBuilderEnhance) {
		super(configProperties, converterProvider, excelWriterBuilderEnhance);
	}

	/**
	 * obj 是List 且list不为空同时list中的元素不是是List 才返回true
	 * @param obj 返回对象
	 * @return boolean
	 */
	@Override
	public boolean support(Object obj) {
		if (obj instanceof List) {
			List<?> objList = (List<?>) obj;
			return !objList.isEmpty() && !(objList.get(0) instanceof List);
		}
		else {
			throw new ExcelException("@ResponseExcel 返回值必须为List类型");
		}
	}

	@Override
	public void write(Object obj, HttpServletResponse response, ResponseExcel responseExcel) {
		List<?> eleList = (List<?>) obj;
		ExcelWriter excelWriter = getExcelWriter(response, responseExcel);

		// 获取 Sheet 配置
		SheetBuildProperties sheetBuildProperties;
		Sheet[] sheets = responseExcel.sheets();
		if (sheets != null && sheets.length > 0) {
			sheetBuildProperties = new SheetBuildProperties(sheets[0]);
		}
		else {
			sheetBuildProperties = new SheetBuildProperties(0);
		}

		// 模板信息
		String template = responseExcel.template();

		// 创建sheet
		WriteSheet sheet;
		if (eleList.isEmpty()) {
			sheet = this.emptySheet(sheetBuildProperties, template);
		}
		else {
			Class<?> dataClass = eleList.get(0).getClass();
			sheet = this.emptySheet(sheetBuildProperties, dataClass, template, responseExcel.headGenerator());
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

		excelWriter.finish();
	}

}
