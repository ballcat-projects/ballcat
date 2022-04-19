package com.hccake.ballcat.common.code;

import org.springframework.web.context.request.ServletWebRequest;

/**
 * ValidateCodeGenerator
 *
 * @author xm.z
 */
public interface ValidateCodeGenerator {

	/**
	 * Generate check code
	 * @param request the request
	 * @return {@link ValidateCode}
	 */
	ValidateCode generate(ServletWebRequest request);

}