/*
 * Copyright 2023 the original author or authors.
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
package org.ballcat.autoconfigure.web.exception.handler;

import org.ballcat.autoconfigure.web.exception.ExceptionHandleProperties;
import org.ballcat.autoconfigure.web.exception.domain.ExceptionMessage;
import org.ballcat.autoconfigure.web.exception.domain.ExceptionNoticeResponse;
import org.ballcat.dingtalk.DingTalkResponse;
import org.ballcat.dingtalk.DingTalkSender;
import org.ballcat.dingtalk.message.DingTalkTextMessage;
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
