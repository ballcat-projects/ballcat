package com.hccake.ballcat.common.mail.service.impl;

import com.hccake.ballcat.common.mail.dto.MailDTO;
import com.hccake.ballcat.common.mail.event.MailSendEvent;
import com.hccake.ballcat.common.mail.service.MailSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.StringUtils;

import java.io.File;

/**
 * @author Hccake
 * @version 1.0
 * @date 2020/2/27 17:06
 */
@Slf4j
@RequiredArgsConstructor
public class MailSenderImpl implements MailSender {

	private final JavaMailSender mailSender;

	private final ApplicationContext applicationContext;

	/**
	 * 配置文件中我的qq邮箱
	 */
	@Value("${spring.mail.properties.from}")
	private String defaultFrom;

	/**
	 * 发送邮件
	 * @param mailDTO 邮件参数
	 */
	@Override
	public void sendMail(MailDTO mailDTO) {
		try {
			// 1.检测邮件
			checkMail(mailDTO);
			// 2.发送邮件
			sendMimeMail(mailDTO);
			mailDTO.setSuccess(true);
		}
		catch (Exception e) {
			mailDTO.setSuccess(false);
			mailDTO.setErrorMsg(e.getMessage());
			log.error("发送邮件失败:", e);
		}
		finally {
			// 发布邮件发送事件
			applicationContext.publishEvent(new MailSendEvent(mailDTO));
		}
	}

	/**
	 * 构建复杂邮件信息类
	 * @param mailDTO 邮件发送设置
	 */
	private void sendMimeMail(MailDTO mailDTO) {
		try {
			// true表示支持复杂类型
			MimeMessageHelper messageHelper = new MimeMessageHelper(mailSender.createMimeMessage(), true);
			String from = StringUtils.isEmpty(mailDTO.getFrom()) ? defaultFrom : mailDTO.getFrom();
			messageHelper.setFrom(from);
			messageHelper.setTo(mailDTO.getTo().split(","));
			messageHelper.setSubject(mailDTO.getSubject());
			// 是否展示html
			boolean showHtml = mailDTO.getShowHtml() == null ? false : mailDTO.getShowHtml();
			messageHelper.setText(mailDTO.getContent(), showHtml);
			if (!StringUtils.isEmpty(mailDTO.getCc())) {
				messageHelper.setCc(mailDTO.getCc().split(","));
			}
			if (!StringUtils.isEmpty(mailDTO.getBcc())) {
				messageHelper.setCc(mailDTO.getBcc().split(","));
			}
			if (mailDTO.getFiles() != null) {
				for (File file : mailDTO.getFiles()) {
					messageHelper.addAttachment(file.getName(), file);
				}
			}
			mailSender.send(messageHelper.getMimeMessage());
			log.info("发送邮件成功：{}->{}", from, mailDTO.getTo());
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
