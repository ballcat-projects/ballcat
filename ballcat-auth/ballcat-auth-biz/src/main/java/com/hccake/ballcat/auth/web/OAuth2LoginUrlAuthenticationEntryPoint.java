package com.hccake.ballcat.auth.web;

import cn.hutool.core.net.url.UrlBuilder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
		return UrlBuilder.of(loginForm).addQuery("return_to", getFullUrl(request)).build();
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