package com.hccake.ballcat.auth.configuration;

import com.hccake.ballcat.auth.OAuth2AuthorizationServerProperties;
import com.hccake.ballcat.auth.filter.FilterWrapper;
import com.hccake.ballcat.auth.filter.LoginCaptchaFilter;
import com.hccake.ballcat.auth.filter.LoginPasswordDecoderFilter;
import org.ballcat.security.captcha.CaptchaValidator;
import org.ballcat.security.properties.SecurityProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * 授权服务器用到的一些过滤器
 *
 * @author hccake
 */
@Deprecated
@Configuration(proxyBeanMethods = false)
public class AuthorizationFilterConfiguration {

	/**
	 * 登录验证码拦截判断
	 * @param captchaValidator 验证码验证器
	 * @return FilterRegistrationBean<LoginCaptchaFilter>
	 */
	@Bean
	@Order(10)
	@ConditionalOnProperty(prefix = OAuth2AuthorizationServerProperties.PREFIX, name = "login-captcha-enabled",
			havingValue = "true", matchIfMissing = true)
	public FilterWrapper loginCaptchaFilter(CaptchaValidator captchaValidator) {
		return new FilterWrapper(new LoginCaptchaFilter(captchaValidator));
	}

	/**
	 * password 模式下，密码入参要求 AES 加密。 在进入令牌端点前，通过过滤器进行解密处理。
	 * @param securityProperties 安全配置相关
	 * @return FilterRegistrationBean<LoginPasswordDecoderFilter>
	 */
	@Bean
	@Order(20)
	@ConditionalOnProperty(prefix = SecurityProperties.PREFIX, name = "password-secret-key")
	public FilterWrapper loginPasswordDecoderFilter(SecurityProperties securityProperties) {
		return new FilterWrapper(new LoginPasswordDecoderFilter(securityProperties.getPasswordSecretKey()));
	}

}
