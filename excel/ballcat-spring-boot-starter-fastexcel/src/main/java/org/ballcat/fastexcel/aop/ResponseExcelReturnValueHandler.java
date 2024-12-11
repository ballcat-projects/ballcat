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

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ballcat.fastexcel.annotation.ResponseExcel;
import org.ballcat.fastexcel.handler.SheetWriteHandler;
import org.springframework.core.MethodParameter;
import org.springframework.util.Assert;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 处理@ResponseExcel 返回值
 *
 * @author lengleng
 */
@Slf4j
@RequiredArgsConstructor
public class ResponseExcelReturnValueHandler implements HandlerMethodReturnValueHandler {

	private final List<SheetWriteHandler> sheetWriteHandlerList;

	/**
	 * 只处理@ResponseExcel 声明的方法
	 * @param parameter 方法签名
	 * @return 是否处理
	 */
	@Override
	public boolean supportsReturnType(MethodParameter parameter) {
		return parameter.getMethodAnnotation(ResponseExcel.class) != null;
	}

	/**
	 * 处理逻辑
	 * @param o 返回参数
	 * @param parameter 方法签名
	 * @param mavContainer 上下文容器
	 * @param nativeWebRequest 上下文
	 */
	@Override
	public void handleReturnValue(Object o, MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest nativeWebRequest) {
		/* check */
		HttpServletResponse response = nativeWebRequest.getNativeResponse(HttpServletResponse.class);
		Assert.state(response != null, "No HttpServletResponse");
		ResponseExcel responseExcel = parameter.getMethodAnnotation(ResponseExcel.class);
		Assert.state(responseExcel != null, "No @ResponseExcel");
		mavContainer.setRequestHandled(true);

		this.sheetWriteHandlerList.stream()
			.filter(handler -> handler.support(o))
			.findFirst()
			.ifPresent(handler -> handler.export(o, response, responseExcel));
	}

}
