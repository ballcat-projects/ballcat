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

import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.ballcat.fastexcel.annotation.ResponseExcel;
import org.ballcat.fastexcel.handler.SheetWriteHandler;

/**
 * 处理 @ResponseExcel 返回值
 *
 * @author xm.z
 */
@Slf4j
public class ResponseExcelReturnValueHandler extends AbstractExcelReturnValueHandler<ResponseExcel> {

	public ResponseExcelReturnValueHandler(List<SheetWriteHandler> sheetWriteHandlerList) {
		super(sheetWriteHandlerList);
	}

	@Override
	protected Class<ResponseExcel> getAnnotationClass() {
		return ResponseExcel.class;
	}

	@Override
	protected String getFileName(ResponseExcel annotation) {
		return getExportInfo(annotation.name()).getFileName();
	}

	@Override
	protected void doWrite(Object data, ResponseExcel annotation, HttpServletResponse response) throws IOException {
		SheetWriteHandler handler = this.findSuitableWriteHandler(data, annotation);
		handler.check(annotation);
		handler.export(data, response.getOutputStream(), annotation, getExportInfo(annotation.name()));
	}

}
