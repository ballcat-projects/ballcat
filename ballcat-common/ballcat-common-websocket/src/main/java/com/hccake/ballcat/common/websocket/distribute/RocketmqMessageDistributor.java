package com.hccake.ballcat.common.websocket.distribute;

import com.hccake.ballcat.common.util.JsonUtils;
import com.hccake.ballcat.common.websocket.exception.ErrorJsonMessageException;
import com.hccake.ballcat.common.websocket.session.WebSocketSessionStore;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Value;

import java.nio.charset.StandardCharsets;

/**
 * MQ发送消息，接收到消息时进行推送, 广播模式
 * <p>
 *
 * @author liu_yx 2022年06月30日 14:10:10
 * @since 0.9.0
 */
@Slf4j
@RocketMQMessageListener(
		consumerGroup = "${spring.application.name:default-ballcat-application}-${spring.profiles.active:dev}",
		topic = "${spring.application.name:default-ballcat-application}-${spring.profiles.active:dev}",
		selectorExpression = "${ballcat.websocket.mq.tag}", messageModel = MessageModel.BROADCASTING)
public class RocketmqMessageDistributor extends AbstractMessageDistributor implements RocketMQListener<MessageExt> {

	@Value("${spring.application.name}")
	private String appName;

	@Value("${ballcat.websocket.mq.tag}")
	private String tag;

	private final RocketMQTemplate template;

	public RocketmqMessageDistributor(WebSocketSessionStore webSocketSessionStore, RocketMQTemplate template) {
		super(webSocketSessionStore);
		this.template = template;
	}

	/**
	 * 消息分发
	 * @param messageDO 发送的消息
	 */
	@Override
	public void distribute(MessageDO messageDO) {
		log.info("the send message body is [{}]", messageDO);
		String destination = this.appName + ":" + this.tag;
		SendResult sendResult = this.template.sendAndReceive(destination, JsonUtils.toJson(messageDO),
				SendResult.class);
		if (log.isDebugEnabled()) {
			log.debug("send message to `{}` finished. result:{}", destination, sendResult);
		}
	}

	/**
	 * 消息消费
	 * @param message 接收的消息
	 */
	@Override
	public void onMessage(MessageExt message) {
		String body = new String(message.getBody(), StandardCharsets.UTF_8);
		MessageDO event = JsonUtils.toObj(body, MessageDO.class);
		log.info("the content is [{}]", event);
		try {
			this.doSend(event);
		}
		catch (Exception e) {
			log.error("MQ消费信息处理异常: {}", e.getMessage(), e);
			throw new ErrorJsonMessageException("MQ消费信息处理异常, " + e.getMessage());
		}
	}

}
