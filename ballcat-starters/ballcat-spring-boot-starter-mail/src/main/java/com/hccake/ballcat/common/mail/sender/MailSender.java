package com.hccake.ballcat.common.mail.sender;

import com.hccake.ballcat.common.mail.dto.MailDTO;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author Hccake
 * @version 1.0
 * @date 2020/2/27 17:05
 */
public interface MailSender {

	/**
	 * 发送邮件
	 * @param mailDTO 邮件信息
	 * @return MailDTO
	 */
	MailDTO sendMail(MailDTO mailDTO);

	/**
	 * 发送邮件
	 * @param subject 主题
	 * @param content 邮件正文
	 * @param showHtml 是否将正文渲染为html
	 * @param to 收件人，多个邮箱使用,号间隔
	 * @return MailDTO
	 */
	default MailDTO sendMail(String subject, String content, boolean showHtml, String... to) {
		MailDTO mailDTO = new MailDTO();
		mailDTO.setShowHtml(showHtml);
		mailDTO.setSubject(subject);
		mailDTO.setContent(content);
		mailDTO.setTo(to);
		return sendMail(mailDTO);
	}

	/**
	 * 发送普通文本邮件
	 * @param subject 主题
	 * @param content 邮件正文
	 * @param to 收件人
	 * @return MailDTO
	 */
	default MailDTO sendTextMail(String subject, String content, String... to) {
		return sendMail(subject, content, false, to);
	}

	/**
	 * 发送普通文本邮件
	 * @param subject 主题
	 * @param content 邮件正文
	 * @param to 收件人
	 * @return MailDTO
	 */
	default MailDTO sendTextMail(String subject, String content, List<String> to) {
		return sendMail(subject, content, false, to.toArray(new String[0]));
	}

	/**
	 * 发送Html邮件
	 * @param subject 主题
	 * @param content 邮件正文
	 * @param to 收件人
	 * @return MailDTO
	 */
	default MailDTO sendHtmlMail(String subject, String content, String... to) {
		return sendMail(subject, content, true, to);
	}

	/**
	 * 发送Html邮件
	 * @param subject 主题
	 * @param content 邮件正文
	 * @param to 收件人
	 * @return boolean 发送是否成功
	 */
	default MailDTO sendHtmlMail(String subject, String content, List<String> to) {
		return sendHtmlMail(subject, content, to.toArray(new String[0]));
	}

	/**
	 * 检查邮件是否符合标准
	 * @param mailDTO 邮件信息
	 */
	default void checkMail(MailDTO mailDTO) {
		if (mailDTO.getTo() == null || mailDTO.getTo().length <= 0) {
			throw new RuntimeException("邮件收信人不能为空");
		}
		if (!StringUtils.hasText(mailDTO.getSubject())) {
			throw new RuntimeException("邮件主题不能为空");
		}
		if (!StringUtils.hasText(mailDTO.getContent())) {
			throw new RuntimeException("邮件内容不能为空");
		}
	}

}
