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

import java.io.IOException;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletOutputStream;

import cn.idev.excel.ExcelWriter;
import org.ballcat.fastexcel.annotation.ResponseExcel;
import org.ballcat.fastexcel.context.ExcelExportInfo;

/**
 * sheet 写出处理器
 *
 * @author lengleng 2020/3/29
 */
public interface SheetWriteHandler {

	/**
	 * 是否支持
	 * @param resultObject 返回对象
	 * @param responseExcel 导出注解
	 * @return boolean
	 */
	boolean support(Object resultObject, ResponseExcel responseExcel);

	/**
	 * 校验
	 * @param responseExcel 导出注解
	 */
	void check(ResponseExcel responseExcel);

	/**
	 * 导出文件
	 * @param resultObject 返回对象
	 * @param outputStream 输出对象
	 * @param responseExcel 导出注解
	 * @param excelExportInfo 导出信息
	 */
	void export(Object resultObject, ServletOutputStream outputStream, ResponseExcel responseExcel,
			ExcelExportInfo excelExportInfo);

	/**
	 * 导出文件
	 * @param resultObject 返回对象
	 * @param outputStream 输出对象
	 * @param responseExcel 导出注解
	 * @param excelExportInfo 导出信息
	 * @throws IOException 写入过程中发生错误
	 */
	void export(Object resultObject, ZipOutputStream outputStream, ResponseExcel responseExcel,
			ExcelExportInfo excelExportInfo) throws IOException;

	/**
	 * 将数据写入Excel文件
	 * @param resultObject 返回对象
	 * @param responseExcel 导出注解
	 * @param excelWriter 写入工具
	 * @param excelExportInfo 导出信息
	 */
	void write(Object resultObject, ResponseExcel responseExcel, ExcelWriter excelWriter,
			ExcelExportInfo excelExportInfo);

}
