package com.hccake.ballcat.admin;

import com.hccake.ballcat.admin.modules.notify.push.MailNotifyPusher;
import com.hccake.ballcat.admin.modules.sys.checker.AdminRuleProperties;
import com.hccake.ballcat.admin.oauth.UserInfoCoordinator;
import com.hccake.ballcat.common.mail.MailAutoConfiguration;
import com.hccake.ballcat.common.mail.sender.MailSender;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
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
@ComponentScan
@ServletComponentScan("com.hccake.ballcat.admin.oauth.filter")
@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter(MailAutoConfiguration.class)
@EnableConfigurationProperties(AdminRuleProperties.class)
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

}
