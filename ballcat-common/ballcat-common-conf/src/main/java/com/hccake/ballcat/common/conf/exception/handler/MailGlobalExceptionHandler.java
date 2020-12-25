package com.hccake.ballcat.common.conf.exception.handler;

import com.hccake.ballcat.common.conf.config.ExceptionHandleConfig;
import com.hccake.ballcat.common.conf.exception.domain.ExceptionMessage;
import com.hccake.ballcat.common.conf.exception.domain.ExceptionNoticeResponse;
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

	public MailGlobalExceptionHandler(ExceptionHandleConfig config, MailSender sender, String applicationName) {
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
