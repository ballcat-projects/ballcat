package com.hccake.ballcat.common.code;

import org.springframework.web.context.request.ServletWebRequest;

/**
 * ValidateCodeProcessor
 *
 * @author xm.z
 */
public interface ValidateCodeProcessor {

	/**
	 * Create verification code
	 * @param request the request
	 * @throws Exception the exception
	 */
	void create(ServletWebRequest request) throws Exception;

	/**
	 * Verification code
	 * @param request the request
	 */
	void validate(ServletWebRequest request);

}