package com.hccake.starter.captcha;

import com.anji.captcha.service.CaptchaService;
import com.hccake.ballcat.common.captcha.processor.CaptchaProcessor;
import com.hccake.starter.captcha.anji.AnJiCaptchaProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class CaptchaAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public CaptchaProcessor captchaProcessor(CaptchaService captchaService) {
		return new AnJiCaptchaProcessor(captchaService);
	}

}
