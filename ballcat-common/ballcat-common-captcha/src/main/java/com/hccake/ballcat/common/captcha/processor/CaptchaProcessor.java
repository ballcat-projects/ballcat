package com.hccake.ballcat.common.captcha.processor;

import com.hccake.ballcat.common.captcha.domain.CaptchaResponse;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * CaptchaProcessor
 *
 * @author xm.z
 */
public interface CaptchaProcessor {

	/**
	 * 创建验证码
	 * @param request the current request
	 * @throws Exception 异常
	 */
	void create(ServletWebRequest request) throws Exception;

	/**
	 * 校验验证码
	 * @param request the current request
	 * @return {@link CaptchaResponse}
	 */
	CaptchaResponse verification(ServletWebRequest request);

}