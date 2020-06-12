package com.hccake.ballcat.common.conf.exception.handler;

import com.hccake.ballcat.common.conf.config.ExceptionHandleConfig;
import com.hccake.ballcat.common.conf.exception.domain.ExceptionMessage;
import com.hccake.ballcat.common.conf.exception.domain.ExceptionNoticeResponse;
import com.hccake.ballcat.common.mail.dto.MailDTO;
import com.hccake.ballcat.common.mail.service.MailSender;
import lombok.extern.slf4j.Slf4j;

/**
 * 钉钉消息通知
 *
 * @author lingting  2020/6/12 0:25
 */
@Slf4j
public class MailGlobalExceptionHandler extends AbstractNoticeGlobalExceptionHandler {
	private final MailSender sender;

	public MailGlobalExceptionHandler(ExceptionHandleConfig config, MailSender sender, String applicationName) {
		super(config, applicationName);
		this.sender = sender;
	}

	@Override
	public ExceptionNoticeResponse send(ExceptionMessage sendMessage) {
		MailDTO mail = new MailDTO();
		mail.setTo(String.join(MailDTO.MAIL_DELIMITER, config.getReceiveEmails()));
		mail.setSubject("异常警告");
		mail.setContent(sendMessage.toString());
		sender.sendMail(mail);
		// 邮箱发送失败会抛出异常，否则视作发送成功
		return new ExceptionNoticeResponse().setSuccess(mail.getSuccess()).setErrMsg(mail.getErrorMsg());
	}
}
