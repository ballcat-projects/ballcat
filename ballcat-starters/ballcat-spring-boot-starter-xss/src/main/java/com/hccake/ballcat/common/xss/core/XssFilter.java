package com.hccake.ballcat.common.xss.core;

import cn.hutool.core.util.StrUtil;
import com.hccake.ballcat.common.xss.config.XssProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/10/17 20:28
 */
@RequiredArgsConstructor
public class XssFilter extends OncePerRequestFilter {

	/**
	 * Xss 防注入配置
	 */
	private final XssProperties xssProperties;

	/**
	 * AntPath规则匹配器
	 */
	private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

	/**
	 * Same contract as for {@code doFilter}, but guaranteed to be just invoked once per
	 * request within a single request thread. See {@link #shouldNotFilterAsyncDispatch()}
	 * for details.
	 * <p>
	 * Provides HttpServletRequest and HttpServletResponse arguments instead of the
	 * default ServletRequest and ServletResponse ones.
	 * @param request 请求信息
	 * @param response 响应信息
	 * @param filterChain 过滤器链
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// 开启 Xss 过滤状态
		XssStateHolder.open();
		try {
			filterChain.doFilter(new XssRequestWrapper(request), response);
		}
		finally {
			// 必须删除 ThreadLocal 存储的状态
			XssStateHolder.remove();
		}
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		// 关闭直接跳过
		if (!xssProperties.isEnabled()) {
			return true;
		}

		// 请求方法检查
		if (!StrUtil.equalsAnyIgnoreCase(request.getMethod(),
				xssProperties.getIncludeHttpMethods().toArray(new String[] {}))) {
			return true;
		}

		// 请求路径检查
		String requestUri = request.getRequestURI();
		// 此路径是否不需要处理
		for (String exclude : xssProperties.getExcludePaths()) {
			if (ANT_PATH_MATCHER.match(exclude, requestUri)) {
				return true;
			}
		}

		// 路径是否包含
		for (String include : xssProperties.getIncludePaths()) {
			if (ANT_PATH_MATCHER.match(include, requestUri)) {
				return false;
			}
		}

		return false;
	}

}
