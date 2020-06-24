package com.hccake.ballcat.common.mail.service;

import com.hccake.ballcat.common.mail.dto.MailDTO;
import org.springframework.util.StringUtils;

/**
 * @author Hccake
 * @version 1.0
 * @date 2020/2/27 17:05
 */
public interface MailSender {

	/**
	 * 发送邮件
	 * @param mailDTO 邮件信息
	 */
	void sendMail(MailDTO mailDTO);

	/**
	 * 发送邮件
	 * @param to 收件人，多个邮箱使用,号间隔
	 * @param subject 主题
	 * @param content 邮件正文
	 * @param showHtml 是否将正文渲染为html
	 */
	default void sendMail(String to, String subject, String content, boolean showHtml) {
		MailDTO mailDTO = new MailDTO();
		mailDTO.setShowHtml(showHtml);
		mailDTO.setSubject(subject);
		mailDTO.setContent(content);
		mailDTO.setTo(to);
		sendMail(mailDTO);
	}

	/**
	 * 发送普通文本邮件
	 * @param to 收件人，多个邮箱使用,号间隔
	 * @param subject 主题
	 * @param content 邮件正文
	 */
	default void sendTextMail(String to, String subject, String content) {
		sendMail(to, subject, content, false);
	}

	/**
	 * 发送Html邮件
	 * @param to 收件人，多个邮箱使用,号间隔
	 * @param subject 主题
	 * @param content 邮件正文
	 */
	default void sendHtmlMail(String to, String subject, String content) {
		sendMail(to, subject, content, true);
	}

	/**
	 * 检查邮件是否符合标准
	 * @param mailDTO 邮件信息
	 */
	default void checkMail(MailDTO mailDTO) {
		if (StringUtils.isEmpty(mailDTO.getTo())) {
			throw new RuntimeException("邮件收信人不能为空");
		}
		if (StringUtils.isEmpty(mailDTO.getSubject())) {
			throw new RuntimeException("邮件主题不能为空");
		}
		if (StringUtils.isEmpty(mailDTO.getContent())) {
			throw new RuntimeException("邮件内容不能为空");
		}
	}

}
