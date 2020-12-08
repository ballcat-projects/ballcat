package com.hccake.common.excel.handler;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.hccake.common.excel.annotation.ResponseExcel;
import com.hccake.common.excel.config.ExcelConfigProperties;
import com.hccake.common.excel.kit.ExcelException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.ObjectProvider;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author lengleng
 * @date 2020/3/29
 * <p>
 * 处理单sheet 页面
 */
@RequiredArgsConstructor
public class SingleSheetWriteHandler extends AbstractSheetWriteHandler {

	private final ExcelConfigProperties configProperties;

	private final ObjectProvider<List<Converter<?>>> converterProvider;

	/**
	 * obj 是List 且list不为空同时list中的元素不是是List 才返回true
	 * @param obj 返回对象
	 * @return
	 */
	@Override
	public boolean support(Object obj) {
		if (obj instanceof List) {
			List objList = (List) obj;
			return !objList.isEmpty() && !(objList.get(0) instanceof List);
		}
		else {
			throw new ExcelException("@ResponseExcel 返回值必须为List类型");
		}
	}

	@Override
	@SneakyThrows
	public void write(Object obj, HttpServletResponse response, ResponseExcel responseExcel) {
		List list = (List) obj;
		ExcelWriter excelWriter = getExcelWriter(response, responseExcel, configProperties.getTemplatePath());

		// 有模板则不指定sheet名
		Class<?> dataClass = list.get(0).getClass();
		WriteSheet sheet = this.sheet(null, responseExcel.sheet()[0], dataClass, responseExcel.template(),
				responseExcel.headGenerator());
		excelWriter.write(list, sheet);
		excelWriter.finish();
	}

	@Override
	public void registerCustomConverter(ExcelWriterBuilder builder) {
		converterProvider.ifAvailable(converters -> converters.forEach(builder::registerConverter));
	}

}
