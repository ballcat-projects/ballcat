package com.hccake.extend.dingtalk;

import com.hccake.extend.dingtalk.message.DingTalkTextMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author lingting 2023-03-04 18:43
 */
class DingTalkTest {

	DingTalkSender sender;

	@BeforeEach
	void before() {
		sender = new DingTalkSender(System.getProperty("extend.dingtalk.webhook"));
		sender.setSecret(System.getProperty("extend.dingtalk.secret"));
	}

	@Test
	void send() {
		DingTalkTextMessage message = new DingTalkTextMessage();
		message.setContent("测试机器人消息通知");
		DingTalkResponse response = sender.sendMessage(message);
		Assertions.assertTrue(response.isSuccess());
	}

}
