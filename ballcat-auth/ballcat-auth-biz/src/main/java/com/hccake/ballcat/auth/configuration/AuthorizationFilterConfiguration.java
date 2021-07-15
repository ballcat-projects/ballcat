package com.hccake.ballcat.auth.configuration;

import com.anji.captcha.service.CaptchaService;
import com.hccake.ballcat.auth.OAuth2AuthorizationServerProperties;
import com.hccake.ballcat.auth.filter.LoginCaptchaFilter;
import com.hccake.ballcat.auth.filter.LoginPasswordDecoderFilter;
import com.hccake.ballcat.common.security.constant.SecurityConstants;
import com.hccake.ballcat.common.security.properties.SecurityProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 授权服务器用到的一些过滤器
 *
 * @author hccake
 */
@Configuration(proxyBeanMethods = false)
public class AuthorizationFilterConfiguration {

	/**
	 * password 模式下，密码入参要求 AES 加密。 在进入令牌端点前，通过过滤器进行解密处理。
	 * @param securityProperties 安全配置相关
	 * @return FilterRegistrationBean<LoginPasswordDecoderFilter>
	 */
	@Bean
	@ConditionalOnProperty(prefix = SecurityProperties.PREFIX, name = "password-secret-key")
	public FilterRegistrationBean<LoginPasswordDecoderFilter> loginPasswordDecoderFilter(
			SecurityProperties securityProperties) {
		FilterRegistrationBean<LoginPasswordDecoderFilter> bean = new FilterRegistrationBean<>();
		LoginPasswordDecoderFilter filter = new LoginPasswordDecoderFilter(securityProperties.getPasswordSecretKey());
		bean.setFilter(filter);
		bean.setOrder(0);
		bean.addUrlPatterns(SecurityConstants.LOGIN_URL);
		return bean;
	}

	/**
	 * 登录验证码拦截判断
	 * @param captchaService 验证码处理类
	 * @return FilterRegistrationBean<LoginCaptchaFilter>
	 */
	@Bean
	@ConditionalOnProperty(prefix = OAuth2AuthorizationServerProperties.PREFIX, name = "login-captcha-enabled",
			havingValue = "true", matchIfMissing = true)
	public FilterRegistrationBean<LoginCaptchaFilter> loginCaptchaFilter(CaptchaService captchaService) {
		FilterRegistrationBean<LoginCaptchaFilter> bean = new FilterRegistrationBean<>();
		LoginCaptchaFilter filter = new LoginCaptchaFilter(captchaService);
		bean.setFilter(filter);
		// 比密码解密早一步
		bean.setOrder(-1);
		bean.addUrlPatterns(SecurityConstants.LOGIN_URL);
		return bean;
	}

}
