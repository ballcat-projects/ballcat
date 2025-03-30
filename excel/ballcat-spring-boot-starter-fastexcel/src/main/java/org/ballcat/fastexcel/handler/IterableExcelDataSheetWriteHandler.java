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

		// 模板信息
		String template = responseExcel.template();

		// 数据类型
		Class<?> dataClass = sheetDataProvider.getDataClass();

		int sheetRowLimit = sheetDataProvider.getSheetRowLimit();
		int currentRowCount = 0;
		int sheetIndex = 0;

		// 创建sheet
		WriteSheet sheet = createNewSheet(sheetDataProvider, sheetIndex, dataClass, template);
		// 遍历数据提供者返回的数据批次
		while (sheetDataProvider.hasNext()) {
			List<?> batchData = sheetDataProvider.next();
			int batchSize = batchData.size();
			int fromIndex = 0;

			// 按当前 sheet 剩余空间分片写入数据
			while (fromIndex < batchSize) {
				// 在每次循环开始时检查当前 sheet 是否已满，已满则创建新的 sheet，并重置当前行计数
				if (currentRowCount >= sheetRowLimit) {
					sheet = createNewSheet(sheetDataProvider, ++sheetIndex, dataClass, template);
					currentRowCount = 0;
				}

				// 计算当前 sheet 剩余可写入的行数
				int remaining = sheetRowLimit - currentRowCount;
				// 计算本次写入数据的结束索引，不能超过 batchData 总大小
				int toIndex = Math.min(fromIndex + remaining, batchSize);
				// 获取当前分片数据
				List<?> subList = batchData.subList(fromIndex, toIndex);

				// 立即写入当前分片数据
				flushSheetData(responseExcel, excelWriter, subList, sheet);
				// 更新当前 sheet 已写入的行数
				currentRowCount += subList.size();
				// 更新当前处理数据的起始索引
				fromIndex = toIndex;
			}
			// 此处 batchData 已经全部处理完，可以被GC回收
		}

		excelWriter.finish();
	}

	private WriteSheet createNewSheet(IterableSheetDataProvider<?> sheetDataProvider, int sheetIndex,
			Class<?> dataClass, String template) {
		String sheetName = sheetDataProvider.getSheetName(sheetIndex);
		SheetBuildProperties sheetBuildProperties = new SheetBuildProperties(sheetIndex);
		sheetBuildProperties.setSheetName(sheetName);
		return this.emptySheet(sheetBuildProperties, dataClass, template);
	}

	private void flushSheetData(ResponseExcel responseExcel, ExcelWriter excelWriter, List<?> data, WriteSheet sheet) {
		SheetWriteHandlerUtils.writeOrFillExcel(responseExcel, excelWriter, data, sheet);
	}

}
