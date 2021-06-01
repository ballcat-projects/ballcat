package com.hccake.ballcat.admin;

import com.anji.captcha.service.CaptchaService;
import com.hccake.ballcat.admin.modules.notify.push.MailNotifyPusher;
import com.hccake.ballcat.common.mail.MailAutoConfiguration;
import com.hccake.ballcat.common.mail.sender.MailSender;
import com.hccake.ballcat.oauth.UserInfoCoordinator;
import com.hccake.ballcat.oauth.constant.SecurityConst;
import com.hccake.ballcat.oauth.filter.LoginCaptchaFilter;
import com.hccake.ballcat.system.properties.UpmsProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
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
@ComponentScan("com.hccake.ballcat")
@ServletComponentScan("com.hccake.ballcat.oauth.filter")
@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter(MailAutoConfiguration.class)
@EnableConfigurationProperties(UpmsProperties.class)
public class UpmsAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public UserInfoCoordinator userInfoCoordinator() {
		return new UserInfoCoordinator();
	}

	@Bean
	@ConditionalOnBean(MailSender.class)
	public MailNotifyPusher mailNotifyPusher(MailSender mailSender) {
		return new MailNotifyPusher(mailSender);
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
		bean.addUrlPatterns(SecurityConst.LOGIN_URL);
		return bean;
	}

}
