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

package org.ballcat.mail.sender;

import java.io.File;
import java.time.LocalDateTime;

import javax.mail.MessagingException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ballcat.mail.event.MailSendEvent;
import org.ballcat.mail.model.MailDetails;
import org.ballcat.mail.model.MailSendInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.StringUtils;

/**
 * @author Hccake 2020/2/27 17:06
 */
@Slf4j
@RequiredArgsConstructor
public class MailSenderImpl implements MailSender {

	private final JavaMailSender mailSender;

	private final ApplicationEventPublisher eventPublisher;

	/**
	 * 配置文件中我的qq邮箱
	 */
	@Value("${spring.mail.properties.from}")
	private String defaultFrom;

	/**
	 * 发送邮件
	 * @param mailDetails 邮件参数
	 * @return boolean 发送是否成功
	 */
	@Override
	public MailSendInfo sendMail(MailDetails mailDetails) {
		MailSendInfo mailSendInfo = new MailSendInfo(mailDetails);
		mailSendInfo.setSentDate(LocalDateTime.now());

		try {
			// 1.检测邮件
			checkMail(mailDetails);
			// 2.发送邮件
			sendMimeMail(mailDetails);
			mailSendInfo.setSuccess(true);
		}
		catch (Exception e) {
			mailSendInfo.setSuccess(false);
			mailSendInfo.setErrorMsg(e.getMessage());
			log.error("发送邮件失败: [{}]", mailDetails, e);
		}
		finally {
			// 发布邮件发送事件
			this.eventPublisher.publishEvent(new MailSendEvent(mailSendInfo));
		}
		return mailSendInfo;
	}

	/**
	 * 构建复杂邮件信息类
	 * @param mailDetails 邮件发送设置
	 */
	private void sendMimeMail(MailDetails mailDetails) throws MessagingException {
		// true表示支持复杂类型
		MimeMessageHelper messageHelper = new MimeMessageHelper(this.mailSender.createMimeMessage(), true);
		String from = StringUtils.hasText(mailDetails.getFrom()) ? mailDetails.getFrom() : this.defaultFrom;
		messageHelper.setFrom(from);
		messageHelper.setSubject(mailDetails.getSubject());
		if (mailDetails.getTo() != null && mailDetails.getTo().length > 0) {
			messageHelper.setTo(mailDetails.getTo());
		}
		if (mailDetails.getCc() != null && mailDetails.getCc().length > 0) {
			messageHelper.setCc(mailDetails.getCc());
		}
		if (mailDetails.getBcc() != null && mailDetails.getBcc().length > 0) {
			messageHelper.setBcc(mailDetails.getBcc());
		}
		// 是否展示html
		boolean showHtml = mailDetails.getShowHtml() != null && mailDetails.getShowHtml();
		messageHelper.setText(mailDetails.getContent(), showHtml);
		if (mailDetails.getFiles() != null) {
			for (File file : mailDetails.getFiles()) {
				messageHelper.addAttachment(file.getName(), file);
			}
		}

		this.mailSender.send(messageHelper.getMimeMessage());
		log.info("发送邮件成功：[{}]", mailDetails);
	}

}
