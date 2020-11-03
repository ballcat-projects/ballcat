package com.hccake.ballcat.commom.log.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/10/15 22:53
 */
@Slf4j
@UtilityClass
public class LogUtils {

	/**
	 * 获取当前登陆用户名
	 * @return current login user name
	 */
	public String getUsername() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			return null;
		}
		return authentication.getName();
	}

	/**
	 * 获取请求体
	 * @param request 请求体
	 * @return requestBody
	 */
	public String getRequestBody(HttpServletRequest request) {
		String body = null;
		if (!request.getMethod().equals(HttpMethod.GET.name())) {
			try {
				BufferedReader reader = request.getReader();
				if (reader != null) {
					body = reader.lines().collect(Collectors.joining(System.lineSeparator()));
				}
			}
			catch (Exception e) {
				log.error("读取请求体异常：", e);
			}
		}
		return body;
	}

	/**
	 * 获取响应体
	 * @param response 响应信息
	 * @return responseBody 响应体
	 */
	public String getResponseBody(HttpServletResponse response) {
		HttpServletRequest request = getHttpServletRequest();
		return getResponseBody(request, response);
	}

	/**
	 * 获取响应体 防止在 {@link org.springframework.web.context.request.RequestContextHolder}
	 * 设置内容之前或清空内容之后使用，从而导致获取不到响应体的问题
	 * @param request 请求信息
	 * @param response 响应信息
	 * @return responseBody 响应体
	 */
	public String getResponseBody(HttpServletRequest request, HttpServletResponse response) {
		try {
			if (response instanceof ContentCachingResponseWrapper) {
				ContentCachingResponseWrapper responseWrapper = (ContentCachingResponseWrapper) response;
				// 获取响应体
				byte[] contentAsByteArray = responseWrapper.getContentAsByteArray();
				return new String(contentAsByteArray, StandardCharsets.UTF_8);
			}
			log.warn("对于未包装的响应体，默认不进行读取请求体，请求 uri: [{}]", request.getRequestURI());
		}
		catch (Exception exception) {
			log.error("获取响应体信息失败，请求 uri: [{}]", request.getRequestURI());
		}
		return "";
	}

	/**
	 * 判断是否是multipart/form-data请求
	 * @param request 请求信息
	 * @return 是否是multipart/form-data请求
	 */
	public boolean isMultipartContent(HttpServletRequest request) {
		// 获取Content-Type
		String contentType = request.getContentType();
		return (contentType != null) && (contentType.toLowerCase().startsWith("multipart/"));
	}

	/**
	 * 获取当前请求的request
	 * @return HttpServletRequest
	 */
	public HttpServletRequest getHttpServletRequest() {
		return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
				.getRequest();
	}

}
