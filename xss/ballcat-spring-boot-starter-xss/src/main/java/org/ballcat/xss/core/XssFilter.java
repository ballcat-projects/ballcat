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

package org.ballcat.xss.core;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import org.ballcat.xss.cleaner.XssCleaner;
import org.ballcat.xss.config.XssProperties;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * @author Hccake 2019/10/17 20:28
 */
@RequiredArgsConstructor
public class XssFilter extends OncePerRequestFilter {

	/**
	 * Xss 防注入配置
	 */
	private final XssProperties xssProperties;

	private final XssCleaner xssCleaner;

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
			filterChain.doFilter(new XssRequestWrapper(request, this.xssCleaner), response);
		}
		finally {
			// 必须删除 ThreadLocal 存储的状态
			XssStateHolder.remove();
		}
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		// 关闭直接跳过
		if (!this.xssProperties.isEnabled()) {
			return true;
		}

		// 请求方法检查
		if (this.xssProperties.getIncludeHttpMethods().stream().noneMatch(request.getMethod()::equalsIgnoreCase)) {
			return true;
		}

		// 请求路径检查
		String requestUri = request.getRequestURI();
		// 此路径是否不需要处理
		for (String exclude : this.xssProperties.getExcludePaths()) {
			if (ANT_PATH_MATCHER.match(exclude, requestUri)) {
				return true;
			}
		}

		// 路径是否包含
		for (String include : this.xssProperties.getIncludePaths()) {
			if (ANT_PATH_MATCHER.match(include, requestUri)) {
				return false;
			}
		}

		return false;
	}

}
