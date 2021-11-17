package com.hccake.ballcat.autoconfigure.websocket;

import com.hccake.ballcat.common.websocket.handler.ConcurrentWebSocketSessionOptions;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * websocket props
 *
 * @author Yakir
 */
@Data
@ConfigurationProperties(WebSocketProperties.PREFIX)
public class WebSocketProperties {

	public static final String PREFIX = "ballcat.websocket";

	/**
	 * 路径: 无参: /ws 有参: PathVariable: 单参: /ws/{test} 多参: /ws/{test1}/{test2} query:
	 * /ws?uid=1&name=test
	 *
	 */
	private String path = "/ws";

	/**
	 * 允许访问源
	 */
	private String allowOrigins = "*";

	/**
	 * 是否支持部分消息
	 */
	private boolean supportPartialMessages = false;

	/**
	 * 心跳处理
	 */
	private boolean heartbeat = true;

	/**
	 * 是否开启对session的映射记录
	 */
	private boolean mapSession = true;

	/**
	 * 多线程发送相关配置
	 */
	@NestedConfigurationProperty
	private ConcurrentWebSocketSessionOptions concurrent = new ConcurrentWebSocketSessionOptions();

	/**
	 * 消息分发器：local | redis，默认 local, 如果自定义的话，可以配置为其他任意值
	 */
	private String messageDistributor = MessageDistributorTypeConstants.LOCAL;

}
