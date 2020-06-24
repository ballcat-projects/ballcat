package com.hccake.common.excel.handler;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.hccake.common.excel.annotation.ResponseExcel;
import com.hccake.common.excel.config.ExcelConfigProperties;
import com.hccake.common.excel.kit.ExcelException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author lengleng
 * @date 2020/3/29
 * <p>
 * 处理单sheet 页面
 */
@Component
@RequiredArgsConstructor
public class SingleSheetWriteHandler extends AbstractSheetWriteHandler {

	private final ExcelConfigProperties configProperties;

	/**
	 * obj 是List 且元素不是是List 才返回true
	 * @param obj 返回对象
	 * @return
	 */
	@Override
	public boolean support(Object obj) {
		if (obj instanceof List) {
			List objList = (List) obj;
			return !(objList.get(0) instanceof List);
		}
		else {
			throw new ExcelException("@ResponseExcel 返回值必须为List类型");
		}
	}

	@Override
	@SneakyThrows
	public void write(Object obj, HttpServletResponse response, ResponseExcel responseExcel) {
		List list = (List) obj;

		ExcelWriter excelWriter = getExcelWriter(response, responseExcel, list, configProperties.getTemplatePath());
		// 有模板则不指定sheet名
		WriteSheet sheet = StringUtils.hasText(responseExcel.template()) ? EasyExcel.writerSheet().build()
				: EasyExcel.writerSheet(responseExcel.sheet()[0]).build();

		excelWriter.write(list, sheet);
		excelWriter.finish();
	}

}
