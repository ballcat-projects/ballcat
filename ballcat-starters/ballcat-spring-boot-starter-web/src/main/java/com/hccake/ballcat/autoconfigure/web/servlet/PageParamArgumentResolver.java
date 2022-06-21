package com.hccake.ballcat.autoconfigure.web.servlet;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageParamRequest;
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
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author lengleng
 * @author hccake
 * @date 2019-06-24
 * <p>
 * 解决Mybatis Plus Order By SQL注入问题
 */
@Slf4j
public class PageParamArgumentResolver implements HandlerMethodArgumentResolver {

	private static final Set<String> SQL_KEYWORDS = CollectionUtil.newHashSet("master", "truncate", "insert", "select",
			"delete", "update", "declare", "alter", "drop", "sleep");

	private static final String ASC = "asc";

	private final int pageSizeLimit;

	public PageParamArgumentResolver() {
		this(0);
	}

	public PageParamArgumentResolver(int pageSizeLimit) {
		this.pageSizeLimit = pageSizeLimit;
	}

	/**
	 * 判断Controller是否包含page 参数
	 * @param parameter 参数
	 * @return 是否过滤
	 */
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return PageParam.class.isAssignableFrom(parameter.getParameterType());
	}

	/**
	 * @param parameter 入参集合
	 * @param mavContainer model 和 view
	 * @param webRequest web相关
	 * @param binderFactory 入参解析
	 * @return 检查后新的page对象
	 * <p>
	 * page 只支持查询 GET .如需解析POST获取请求报文体处理
	 */
	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
		if (request == null) {
			return null;
		}

		String current = request.getParameter("current");
		String size = request.getParameter("size");
		Map<String, String[]> parameterMap = request.getParameterMap();

		// sort 同时支持 sort 和 sort[]
		String[] sort = parameterMap.get("sort");
		if (ArrayUtil.isEmpty(sort)) {
			sort = parameterMap.get("sort[]");
		}

		PageParam pageParam;
		try {
			pageParam = (PageParam) parameter.getParameterType().newInstance();
		}
		catch (InstantiationException | IllegalAccessException e) {
			pageParam = new PageParam();
		}

		if (StrUtil.isNotBlank(current)) {
			pageParam.setCurrent(Long.parseLong(current));
		}
		if (StrUtil.isNotBlank(size)) {
			pageParam.setSize(Long.parseLong(size));
		}

		List<PageParam.Sort> sorts;
		if (ArrayUtil.isNotEmpty(sort)) {
			sorts = getSortList(sort);
		}
		else {
			String sortFields = request.getParameter("sortFields");
			String sortOrders = request.getParameter("sortOrders");
			sorts = getSortList(sortFields, sortOrders);
		}
		pageParam.setSorts(sorts);

		paramValidate(parameter, mavContainer, webRequest, binderFactory, pageParam);

		return pageParam;
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

			// 切割后必须是两位， a:b 的规则
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
	protected List<PageParam.Sort> getSortList(String sortFields, String sortOrders) {
		List<PageParam.Sort> sorts = new ArrayList<>();

		// 字段和规则都不能为空
		if (StrUtil.isBlank(sortFields) || StrUtil.isBlank(sortOrders)) {
			return sorts;
		}

		// 字段和规则不一一对应则不处理
		String[] fieldArr = sortFields.split(StrUtil.COMMA);
		String[] orderArr = sortOrders.split(StrUtil.COMMA);
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
			sort.setField(StrUtil.toUnderlineCase(field));
			sorts.add(sort);
		}
	}

	/**
	 * 判断排序字段名是否非法 字段名只允许数字字母下划线，且不能是 sql 关键字
	 * @param filedName 字段名
	 * @return 是否非法
	 */
	public boolean validFieldName(String filedName) {
		boolean isValid = StrUtil.isNotBlank(filedName) && filedName.matches(PageParamRequest.SORT_FILED_REGEX)
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
			if (size > pageSizeLimit) {
				bindingResult.addError(new ObjectError("size", "分页条数不能大于" + pageSizeLimit));
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
