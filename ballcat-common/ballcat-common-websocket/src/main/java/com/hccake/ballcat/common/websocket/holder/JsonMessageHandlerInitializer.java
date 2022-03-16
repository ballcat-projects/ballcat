package com.hccake.ballcat.common.websocket.holder;

import com.hccake.ballcat.common.websocket.handler.JsonMessageHandler;
import com.hccake.ballcat.common.websocket.message.JsonWebSocketMessage;
import lombok.RequiredArgsConstructor;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * <p>
 * JsonMessageHandler 初始化器
 * <p/>
 * 将所有的 jsonMessageHandler 收集到 JsonMessageHandlerHolder 中
 *
 * @author Hccake
 */
@RequiredArgsConstructor
public class JsonMessageHandlerInitializer {

	private final List<JsonMessageHandler<JsonWebSocketMessage>> jsonMessageHandlerList;

	@PostConstruct
	public void initJsonMessageHandlerHolder() {
		for (JsonMessageHandler<JsonWebSocketMessage> jsonMessageHandler : jsonMessageHandlerList) {
			JsonMessageHandlerHolder.addHandler(jsonMessageHandler);
		}
	}

}