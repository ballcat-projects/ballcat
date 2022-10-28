package com.hccake.ballcat.autoconfigure.web.exception.handler;

import com.hccake.ballcat.autoconfigure.web.exception.ExceptionHandleProperties;
import com.hccake.ballcat.autoconfigure.web.exception.domain.ExceptionMessage;
import com.hccake.ballcat.autoconfigure.web.exception.domain.ExceptionNoticeResponse;
import com.hccake.ballcat.common.mail.sender.MailSender;
import com.hccake.extend.dingtalk.DingTalkSender;
import com.hccake.extend.dingtalk.message.DingTalkTextMessage;
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
