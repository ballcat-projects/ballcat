/*
 * Copyright 2023 the original author or authors.
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
package com.hccake.ballcat.autoconfigure.web.exception.handler;

import com.hccake.ballcat.autoconfigure.web.exception.ExceptionHandleProperties;
import com.hccake.ballcat.autoconfigure.web.exception.domain.ExceptionMessage;
import com.hccake.ballcat.autoconfigure.web.exception.domain.ExceptionNoticeResponse;
import com.hccake.ballcat.common.mail.model.MailSendInfo;
import com.hccake.ballcat.common.mail.sender.MailSender;
import lombok.extern.slf4j.Slf4j;

/**
 * 异常邮件通知
 *
 * @author lingting 2020/6/12 0:25
 */
@Slf4j
public class MailGlobalExceptionHandler extends AbstractNoticeGlobalExceptionHandler {

	private final MailSender sender;

	public MailGlobalExceptionHandler(ExceptionHandleProperties config, MailSender sender, String applicationName) {
		super(config, applicationName);
		this.sender = sender;
	}

	@Override
	public ExceptionNoticeResponse send(ExceptionMessage sendMessage) {
		String[] to = config.getReceiveEmails().toArray(new String[0]);
		MailSendInfo mailSendInfo = sender.sendTextMail("异常警告", sendMessage.toString(), to);
		// 邮箱发送失败会抛出异常，否则视作发送成功
		return new ExceptionNoticeResponse().setSuccess(mailSendInfo.getSuccess())
			.setErrMsg(mailSendInfo.getErrorMsg());
	}

}
