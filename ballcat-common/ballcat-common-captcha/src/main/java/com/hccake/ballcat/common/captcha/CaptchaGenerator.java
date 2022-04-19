package com.hccake.ballcat.common.captcha;

import org.springframework.web.context.request.ServletWebRequest;

/**
 * CaptchaGenerator
 *
 * @author xm.z
 */
public interface CaptchaGenerator {

	/**
	 * Generate check code
	 * @param request the request
	 * @return {@link Captcha}
	 */
	Captcha generate(ServletWebRequest request);

}