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

package org.ballcat.springsecurity.oauth2.server.authorization.web.authentication;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * OAuth2 登录失败的处理器，跳转到登录页，同时携带 return_to 参数，值为当前 request fullUrl
 *
 * @author hccake
 */
public class OAuth2LoginUrlAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {

	/**
	 * @param loginFormUrl URL where the login page can be found. Should either be
	 * relative to the web-app context path (include a leading {@code /}) or an absolute
	 * URL.
	 */
	public OAuth2LoginUrlAuthenticationEntryPoint(String loginFormUrl) {
		super(loginFormUrl);
	}

	@Override
	protected String buildRedirectUrlToLoginPage(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) {
		String loginForm = super.buildRedirectUrlToLoginPage(request, response, authException);
		return addReturnToParameter(request, loginForm);
	}

	private static String addReturnToParameter(HttpServletRequest request, String loginForm) {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("return_to", getFullUrl(request));
		UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(loginForm).queryParams(params).build();
		return uriComponents.toString();
	}

	private static String getFullUrl(HttpServletRequest req) {

		String scheme = req.getScheme();
		String serverName = req.getServerName();
		int serverPort = req.getServerPort();
		String contextPath = req.getContextPath();
		String servletPath = req.getServletPath();
		String pathInfo = req.getPathInfo();
		String queryString = req.getQueryString();

		// Reconstruct original requesting URL
		StringBuilder url = new StringBuilder();
		url.append(scheme).append("://").append(serverName);

		if (serverPort != 80 && serverPort != 443) {
			url.append(":").append(serverPort);
		}

		url.append(contextPath).append(servletPath);

		if (pathInfo != null) {
			url.append(pathInfo);
		}
		if (queryString != null) {
			url.append("?").append(queryString);
		}
		return url.toString();
	}

	@Override
	protected String buildHttpsRedirectUrlForRequest(HttpServletRequest request) throws IOException, ServletException {
		String loginForm = super.buildHttpsRedirectUrlForRequest(request);
		return addReturnToParameter(request, loginForm);
	}

}
