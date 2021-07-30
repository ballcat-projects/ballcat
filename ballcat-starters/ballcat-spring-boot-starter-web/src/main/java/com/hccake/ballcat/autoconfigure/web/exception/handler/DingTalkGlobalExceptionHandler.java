package com.hccake.ballcat.autoconfigure.web.exception.handler;

import com.hccake.ballcat.autoconfigure.web.exception.ExceptionHandleProperties;
import com.hccake.ballcat.autoconfigure.web.exception.domain.ExceptionMessage;
import com.hccake.ballcat.autoconfigure.web.exception.domain.ExceptionNoticeResponse;
import com.hccake.extend.dingtalk.DingTalkResponse;
import com.hccake.extend.dingtalk.DingTalkSender;
import com.hccake.extend.dingtalk.message.DingTalkTextMessage;
import lombok.extern.slf4j.Slf4j;

/**
 * 钉钉消息通知
 *
 * @author lingting 2020/6/12 0:25
 */
@Slf4j
public class DingTalkGlobalExceptionHandler extends AbstractNoticeGlobalExceptionHandler {

	private final DingTalkSender sender;

	public DingTalkGlobalExceptionHandler(ExceptionHandleProperties config, DingTalkSender sender,
			String applicationName) {
		super(config, applicationName);
		this.sender = sender;
	}

	@Override
	public ExceptionNoticeResponse send(ExceptionMessage sendMessage) {
		DingTalkResponse response = sender
				.sendMessage(new DingTalkTextMessage().setContent(sendMessage.toString()).atAll());
		return new ExceptionNoticeResponse().setErrMsg(response.getResponse()).setSuccess(response.isSuccess());
	}

}
