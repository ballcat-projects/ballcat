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

package org.ballcat.web.pageable;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.ballcat.common.constant.Symbol;
import org.ballcat.common.model.domain.PageParam;
import org.ballcat.common.model.domain.PageableConstants;
import org.springframework.core.MethodParameter;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.ValidationAnnotationUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @author hccake
 */
@Slf4j
public abstract class PageParamArgumentResolverSupport {

	@Setter
	@Getter
	private PageableRequestOptions defaultPageableRequestOptions = new PageableRequestOptions();

	protected PageParam getPageParam(MethodParameter parameter, HttpServletRequest request,
			PageableRequestOptions pageableRequestOptions) {

		PageParam pageParam;
		try {
			pageParam = (PageParam) parameter.getParameterType().newInstance();
		}
		catch (InstantiationException | IllegalAccessException e) {
			pageParam = new PageParam();
		}

		String pageParameterValue = request.getParameter(pageableRequestOptions.getPageParameterName());
		long pageValue = parseValueFormString(pageParameterValue, 1);
		pageParam.setPage(pageValue);
		pageParam.setCurrent(pageValue);

		String sizeParameterValue = request.getParameter(pageableRequestOptions.getSizeParameterName());
		long sizeValue = parseValueFormString(sizeParameterValue, 10);
		pageParam.setSize(sizeValue);

		// ========== 排序处理 ===========
		Map<String, String[]> parameterMap = request.getParameterMap();
		// sort 可以传多个，所以同时支持 sort 和 sort[]
		String[] sort = parameterMap.get(pageableRequestOptions.getSortParameterName());
		if (ObjectUtils.isEmpty(sort)) {
			sort = parameterMap.get(pageableRequestOptions.getSortParameterName() + "[]");
		}

		List<PageParam.Sort> sorts;
		if (!ObjectUtils.isEmpty(sort)) {
			sorts = getSortList(sort);
		}
		else {
			String sortFields = request.getParameter(PageableConstants.SORT_FIELDS);
			String sortOrders = request.getParameter(PageableConstants.SORT_ORDERS);
			sorts = getSortList(sortFields, sortOrders);
		}
		pageParam.setSorts(sorts);

		return pageParam;
	}

	private long parseValueFormString(String currentParameterValue, long defaultValue) {
		try {
			return Long.parseLong(currentParameterValue);
		}
		catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	/**
	 * 封装排序规则
	 * @param sort 排序规则字符串
	 * @return List<PageParam.Sort>
	 */
	protected List<PageParam.Sort> getSortList(String[] sort) {
		List<PageParam.Sort> sorts = new ArrayList<>();

		// 将排序规则转换为 Sort 对象
		for (String sortRule : sort) {
			if (sortRule == null) {
				continue;
			}

			// 切割后最多两位，第一位 field 第二位 order
			String[] sortRuleArr = sortRule.split(",");

			// 字段
			String field = sortRuleArr[0];

			// 排序规则，默认正序
			String order;
			if (sortRuleArr.length < 2) {
				order = PageableConstants.ASC;
			}
			else {
				order = sortRuleArr[1];
			}

			fillValidSort(field, order, sorts);
		}

		return sorts;
	}

	/**
	 * 封装排序规则
	 * @param sortFields 排序字段，使用英文逗号分割
	 * @param sortOrders 排序规则，使用英文逗号分割，与排序字段一一对应
	 * @return List<PageParam.OrderItem>
	 */
	@Deprecated
	protected List<PageParam.Sort> getSortList(String sortFields, String sortOrders) {
		List<PageParam.Sort> sorts = new ArrayList<>();

		// 字段和规则都不能为空
		if (!StringUtils.hasText(sortFields) || !StringUtils.hasText(sortOrders)) {
			return sorts;
		}

		// 字段和规则不一一对应则不处理
		String[] fieldArr = sortFields.split(Symbol.COMMA);
		String[] orderArr = sortOrders.split(Symbol.COMMA);
		if (fieldArr.length != orderArr.length) {
			return sorts;
		}

		for (int i = 0; i < fieldArr.length; i++) {
			String field = fieldArr[i];
			String order = orderArr[i];
			fillValidSort(field, order, sorts);
		}

		return sorts;
	}

	/**
	 * 校验并填充有效的 sort 对象到指定集合忠
	 * @param field 排序列
	 * @param order 排序顺序
	 * @param sorts sorts 集合
	 */
	protected void fillValidSort(String field, String order, List<PageParam.Sort> sorts) {
		if (validFieldName(field)) {
			PageParam.Sort sort = new PageParam.Sort();
			// 驼峰转下划线
			sort.setAsc(PageableConstants.ASC.equalsIgnoreCase(order));
			// 正序/倒序
			sort.setField(org.ballcat.common.util.StringUtils.toUnderlineCase(field));
			sorts.add(sort);
		}
	}

	/**
	 * 判断排序字段名是否非法 字段名只允许数字字母下划线，且不能是 sql 关键字
	 * @param filedName 字段名
	 * @return 是否非法
	 */
	protected boolean validFieldName(String filedName) {
		boolean isValid = StringUtils.hasText(filedName) && filedName.matches(PageableConstants.SORT_FILED_REGEX)
				&& !PageableConstants.SQL_KEYWORDS.contains(filedName);
		if (!isValid) {
			log.warn("异常的分页查询排序字段：{}", filedName);
		}
		return isValid;
	}

	protected void paramValidate(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory, PageParam pageParam,
			PageableRequestOptions pageableRequestOptions) throws Exception {
		// 数据校验处理
		if (binderFactory != null) {
			WebDataBinder binder = binderFactory.createBinder(webRequest, pageParam, "pageParam");
			validateIfApplicable(binder, parameter);
			BindingResult bindingResult = binder.getBindingResult();

			long size = pageParam.getSize();
			int maxPageSize = pageableRequestOptions.getMaxPageSize();
			if (maxPageSize > 0 && size > maxPageSize) {
				bindingResult.addError(new ObjectError("size", "分页条数不能大于" + maxPageSize));
			}

			if (bindingResult.hasErrors() && isBindExceptionRequired(binder, parameter)) {
				throw new MethodArgumentNotValidException(parameter, bindingResult);
			}
			if (mavContainer != null) {
				mavContainer.addAttribute(BindingResult.MODEL_KEY_PREFIX + "pageParam", bindingResult);
			}
		}
	}

	protected void validateIfApplicable(WebDataBinder binder, MethodParameter parameter) {
		Annotation[] annotations = parameter.getParameterAnnotations();
		for (Annotation ann : annotations) {
			Object[] validationHints = ValidationAnnotationUtils.determineValidationHints(ann);
			if (validationHints != null) {
				binder.validate(validationHints);
				break;
			}
		}
	}

	protected boolean isBindExceptionRequired(WebDataBinder binder, MethodParameter parameter) {
		int i = parameter.getParameterIndex();
		Class<?>[] paramTypes = parameter.getExecutable().getParameterTypes();
		boolean hasBindingResult = (paramTypes.length > (i + 1) && Errors.class.isAssignableFrom(paramTypes[i + 1]));
		return !hasBindingResult;
	}

}
