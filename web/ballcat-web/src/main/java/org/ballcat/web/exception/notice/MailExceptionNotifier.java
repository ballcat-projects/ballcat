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

package org.ballcat.web.exception.notice;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.ballcat.mail.model.MailSendInfo;
import org.ballcat.mail.sender.MailSender;
import org.ballcat.web.exception.domain.ExceptionMessage;
import org.springframework.util.Assert;

/**
 * 异常邮件通知
 *
 * @author lingting 2020/6/12 0:25
 * @author Hccake
 */
@Slf4j
public class MailExceptionNotifier implements ExceptionNotifier {

	private final MailSender sender;

	private final List<String> recipientEmails;

	public MailExceptionNotifier(MailSender sender, List<String> recipientEmails) {
		Assert.notNull(sender, "MailSender 不可为空!");
		Assert.notEmpty(recipientEmails, "收件人邮箱不可为空!");
		this.sender = sender;
		this.recipientEmails = recipientEmails;
	}

	@Override
	public ExceptionNoticeResponse notify(ExceptionMessage sendMessage) {
		MailSendInfo mailSendInfo = this.sender.sendTextMail("异常警告", sendMessage.toString(), this.recipientEmails);
		// 邮箱发送失败会抛出异常，否则视作发送成功
		return new ExceptionNoticeResponse().setSuccess(mailSendInfo.getSuccess())
			.setErrMsg(mailSendInfo.getErrorMsg());
	}

}
