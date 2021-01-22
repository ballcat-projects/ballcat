package com.hccake.ballcat.common.conf.web;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hccake.ballcat.common.core.domain.PageParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 为了兼容处理，当用户直接使用 IPage 类型作为分页参数接收时的 sql 防注入处理 <br/>
 * 现在推荐使用 PageParam 接收分页参数，使用 PageParamArgumentResolver 作为sql过滤处理
 *
 * @see com.hccake.ballcat.common.core.domain.PageParam
 * @see PageParamArgumentResolver
 * @author Hccake 2021/1/22
 * @version 1.0
 */
@Slf4j
@Deprecated
public class IPageArgumentResolver extends PageParamArgumentResolver {

	/**
	 * 判断Controller是否包含page 参数
	 * @param parameter 参数
	 * @return 是否过滤
	 */
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return IPage.class.isAssignableFrom(parameter.getParameterType());
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
		String sortFields = request.getParameter("sortFields");
		String sortOrders = request.getParameter("sortOrders");

		Page<?> page = new Page<>();
		if (StrUtil.isNotBlank(current)) {
			page.setCurrent(Long.parseLong(current));
		}
		if (StrUtil.isNotBlank(size)) {
			page.setSize(Long.parseLong(size));
		}

		List<PageParam.Sort> sorts = getOrderItems(sortFields, sortOrders);
		for (PageParam.Sort sort : sorts) {
			OrderItem orderItem = sort.isAsc() ? OrderItem.asc(sort.getField()) : OrderItem.desc(sort.getField());
			page.addOrder(orderItem);
		}

		return page;
	}

}
