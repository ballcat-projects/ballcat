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
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ballcat.fastexcel.annotation.ResponseExcel;
import org.ballcat.fastexcel.context.ExcelContextHolder;
import org.ballcat.fastexcel.context.ExcelExportInfo;
import org.ballcat.fastexcel.handler.SheetWriteHandler;
import org.ballcat.fastexcel.util.FastExcelUtils;
import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 抽象基类，支持处理不同注解类型的Excel返回值
 *
 * @param <A> 注解类型（ResponseExcel 或 ResponseExcelZip）
 * @author xm.z
 */
@Slf4j
@RequiredArgsConstructor
public abstract class AbstractExcelReturnValueHandler<A extends Annotation> implements HandlerMethodReturnValueHandler {

	private final List<SheetWriteHandler> sheetWriteHandlerList;

	@Override
	public boolean supportsReturnType(MethodParameter parameter) {
		return parameter.hasMethodAnnotation(getAnnotationClass());
	}

	@Override
	public void handleReturnValue(Object returnValue, MethodParameter parameter, ModelAndViewContainer mavContainer,
			@NonNull NativeWebRequest nativeWebRequest) throws IOException {
		// 获取响应对象
		HttpServletResponse response = getResponse(nativeWebRequest);
		// 获取注解
		A annotation = parameter.getMethodAnnotation(getAnnotationClass());
		Assert.state(annotation != null, "No annotation found: " + getAnnotationClass().getName());

		mavContainer.setRequestHandled(true);

		// 设置响应头
		String fileName = getFileName(annotation);
		FastExcelUtils.setResponseHeader(response, fileName);

		// 核心写入逻辑
		doWrite(returnValue, annotation, response);
	}

	/**
	 * 获取当前处理器支持的注解类型
	 */
	protected abstract Class<A> getAnnotationClass();

	/**
	 * 获取文件名
	 */
	protected abstract String getFileName(A annotation);

	/**
	 * 执行实际的Excel写入操作
	 */
	protected abstract void doWrite(Object data, A annotation, HttpServletResponse response) throws IOException;

	/**
	 * 获取HttpServletResponse
	 */
	protected HttpServletResponse getResponse(NativeWebRequest nativeWebRequest) {
		HttpServletResponse response = nativeWebRequest.getNativeResponse(HttpServletResponse.class);
		Assert.state(response != null, "No HttpServletResponse");
		return response;
	}

	/**
	 * 查找合适的SheetWriteHandler
	 */
	protected SheetWriteHandler findSuitableWriteHandler(Object data, ResponseExcel config) {
		return this.sheetWriteHandlerList.stream()
			.filter(handler -> handler.support(data, config))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("No suitable SheetWriteHandler found for data: " + data));
	}

	/**
	 * 获取ExcelExportInfo
	 */
	protected ExcelExportInfo getExportInfo(String name) {
		return ExcelContextHolder.getExcelExportInfo(name);
	}

	/**
	 * 判断是否为嵌套List结构（List<List<?>>）
	 */
	public static boolean isNestedList(MethodParameter parameter) {
		if (!List.class.isAssignableFrom(parameter.getParameterType())) {
			return false;
		}

		Type genericType = parameter.getGenericParameterType();
		if (!(genericType instanceof ParameterizedType)) {
			return true;
		}

		ParameterizedType pt = (ParameterizedType) genericType;
		Type[] args = pt.getActualTypeArguments();
		if (args.length != 1) {
			return false;
		}

		Type innerType = args[0];
		if (innerType instanceof ParameterizedType) {
			return List.class.isAssignableFrom((Class<?>) ((ParameterizedType) innerType).getRawType());
		}

		if (innerType instanceof Class) {
			return List.class.isAssignableFrom((Class<?>) innerType);
		}

		return false;
	}

}
