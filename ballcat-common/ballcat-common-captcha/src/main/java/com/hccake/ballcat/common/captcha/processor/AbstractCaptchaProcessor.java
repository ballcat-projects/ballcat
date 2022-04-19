package com.hccake.ballcat.common.captcha.processor;

import com.hccake.ballcat.common.captcha.Captcha;
import com.hccake.ballcat.common.captcha.CaptchaGenerator;
import com.hccake.ballcat.common.captcha.CaptchaRepository;
import lombok.Setter;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * AbstractValidateCodeProcessor
 *
 * @author xm.z
 */
public abstract class AbstractCaptchaProcessor<C extends Captcha> implements CaptchaProcessor {

	@Setter
	private CaptchaGenerator captchaGenerator;

	@Setter
	private CaptchaRepository captchaRepository;

	@Override
	public void create(ServletWebRequest request) throws Exception {
		C captcha = (C) this.captchaGenerator.generate(request);
		save(request, captcha);
		send(request, captcha);
	}

	private void save(ServletWebRequest request, C captcha) {
		if (captchaRepository == null) {
			return;
		}
		this.captchaRepository.save(request, captcha);
	}

	protected void send(ServletWebRequest request, C captcha) throws Exception {
	}

}
