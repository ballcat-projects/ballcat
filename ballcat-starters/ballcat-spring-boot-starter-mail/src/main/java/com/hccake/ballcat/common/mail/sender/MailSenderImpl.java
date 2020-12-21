package com.hccake.ballcat.common.mail.sender;

import com.hccake.ballcat.common.mail.dto.MailDTO;
import com.hccake.ballcat.common.mail.event.MailSendEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.StringUtils;

import javax.mail.MessagingException;
import java.io.File;
import java.time.LocalDateTime;

/**
 * @author Hccake
 * @version 1.0
 * @date 2020/2/27 17:06
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
	 * @param mailDTO 邮件参数
	 * @return boolean 发送是否成功
	 */
	@Override
	public MailDTO sendMail(MailDTO mailDTO) {
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
			eventPublisher.publishEvent(new MailSendEvent(mailDTO));
		}
		return mailDTO;
	}

	/**
	 * 构建复杂邮件信息类
	 * @param mailDTO 邮件发送设置
	 */
	private void sendMimeMail(MailDTO mailDTO) throws MessagingException {
		// true表示支持复杂类型
		MimeMessageHelper messageHelper = new MimeMessageHelper(mailSender.createMimeMessage(), true);
		String from = StringUtils.hasText(mailDTO.getFrom()) ? mailDTO.getFrom() : defaultFrom;
		messageHelper.setFrom(from);
		messageHelper.setTo(mailDTO.getTo());
		messageHelper.setSubject(mailDTO.getSubject());
		// 是否展示html
		boolean showHtml = mailDTO.getShowHtml() != null && mailDTO.getShowHtml();
		messageHelper.setText(mailDTO.getContent(), showHtml);
		if (mailDTO.getCc() != null && mailDTO.getCc().length > 0) {
			messageHelper.setCc(mailDTO.getCc());
		}
		if (mailDTO.getBcc() != null && mailDTO.getBcc().length > 0) {
			messageHelper.setCc(mailDTO.getBcc());
		}
		if (mailDTO.getFiles() != null) {
			for (File file : mailDTO.getFiles()) {
				messageHelper.addAttachment(file.getName(), file);
			}
		}
		mailDTO.setSentDate(LocalDateTime.now());
		mailSender.send(messageHelper.getMimeMessage());
		log.info("发送邮件成功：{}->{}", from, mailDTO.getTo());
	}

}
