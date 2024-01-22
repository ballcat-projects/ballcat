/*
 * Copyright 2023-2024 the original author or authors.
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

package org.ballcat.web.exception.notice;

import lombok.extern.slf4j.Slf4j;
import org.ballcat.dingtalk.DingTalkResponse;
import org.ballcat.dingtalk.DingTalkSender;
import org.ballcat.dingtalk.message.DingTalkTextMessage;
import org.ballcat.web.exception.domain.ExceptionMessage;

/**
 * 钉钉消息通知
 *
 * @author lingting 2020/6/12 0:25
 * @author Hccake
 */
@Slf4j
public class DingTalkExceptionNotifier implements ExceptionNotifier {

	private final Boolean atAll;

	private final DingTalkSender sender;

	public DingTalkExceptionNotifier(DingTalkSender sender, boolean atAll) {
		this.sender = sender;
		this.atAll = atAll;
	}

	@Override
	public ExceptionNoticeResponse notify(ExceptionMessage sendMessage) {
		DingTalkTextMessage message = new DingTalkTextMessage();
		message.setContent(sendMessage.toString());
		if (Boolean.TRUE.equals(this.atAll)) {
			message.atAll();
		}

		DingTalkResponse response = this.sender.sendMessage(message);
		return new ExceptionNoticeResponse().setErrMsg(response.getResponse()).setSuccess(response.isSuccess());
	}

}
