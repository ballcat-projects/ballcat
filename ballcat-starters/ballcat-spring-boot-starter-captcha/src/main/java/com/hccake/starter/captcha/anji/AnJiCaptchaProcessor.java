package com.hccake.starter.captcha.anji;

import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.hccake.ballcat.common.captcha.Captcha;
import com.hccake.ballcat.common.captcha.domain.CaptchaResponse;
import com.hccake.ballcat.common.captcha.processor.AbstractCaptchaProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * @author xiaomingzhang
 */
@RequiredArgsConstructor
public class AnJiCaptchaProcessor extends AbstractCaptchaProcessor<Captcha> {

	private static final String CAPTCHA_VERIFICATION_PARAM = "captchaVerification";

	private final CaptchaService captchaService;

	@Override
	public CaptchaResponse verification(ServletWebRequest request) {
		String captchaVerification = request.getParameter(CAPTCHA_VERIFICATION_PARAM);

		CaptchaVO captchaVO = new CaptchaVO();
		captchaVO.setCaptchaVerification(captchaVerification);
		ResponseModel responseModel = captchaService.verification(captchaVO);

		return new CaptchaResponse().setSuccess(responseModel.isSuccess()).setErrMsg(responseModel.getRepMsg());
	}

}
