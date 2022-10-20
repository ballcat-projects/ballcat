package com.hccake.ballcat.autoconfigure.web.pageable;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.ArrayUtil;
import com.hccake.ballcat.common.model.domain.PageParam;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.ValidationAnnotationUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.hccake.ballcat.common.model.domain.PageableConstants.*;

/**
 * @author hccake
 */
@Slf4j
public abstract class PageParamArgumentResolverSupport {

	@Setter
	@Getter
	private String pageParameterName = DEFAULT_PAGE_PARAMETER;

	@Setter
	@Getter
	private String sizeParameterName = DEFAULT_SIZE_PARAMETER;

	@Setter
	@Getter
	private String sortParameterName = DEFAULT_SORT_PARAMETER;

	@Setter
	@Getter
	private int maxPageSize = DEFAULT_MAX_PAGE_SIZE;

	protected PageParam getPageParam(MethodParameter parameter, HttpServletRequest request) {
		String pageParameterValue = request.getParameter(pageParameterName);
		String sizeParameterValue = request.getParameter(sizeParameterName);

		PageParam pageParam;
		try {
			pageParam = (PageParam) parameter.getParameterType().newInstance();
		}
		catch (InstantiationException | IllegalAccessException e) {
			pageParam = new PageParam();
		}

		long pageValue = parseValueFormString(pageParameterValue, 1);
		pageParam.setPage(pageValue);
		pageParam.setCurrent(pageValue);

		long sizeValue = parseValueFormString(sizeParameterValue, 10);
		pageParam.setSize(sizeValue);

		// ========== 排序处理 ===========
		Map<String, String[]> parameterMap = request.getParameterMap();
		// sort 可以传多个，所以同时支持 sort 和 sort[]
		String[] sort = parameterMap.get(sortParameterName);
		if (ArrayUtil.isEmpty(sort)) {
			sort = parameterMap.get(sortParameterName + "[]");
		}

		List<PageParam.Sort> sorts;
		if (ArrayUtil.isNotEmpty(sort)) {
			sorts = getSortList(sort);
		}
		else {
			String sortFields = request.getParameter(SORT_FIELDS);
			String sortOrders = request.getParameter(SORT_ORDERS);
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
				order = ASC;
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
		if (CharSequenceUtil.isBlank(sortFields) || CharSequenceUtil.isBlank(sortOrders)) {
			return sorts;
		}

		// 字段和规则不一一对应则不处理
		String[] fieldArr = sortFields.split(StrPool.COMMA);
		String[] orderArr = sortOrders.split(StrPool.COMMA);
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
			sort.setAsc(ASC.equalsIgnoreCase(order));
			// 正序/倒序
			sort.setField(CharSequenceUtil.toUnderlineCase(field));
			sorts.add(sort);
		}
	}

	/**
	 * 判断排序字段名是否非法 字段名只允许数字字母下划线，且不能是 sql 关键字
	 * @param filedName 字段名
	 * @return 是否非法
	 */
	protected boolean validFieldName(String filedName) {
		boolean isValid = CharSequenceUtil.isNotBlank(filedName) && filedName.matches(SORT_FILED_REGEX)
				&& !SQL_KEYWORDS.contains(filedName);
		if (!isValid) {
			log.warn("异常的分页查询排序字段：{}", filedName);
		}
		return isValid;
	}

	protected void paramValidate(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory, PageParam pageParam) throws Exception {
		// 数据校验处理
		if (binderFactory != null) {
			WebDataBinder binder = binderFactory.createBinder(webRequest, pageParam, "pageParam");
			validateIfApplicable(binder, parameter);
			BindingResult bindingResult = binder.getBindingResult();

			long size = pageParam.getSize();
			if (size > maxPageSize) {
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
