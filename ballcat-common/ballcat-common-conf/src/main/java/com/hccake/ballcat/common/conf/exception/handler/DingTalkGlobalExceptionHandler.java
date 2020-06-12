package com.hccake.ballcat.common.conf.exception.handler;

import com.hccake.ballcat.common.conf.config.ExceptionHandleConfig;
import com.hccake.ballcat.common.conf.exception.domain.ExceptionMessage;
import com.hccake.ballcat.common.conf.exception.domain.ExceptionNoticeResponse;
import com.hccake.extend.ding.talk.DingTalkResponse;
import com.hccake.extend.ding.talk.DingTalkSender;
import com.hccake.extend.ding.talk.message.DingTalkTextMessage;
import lombok.extern.slf4j.Slf4j;

/**
 * 钉钉消息通知
 *
 * @author lingting  2020/6/12 0:25
 */
@Slf4j
public class DingTalkGlobalExceptionHandler extends AbstractNoticeGlobalExceptionHandler {
	private final DingTalkSender sender;

	public DingTalkGlobalExceptionHandler(ExceptionHandleConfig config, DingTalkSender sender, String applicationName) {
		super(config, applicationName);
		this.sender = sender;
	}

	@Override
	public ExceptionNoticeResponse send(ExceptionMessage sendMessage) {
		DingTalkResponse response = sender.sendMessage(new DingTalkTextMessage().setContent(sendMessage.toString()));
		return new ExceptionNoticeResponse()
				.setErrMsg(response.getResponse())
				.setSuccess(response.isSuccess());
	}
}
