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

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Validator;

import cn.idev.excel.FastExcel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.ballcat.fastexcel.annotation.RequestExcel;
import org.ballcat.fastexcel.handler.ListAnalysisEventListener;
import org.springframework.beans.BeanUtils;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.ResolvableType;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

/**
 * 上传excel 解析注解
 *
 * @author lengleng
 * @author L.cm 2021/4/16
 */
@Slf4j
public class RequestExcelArgumentResolver implements HandlerMethodArgumentResolver {

	private final ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

	private final ExpressionParser expressionParser = new SpelExpressionParser();

	private final Validator validator;

	public RequestExcelArgumentResolver(Validator validator) {
		this.validator = validator;
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(RequestExcel.class);
	}

	@SneakyThrows(Exception.class)
	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer modelAndViewContainer,
			NativeWebRequest webRequest, WebDataBinderFactory webDataBinderFactory) {
		Class<?> parameterType = parameter.getParameterType();
		if (!parameterType.isAssignableFrom(List.class)) {
			throw new IllegalArgumentException(
					"Excel upload request resolver error, @RequestExcel parameter is not List " + parameterType);
		}

		// 处理自定义 readListener
		RequestExcel requestExcel = parameter.getParameterAnnotation(RequestExcel.class);
		assert requestExcel != null;
		Class<? extends ListAnalysisEventListener<?>> readListenerClass = requestExcel.readListener();
		ListAnalysisEventListener<?> readListener = BeanUtils.instantiateClass(readListenerClass);
		readListener.setValidator(this.validator);

		// 获取请求文件流
		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
		assert request != null;
		InputStream inputStream;
		if (request instanceof MultipartRequest) {
			MultipartFile file = ((MultipartRequest) request).getFile(requestExcel.fileName());
			Assert.notNull(file, "excel import: file can not be null!");
			inputStream = file.getInputStream();
		}
		else {
			inputStream = request.getInputStream();
		}

		// 解析工作表名称
		String sheetName = resolverSheetName(request, parameter.getMethod(), requestExcel.sheetName());

		// 获取目标类型
		Class<?> excelModelClass = ResolvableType.forMethodParameter(parameter).getGeneric(0).resolve();

		// 这里需要指定读用哪个 class 去读，然后读取第一个 sheet 文件流会自动关闭
		FastExcel.read(inputStream, excelModelClass, readListener)
			.numRows(requestExcel.numRows() > 0 ? requestExcel.numRows() : null)
			.ignoreEmptyRow(requestExcel.ignoreEmptyRow())
			.sheet(sheetName)
			.headRowNumber(requestExcel.headRowNumber())
			.doRead();

		// 校验失败的数据处理 交给 BindResult
		WebDataBinder dataBinder = webDataBinderFactory.createBinder(webRequest, readListener.getErrors(), "excel");
		ModelMap model = modelAndViewContainer.getModel();
		model.put(BindingResult.MODEL_KEY_PREFIX + "excel", dataBinder.getBindingResult());

		return readListener.getList();
	}

	/**
	 * 解析Sheet名称
	 * @param request HttpServletRequest对象，用于获取请求参数
	 * @param method 调用的方法对象，用于获取方法参数名称
	 * @param sheetName 需要解析的Sheet名称
	 * @return 解析后的Sheet名称，如果为空则返回null
	 */
	public String resolverSheetName(HttpServletRequest request, Method method, String sheetName) {
		if (!StringUtils.hasText(sheetName)) {
			return null;
		}

		if (!sheetName.contains("#")) {
			return sheetName;
		}
		String[] parameterNames = this.parameterNameDiscoverer.getParameterNames(method);
		StandardEvaluationContext evaluationContext = new StandardEvaluationContext();
		if (parameterNames != null) {
			for (String name : parameterNames) {
				evaluationContext.setVariable(name, request.getParameter(name));
			}
		}
		Expression expression = this.expressionParser.parseExpression(sheetName);
		String value = expression.getValue(evaluationContext, String.class);
		return value == null || value.isEmpty() ? null : value;
	}

}
