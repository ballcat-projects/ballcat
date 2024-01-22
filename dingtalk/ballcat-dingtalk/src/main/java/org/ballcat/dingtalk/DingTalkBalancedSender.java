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

import java.util.Collection;

import lombok.SneakyThrows;
import org.ballcat.common.queue.WaitQueue;
import org.ballcat.dingtalk.message.DingTalkMessage;

/**
 * 订单负载均衡消息发送
 *
 * @author lingting 2020/6/10 21:25
 */
public class DingTalkBalancedSender {

	private final WaitQueue<DingTalkSender> queue = new WaitQueue<>();

	public DingTalkBalancedSender add(DingTalkSender... senders) {
		for (DingTalkSender sender : senders) {
			this.queue.add(sender);
		}
		return this;
	}

	public DingTalkBalancedSender addAll(Collection<DingTalkSender> collection) {
		this.queue.addAll(collection);
		return this;
	}

	@SneakyThrows
	protected DingTalkSender sender() {
		return this.queue.poll();
	}

	public void send(DingTalkMessage message) {
		DingTalkSender sender = sender();
		try {
			sender.sendMessage(message);
		}
		finally {
			this.queue.add(sender);
		}
	}

}
