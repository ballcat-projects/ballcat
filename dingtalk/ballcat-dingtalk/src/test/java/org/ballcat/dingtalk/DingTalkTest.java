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

package org.ballcat.dingtalk;

import org.ballcat.dingtalk.message.DingTalkTextMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.util.StringUtils;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 钉钉机器人测试，由于需要真实的 webhook 地址和密钥，所以只在 ci 环境下开启
 *
 * @author lingting 2023-03-04 18:43
 */
@EnabledIfSystemProperty(named = "test.dingtalk.enabled", matches = "true")
class DingTalkTest {

	String webhook = System.getProperty("extend.dingtalk.webhook");

	String secret = System.getProperty("extend.dingtalk.secret");

	DingTalkSender sender;

	@BeforeEach
	void before() {
		this.sender = new DingTalkSender(this.webhook).setSecret(this.secret);
	}

	@Test
	void send() {
		assertTrue(StringUtils.hasText(this.sender.getUrl()));
		assertTrue(StringUtils.hasText(this.sender.getSecret()));
		DingTalkTextMessage message = new DingTalkTextMessage();
		message.setContent("测试机器人消息通知");
		DingTalkResponse response = this.sender.sendMessage(message);
		assertTrue(response.isSuccess());
	}

}
