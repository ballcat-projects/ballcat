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
package org.ballcat.autoconfigure.web.exception.handler;

import org.ballcat.autoconfigure.web.exception.ExceptionHandleProperties;
import org.ballcat.autoconfigure.web.exception.domain.ExceptionMessage;
import org.ballcat.autoconfigure.web.exception.domain.ExceptionNoticeResponse;
import org.ballcat.mail.sender.MailSender;
import org.ballcat.dingtalk.DingTalkSender;
import org.ballcat.dingtalk.message.DingTalkTextMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 多渠道异常处理类
 *
 * @author lingting 2022/10/28 10:12
 */
@Slf4j
public class MultiGlobalExceptionHandler extends AbstractNoticeGlobalExceptionHandler {

	private final Object mailSender;

	private final String[] receiveEmails;

	private final List<Object> dingTalkSenders;

	public MultiGlobalExceptionHandler(ExceptionHandleProperties config, String applicationName, Object mailSender) {
		super(config, applicationName);
		this.mailSender = mailSender;
		if (CollectionUtils.isEmpty(config.getReceiveEmails())) {
			this.receiveEmails = new String[0];
		}
		else {
			this.receiveEmails = config.getReceiveEmails().toArray(new String[0]);
		}

		ExceptionHandleProperties.DingTalkProperties dingTalkProperties = config.getDingTalk();
		if (dingTalkProperties == null || CollectionUtils.isEmpty(dingTalkProperties.getSenders())) {
			this.dingTalkSenders = new ArrayList<>(0);
		}
		else {
			this.dingTalkSenders = new ArrayList<>(dingTalkProperties.getSenders().size());
			for (ExceptionHandleProperties.DingTalkProperties.Sender s : dingTalkProperties.getSenders()) {
				DingTalkSender sender = new DingTalkSender(s.getUrl());
				sender.setSecret(s.getSecret());
				dingTalkSenders.add(sender);
			}
		}

	}

	@Override
	public ExceptionNoticeResponse send(ExceptionMessage sendMessage) {
		if (receiveEmails != null && receiveEmails.length > 0) {
			try {
				String[] to = config.getReceiveEmails().toArray(new String[0]);
				((MailSender) mailSender).sendTextMail("异常警告", sendMessage.toString(), to);
			}
			catch (Exception e) {
				log.error("邮箱异常通知发送异常! emails: {}", Arrays.toString(receiveEmails));
			}
		}

		for (Object obj : dingTalkSenders) {
			DingTalkSender sender = (DingTalkSender) obj;
			try {
				DingTalkTextMessage message = new DingTalkTextMessage().setContent(sendMessage.toString());
				if (Boolean.TRUE.equals(config.getDingTalk().getAtAll())) {
					message.atAll();
				}
				sender.sendMessage(message);
			}
			catch (Exception e) {
				log.error("钉钉异常通知发送异常! webHook: {}", sender.getUrl(), e);
			}
		}

		return new ExceptionNoticeResponse().setSuccess(true);
	}

}