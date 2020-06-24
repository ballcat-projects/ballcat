package com.hccake.common.excel.handler;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.handler.WriteHandler;
import com.hccake.common.excel.annotation.ResponseExcel;
import com.hccake.common.excel.kit.ExcelException;
import com.hccake.common.excel.kit.ExcelNameContextHolder;
import lombok.SneakyThrows;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

/**
 * @author lengleng
 * @date 2020/3/31
 */
public abstract class AbstractSheetWriteHandler implements SheetWriteHandler {

	@Override
	public void check(ResponseExcel responseExcel) {
		if (!StringUtils.hasText(responseExcel.name())) {
			throw new ExcelException("@ResponseExcel name 配置不合法");
		}

		if (responseExcel.sheet().length == 0) {
			throw new ExcelException("@ResponseExcel sheet 配置不合法");
		}
	}

	@Override
	@SneakyThrows
	public void export(Object o, HttpServletResponse response, ResponseExcel responseExcel) {
		if (support(o)) {
			check(responseExcel);
			String name = ExcelNameContextHolder.get();
			String fileName = String.format("%s%s", URLEncoder.encode(name, "UTF-8"),
					responseExcel.suffix().getValue());
			response.setContentType("application/vnd.ms-excel");
			response.setCharacterEncoding("utf-8");
			response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName);
			write(o, response, responseExcel);
		}
	}

	/**
	 * 通用的获取ExcelWriter方法
	 * @param response HttpServletResponse
	 * @param responseExcel ResponseExcel注解
	 * @param list Excel数据
	 * @param templatePath 模板地址
	 * @return ExcelWriter
	 */
	@SneakyThrows
	public ExcelWriter getExcelWriter(HttpServletResponse response, ResponseExcel responseExcel, List list,
			String templatePath) {
		ExcelWriterBuilder writerBuilder = EasyExcel.write(response.getOutputStream(), list.get(0).getClass())
				.autoCloseStream(true).excelType(responseExcel.suffix()).inMemory(responseExcel.inMemory());

		if (StringUtils.hasText(responseExcel.password())) {
			writerBuilder.password(responseExcel.password());
		}

		if (responseExcel.include().length != 0) {
			writerBuilder.includeColumnFiledNames(Arrays.asList(responseExcel.include()));
		}

		if (responseExcel.exclude().length != 0) {
			writerBuilder.excludeColumnFiledNames(Arrays.asList(responseExcel.include()));
		}

		if (responseExcel.writeHandler().length != 0) {
			for (Class<? extends WriteHandler> clazz : responseExcel.writeHandler()) {
				writerBuilder.registerWriteHandler(clazz.newInstance());
			}
		}

		if (responseExcel.converter().length != 0) {
			for (Class<? extends Converter> clazz : responseExcel.converter()) {
				writerBuilder.registerConverter(clazz.newInstance());
			}
		}

		if (StringUtils.hasText(responseExcel.template())) {
			ClassPathResource classPathResource = new ClassPathResource(
					templatePath + File.separator + responseExcel.template());
			InputStream inputStream = classPathResource.getInputStream();
			writerBuilder.withTemplate(inputStream);
		}

		return writerBuilder.build();
	}

}
