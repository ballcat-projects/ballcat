package com.hccake.common.excel.handler;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.hccake.common.excel.annotation.ResponseExcel;
import com.hccake.common.excel.annotation.Sheet;
import com.hccake.common.excel.config.ExcelConfigProperties;
import com.hccake.common.excel.domain.SheetBuildProperties;
import com.hccake.common.excel.enhance.WriterBuilderEnhancer;
import com.hccake.common.excel.kit.ExcelException;
import org.springframework.beans.factory.ObjectProvider;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lengleng
 * @date 2020/3/29
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
