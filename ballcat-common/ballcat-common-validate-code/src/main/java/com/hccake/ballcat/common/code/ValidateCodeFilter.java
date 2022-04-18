package com.hccake.ballcat.common.code;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpMethod;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.UrlPathHelper;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static com.hccake.ballcat.common.code.ValidateCodeType.IMAGE;
import static com.hccake.ballcat.common.code.ValidateCodeType.SMS;

/**
 * ValidateCodeFilter
 *
 * @author xm.z
 */
@Slf4j
public class ValidateCodeFilter extends OncePerRequestFilter implements InitializingBean {

	/**
	 * 存放所有需要校验验证码的url
	 */
	private final Map<String, ValidateCodeType> urlMap = new HashMap<>();

	/**
	 * 验证请求url与配置的url是否匹配的工具类
	 */
	private final AntPathMatcher pathMatcher = new AntPathMatcher();

	/**
	 * URL路径匹配的帮助工具类
	 */
	private final UrlPathHelper urlPathHelper = new UrlPathHelper();

	/**
	 * 系统配置信息
	 */
	@Resource
	private ValidateCodeProperties validateCodeProperties;

	/**
	 * 系统中的校验码处理器
	 */
	@Resource
	private ValidateCodeProcessorHolder validateCodeProcessorHolder;

	/**
	 * 初始化要拦截的url配置信息
	 */
	@Override
	public void afterPropertiesSet() throws ServletException {
		super.afterPropertiesSet();
		addUrlToMap(validateCodeProperties.getImage().getUrls(), IMAGE);
		addUrlToMap(validateCodeProperties.getSms().getUrls(), SMS);
	}

	/**
	 * 将系统中配置的需要校验验证码的URL根据校验的类型放入map
	 * @param urls url list
	 * @param type verification type
	 */
	protected void addUrlToMap(Set<String> urls, ValidateCodeType type) {
		urls.forEach(value -> urlMap.put(value, type));
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		ValidateCodeType type = getValidateCodeType(request);

		if (Objects.nonNull(type)) {
			ServletWebRequest servletWebRequest = new ServletWebRequest(request, response);
			log.debug("当前请求：[{}] 需通过[{}]验证码校验.", request.getRequestURI(), type);
			validateCodeProcessorHolder.findValidateCodeProcessor(type).validate(servletWebRequest);
			log.debug("当前请求：[{}] [{}]验证码已校验通过.", request.getRequestURI(), type);
		}

		chain.doFilter(request, response);
	}

	/**
	 * 获取校验码的类型，如果当前请求不需要校验，则返回null
	 * @param request the current request attributes
	 * @return {@link ValidateCodeType}
	 */
	private ValidateCodeType getValidateCodeType(HttpServletRequest request) {
		ValidateCodeType validateCodeType = null;
		if (!HttpMethod.GET.matches(request.getMethod().toUpperCase())) {
			String lookupPath = urlPathHelper.getLookupPathForRequest(request);
			for (String url : urlMap.keySet()) {
				if (pathMatcher.match(url, lookupPath)) {
					validateCodeType = urlMap.get(url);
				}
			}
		}
		return validateCodeType;
	}

}