package com.hccake.ballcat.common.mail;

import com.hccake.ballcat.common.mail.sender.MailSender;
import com.hccake.ballcat.common.mail.sender.MailSenderImpl;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * @author Hccake 2021/1/7
 * @version 1.0
 */
@AutoConfigureAfter(MailSenderAutoConfiguration.class)
@Configuration(proxyBeanMethods = false)
public class MailAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean(MailSender.class)
	@ConditionalOnProperty(prefix = "spring.mail", name = "host")
	public MailSender mailSenderImpl(JavaMailSender javaMailSender,
			ApplicationEventPublisher applicationEventPublisher) {
		return new MailSenderImpl(javaMailSender, applicationEventPublisher);
	}

}
