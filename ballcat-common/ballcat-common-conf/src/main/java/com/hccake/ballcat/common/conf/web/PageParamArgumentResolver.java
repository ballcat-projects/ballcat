package com.hccake.ballcat.common.conf.web;

import cn.hutool.core.util.StrUtil;
import com.hccake.ballcat.common.core.domain.PageParam;
import com.hccake.ballcat.common.core.exception.SqlCheckedException;
import com.hccake.ballcat.common.core.result.BaseResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lengleng
 * @author hccake
 * @date 2019-06-24
 * <p>
 * 解决Mybatis Plus Order By SQL注入问题
 */
@Slf4j
public class PageParamArgumentResolver implements HandlerMethodArgumentResolver {

	private final static String[] KEYWORDS = { "master", "truncate", "insert", "select", "delete", "update", "declare",
			"alter", "drop", "sleep" };

	/**
	 * 判断Controller是否包含page 参数
	 * @param parameter 参数
	 * @return 是否过滤
	 */
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType().equals(PageParam.class);
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
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

		String current = request.getParameter("current");
		String size = request.getParameter("size");
		String sortField = request.getParameter("sortField");
		String sortAsc = request.getParameter("sortAsc");

		PageParam pageParam = new PageParam();
		if (StrUtil.isNotBlank(current)) {
			pageParam.setCurrent(Long.parseLong(current));
		}
		if (StrUtil.isNotBlank(size)) {
			pageParam.setSize(Long.parseLong(size));
		}

		if (StrUtil.isNotEmpty(sortField)) {
			// 校验参数
			sqlInject(sortField);
			// 驼峰转下划线
			sortField = StrUtil.toUnderlineCase(sortField);
			// 正序/倒序
			boolean isAsc = (StrUtil.isNotBlank(sortAsc) && Boolean.parseBoolean(sortAsc));
			pageParam.setSortAsc(isAsc);
			pageParam.setSortField(sortField);
		}

		return pageParam;
	}

	/**
	 * SQL注入过滤
	 * @param str 待验证的字符串
	 */
	public static void sqlInject(String str) {
		if (StrUtil.isEmpty(str)) {
			return;
		}
		// 转换成小写
		String inStr = str.toLowerCase();

		// 判断是否包含非法字符
		for (String keyword : KEYWORDS) {
			if (inStr.contains(keyword)) {
				log.error("查询包含非法字符 {}", keyword);
				throw new SqlCheckedException(BaseResultCode.MALICIOUS_REQUEST.getCode(), "恶意请求参数：" + keyword);
			}
		}
	}

}
