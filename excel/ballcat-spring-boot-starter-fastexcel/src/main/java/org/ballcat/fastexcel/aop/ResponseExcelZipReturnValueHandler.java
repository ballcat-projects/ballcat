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

import java.io.IOException;
import java.util.List;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.ballcat.fastexcel.annotation.ResponseExcel;
import org.ballcat.fastexcel.annotation.ResponseExcelZip;
import org.ballcat.fastexcel.context.ExcelContextHolder;
import org.ballcat.fastexcel.handler.SheetWriteHandler;
import org.springframework.core.MethodParameter;

/**
 * 处理 @ResponseExcelZip 返回值
 *
 * @author xm.z
 */
@Slf4j
public class ResponseExcelZipReturnValueHandler extends AbstractExcelReturnValueHandler<ResponseExcelZip> {

	public ResponseExcelZipReturnValueHandler(List<SheetWriteHandler> sheetWriteHandlerList) {
		super(sheetWriteHandlerList);
	}

	@Override
	protected Class<ResponseExcelZip> getAnnotationClass() {
		return ResponseExcelZip.class;
	}

	@Override
	protected String getFileName(ResponseExcelZip annotation) {
		return ExcelContextHolder.getZipFileName();
	}

	@Override
	protected void doWrite(Object data, ResponseExcelZip annotation, HttpServletResponse response) throws IOException {
		List<?> dataList = (List<?>) data;
		try (ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream())) {
			for (int i = 0; i < annotation.value().length; i++) {
				ResponseExcel config = annotation.value()[i];
				Object subData = dataList.isEmpty() ? null : dataList.get(i);

				SheetWriteHandler handler = findSuitableWriteHandler(subData, config);
				handler.check(config);

				handler.write(subData, zipOut, config, getExportInfo(config.name()));
				zipOut.closeEntry();
			}
			zipOut.flush();
		}
	}

	@Override
	public boolean supportsReturnType(MethodParameter parameter) {
		return super.supportsReturnType(parameter) && isNestedList(parameter);
	}

}
