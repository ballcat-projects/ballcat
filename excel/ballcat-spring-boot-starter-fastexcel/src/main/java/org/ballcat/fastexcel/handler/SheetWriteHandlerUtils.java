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

import cn.idev.excel.ExcelWriter;
import cn.idev.excel.write.metadata.WriteSheet;
import org.ballcat.fastexcel.annotation.ResponseExcel;
import org.ballcat.fastexcel.annotation.Sheet;
import org.ballcat.fastexcel.domain.SheetBuildProperties;
import org.ballcat.fastexcel.fill.FillDataSupplier;
import org.springframework.beans.BeanUtils;

/**
 * 内部工具类。
 *
 * @author Hccake
 */
final class SheetWriteHandlerUtils {

	private SheetWriteHandlerUtils() {
	}

	public static SheetBuildProperties getSheetBuildProperties(ResponseExcel responseExcel) {
		List<SheetBuildProperties> sheetBuildPropertiesList = getSheetBuildPropertiesList(responseExcel, 1);
		return sheetBuildPropertiesList.get(0);
	}

	public static List<SheetBuildProperties> getSheetBuildPropertiesList(ResponseExcel responseExcel, int objListSize) {
		List<SheetBuildProperties> sheetBuildPropertiesList = new ArrayList<>();
		Sheet[] sheets = responseExcel.sheets();
		if (sheets != null && sheets.length > 0) {
			for (int i = 0; i < sheets.length; i++) {
				sheetBuildPropertiesList.add(new SheetBuildProperties(sheets[i], i));
			}
		}
		else {
			for (int i = 0; i < objListSize; i++) {
				sheetBuildPropertiesList.add(new SheetBuildProperties(i));
			}
		}
		return sheetBuildPropertiesList;
	}

	public static void writeOrFillExcel(ResponseExcel responseExcel, ExcelWriter excelWriter, List<?> eleList,
			WriteSheet sheet) {
		if (responseExcel.fill()) {
			// 填充 sheet
			excelWriter.fill(eleList, sheet);
			Object customFillData = SheetWriteHandlerUtils.getCustomFillData(responseExcel);
			if (customFillData != null) {
				excelWriter.fill(customFillData, sheet);
			}
		}
		else {
			// 写入 sheet
			excelWriter.write(eleList, sheet);
		}
	}

	public static Object getCustomFillData(ResponseExcel responseExcel) {
		Class<? extends FillDataSupplier> fillDataSupplierClazz = responseExcel.fillDataSupplier();
		if (fillDataSupplierClazz != null && !fillDataSupplierClazz.isInterface()) {
			FillDataSupplier fillDataSupplier = BeanUtils.instantiateClass(fillDataSupplierClazz);
			return fillDataSupplier.getFillData();
		}
		return null;
	}

}
