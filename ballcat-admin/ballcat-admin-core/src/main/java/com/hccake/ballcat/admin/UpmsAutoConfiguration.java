package com.hccake.ballcat.admin;

import com.anji.captcha.service.CaptchaService;
import com.hccake.ballcat.common.security.annotation.EnableOauth2ResourceServer;
import com.hccake.ballcat.common.security.constant.SecurityConstants;
import com.hccake.ballcat.oauth.UserInfoCoordinator;
import com.hccake.ballcat.oauth.filter.LoginCaptchaFilter;
import com.hccake.ballcat.system.properties.UpmsProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Hccake
 * @version 1.0
 * @date 2020/5/25 21:01
 */
@MapperScan("com.hccake.ballcat.**.mapper")
@ComponentScan({ "com.hccake.ballcat.admin", "com.hccake.ballcat.oauth", "com.hccake.ballcat.system",
		"com.hccake.ballcat.log", "com.hccake.ballcat.file", "com.hccake.ballcat.notify" })
@ServletComponentScan("com.hccake.ballcat.oauth.filter")
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(UpmsProperties.class)
@EnableOauth2ResourceServer
public class UpmsAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public UserInfoCoordinator userInfoCoordinator() {
		return new UserInfoCoordinator();
	}

	@Bean
	@ConditionalOnProperty(prefix = "ballcat.upms", name = "loginCaptchaEnabled", havingValue = "true",
			matchIfMissing = true)
	public FilterRegistrationBean<LoginCaptchaFilter> filterRegistrationBean(CaptchaService captchaService) {
		FilterRegistrationBean<LoginCaptchaFilter> bean = new FilterRegistrationBean<>();
		LoginCaptchaFilter filter = new LoginCaptchaFilter(captchaService);
		bean.setFilter(filter);
		// 比密码解密早一步
		bean.setOrder(-1);
		bean.addUrlPatterns(SecurityConstants.LOGIN_URL);
		return bean;
	}

}
