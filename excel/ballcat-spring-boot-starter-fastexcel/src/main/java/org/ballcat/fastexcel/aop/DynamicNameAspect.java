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

package org.ballcat.fastexcel.aop;

import java.time.LocalDateTime;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.ballcat.fastexcel.annotation.ResponseExcel;
import org.ballcat.fastexcel.annotation.ResponseExcelZip;
import org.ballcat.fastexcel.context.ExcelContextHolder;
import org.ballcat.fastexcel.context.ExcelExportInfo;
import org.ballcat.fastexcel.processor.NameProcessor;
import org.springframework.util.StringUtils;

/**
 * @author lengleng 2020/3/29
 */
@Aspect
@RequiredArgsConstructor
public class DynamicNameAspect {

	private final NameProcessor processor;

	/**
	 * 单个 Excel 导出时，设置文件名与模板路径到上下文
	 */
	@Before("@annotation(excel)")
	public void beforeExcel(JoinPoint point, ResponseExcel excel) {
		MethodSignature ms = (MethodSignature) point.getSignature();
		handleSingleExcel(point, ms, excel);
	}

	/**
	 * ZIP 多文件导出时，设置 ZIP 文件名及各个 Excel 文件信息
	 */
	@Before("@annotation(zipExcel)")
	public void beforeZip(JoinPoint point, ResponseExcelZip zipExcel) {
		MethodSignature ms = (MethodSignature) point.getSignature();
		ResponseExcel[] excels = zipExcel.value();

		// 清空旧数据
		ExcelContextHolder.clearExcelContext();

		// 设置每个 Excel 文件信息
		for (ResponseExcel excel : excels) {
			handleSingleExcel(point, ms, excel);
		}

		// 设置 ZIP 文件名
		String zipFileName = determineName(point, ms, zipExcel.name());
		ExcelContextHolder.setZipFileName(zipFileName);
	}

	/**
	 * 将单个 Excel 的配置解析并放入上下文
	 */
	private void handleSingleExcel(JoinPoint point, MethodSignature ms, ResponseExcel excel) {
		String name = determineName(point, ms, excel.name());
		String fileName = name + excel.suffix().getValue();
		String template = determineTemplate(point, ms, excel.template());

		ExcelContextHolder.putExcelExportInfo(excel.name(), ExcelExportInfo.of(fileName, template));
	}

	/**
	 * 解析名称字段（支持 SpEL）
	 */
	private String determineName(JoinPoint point, MethodSignature ms, String expression) {
		return StringUtils.hasText(expression)
				? this.processor.doDetermineName(point.getArgs(), ms.getMethod(), expression)
				: LocalDateTime.now().toString();
	}

	/**
	 * 解析模板字段（支持 SpEL）
	 */
	private String determineTemplate(JoinPoint point, MethodSignature ms, String expression) {
		if (StringUtils.hasText(expression)) {
			return this.processor.doDetermineName(point.getArgs(), ms.getMethod(), expression);
		}
		return null;
	}

}
