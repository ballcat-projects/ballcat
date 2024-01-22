/*
 * Copyright 2023-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ballcat.autoconfigure.web.exception;

import java.util.List;

import org.ballcat.dingtalk.DingTalkSender;
import org.ballcat.mail.sender.MailSender;
import org.ballcat.web.exception.notice.DingTalkExceptionNotifier;
import org.ballcat.web.exception.notice.MailExceptionNotifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 异常通知配置
 *
 * @author Hccake
 */
@Configuration(proxyBeanMethods = false)
public class ExceptionNoticeConfiguration {

	@ConditionalOnClass(MailSender.class)
	@ConditionalOnMissingBean(MailExceptionNotifier.class)
	@ConditionalOnProperty(prefix = ExceptionNoticeProperties.PREFIX, name = "mail.recipient-emails")
	@Configuration(proxyBeanMethods = false)
	static class MailExceptionNotifierConfiguration {

		@Bean
		public MailExceptionNotifier mailExceptionNotifier(MailSender mailSender,
				ExceptionNoticeProperties exceptionNoticeProperties) {
			List<String> recipientEmails = exceptionNoticeProperties.getMail().getRecipientEmails();
			return new MailExceptionNotifier(mailSender, recipientEmails);
		}

	}

	@ConditionalOnClass(DingTalkSender.class)
	@ConditionalOnMissingBean(DingTalkExceptionNotifier.class)
	@ConditionalOnProperty(prefix = ExceptionNoticeProperties.PREFIX, name = "ding-talk.sender")
	@Configuration(proxyBeanMethods = false)
	static class DingTalkExceptionNotifierConfiguration {

		@Bean
		public DingTalkExceptionNotifier dingTalkExceptionNotifier(
				ExceptionNoticeProperties exceptionNoticeProperties) {
			ExceptionNoticeProperties.DingTalk dingTalk = exceptionNoticeProperties.getDingTalk();
			ExceptionNoticeProperties.DingTalk.Sender sender = dingTalk.getSender();
			DingTalkSender dingTalkSender = new DingTalkSender(sender.getUrl()).setSecret(sender.getSecret());
			return new DingTalkExceptionNotifier(dingTalkSender, dingTalk.getAtAll());
		}

	}

}
