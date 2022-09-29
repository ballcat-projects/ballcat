package org.ballcat.security.captcha;

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
	 * @return {@link CaptchaValidateResult}
	 */
	CaptchaValidateResult validate(HttpServletRequest request);

}
