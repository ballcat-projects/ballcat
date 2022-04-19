package com.hccake.ballcat.common.captcha;

import org.springframework.web.context.request.ServletWebRequest;

/**
 * CaptchaRepository
 *
 * @author xm.z
 */
public interface CaptchaRepository {

	void save(ServletWebRequest request, Captcha code);

	Captcha get(ServletWebRequest request);

	void remove(ServletWebRequest request);

}