package com.hccake.ballcat.auth.configuration;

import com.hccake.ballcat.auth.OAuth2AuthorizationServerProperties;
import com.hccake.ballcat.auth.filter.LoginCaptchaFilter;
import com.hccake.ballcat.common.security.constant.SecurityConstants;
import org.ballcat.security.captcha.CaptchaValidator;
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
	 * 登录验证码拦截判断
	 * @param captchaValidator 验证码验证器
	 * @return FilterRegistrationBean<LoginCaptchaFilter>
	 */
	@Bean
	@ConditionalOnProperty(prefix = OAuth2AuthorizationServerProperties.PREFIX, name = "login-captcha-enabled",
			havingValue = "true", matchIfMissing = true)
	public FilterRegistrationBean<LoginCaptchaFilter> loginCaptchaFilter(CaptchaValidator captchaValidator) {
		FilterRegistrationBean<LoginCaptchaFilter> bean = new FilterRegistrationBean<>();
		LoginCaptchaFilter filter = new LoginCaptchaFilter(captchaValidator);
		bean.setFilter(filter);
		// 比密码解密早一步
		bean.setOrder(-1);
		bean.addUrlPatterns(SecurityConstants.LOGIN_URL);
		return bean;
	}

}
