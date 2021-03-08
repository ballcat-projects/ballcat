package com.hccake.ballcat.common.core.filter;

import cn.hutool.core.util.StrUtil;
import com.hccake.ballcat.common.core.request.wrapper.XssRequestWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/10/17 20:28
 */
@RequiredArgsConstructor
public class XssFilter extends OncePerRequestFilter {

	/**
	 * 需要处理的 HTTP 请求方法集合
	 */
	private final Set<String> includeHttpMethods;

	/**
	 * xss 过滤包含的路径（Ant风格）
	 **/
	private final Set<String> includePaths;

	/**
	 * xss 需要排除的路径（Ant风格），优先级高于包含路径
	 **/
	private final Set<String> excludePaths;

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
		filterChain.doFilter(new XssRequestWrapper(request), response);
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		// 请求方法检查
		if (!StrUtil.equalsAnyIgnoreCase(request.getMethod(), includeHttpMethods.toArray(new String[] {}))) {
			return true;
		}

		// 请求路径检查
		String requestUri = request.getRequestURI();
		// 此路径是否不需要处理
		for (String exclude : excludePaths) {
			if (ANT_PATH_MATCHER.match(exclude, requestUri)) {
				return true;
			}
		}
		// 路径是否包含
		for (String include : includePaths) {
			if (ANT_PATH_MATCHER.match(include, requestUri)) {
				return false;
			}
		}

		return false;
	}

}
