package com.hccake.extend.dingtalk;

import com.hccake.ballcat.common.queue.WaitQueue;
import com.hccake.extend.dingtalk.message.DingTalkMessage;
import lombok.SneakyThrows;

import java.util.Collection;

/**
 * 订单负载均衡消息发送
 *
 * @author lingting 2020/6/10 21:25
 */
public class DingTalkBalancedSender {

	private final WaitQueue<DingTalkSender> queue = new WaitQueue<>();

	public DingTalkBalancedSender add(DingTalkSender... senders) {
		for (DingTalkSender sender : senders) {
			queue.add(sender);
		}
		return this;
	}

	public DingTalkBalancedSender addAll(Collection<DingTalkSender> collection) {
		queue.addAll(collection);
		return this;
	}

	@SneakyThrows
	protected DingTalkSender sender() {
		return queue.poll();
	}

	public void send(DingTalkMessage message) {
		DingTalkSender sender = sender();
		try {
			sender.sendMessage(message);
		}
		finally {
			queue.add(sender);
		}
	}

}
