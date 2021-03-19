package com.hccake.ballcat.codegen.datasource;

import com.baomidou.dynamic.datasource.processor.DsProcessor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @author Hccake
 * @version 1.0
 * @date 2020/6/17 16:40
 */
public class DsRequestProcessor extends DsProcessor {

	/**
	 * request prefix
	 */
	private static final String REQUEST_PREFIX = "#request";

	@Override
	public boolean matches(String key) {
		return key.startsWith(REQUEST_PREFIX);
	}

	/**
	 * 如果没传参数，默认返回master
	 * @param invocation 拦截方法
	 * @param key 注解属性
	 * @return dsName
	 */
	@Override
	public String doDetermineDatasource(MethodInvocation invocation, String key) {
		HttpServletRequest request = ((ServletRequestAttributes) Objects
				.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
		return request.getParameter(key.substring(9));
	}

}
