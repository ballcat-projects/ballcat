package com.hccake.ballcat.common.core.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * WebUtils
 *
 * @author Hccake
 */
@Slf4j
@UtilityClass
public class WebUtils extends org.springframework.web.util.WebUtils {

	/**
	 * 获取 ServletRequestAttributes
	 * @return {ServletRequestAttributes}
	 */
	public ServletRequestAttributes getServletRequestAttributes() {
		return (ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes());
	}

	/**
	 * 获取 HttpServletRequest
	 * @return {HttpServletRequest}
	 */
	public HttpServletRequest getRequest() {
		return getServletRequestAttributes().getRequest();
	}

	/**
	 * 获取 HttpServletResponse
	 * @return {HttpServletResponse}
	 */
	public HttpServletResponse getResponse() {
		return getServletRequestAttributes().getResponse();
	}

}
