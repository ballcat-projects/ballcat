package com.hccake.extend.dingtalk;

import com.hccake.extend.dingtalk.message.DingTalkTextMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

/**
 * 钉钉机器人测试，由于需要真实的 webhook 地址和密钥，所以只在 ci 环境下开启
 *
 * @author lingting 2023-03-04 18:43
 */
@EnabledIfSystemProperty(named = "test.dingtalk.enabled", matches = "true")
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
