package com.hccake.ballcat.auth.filter.captcha;

import com.hccake.ballcat.auth.filter.captcha.domain.CaptchaResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * 验证码验证器
 *
 * @author xm.z
 */
public interface CaptchaValidator {

	/**
	 * 校验验证码
	 * @param request the current request
	 * @return {@link CaptchaResponse}
	 */
	CaptchaResponse validate(HttpServletRequest request);

}
