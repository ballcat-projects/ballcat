package com.hccake.ballcat.common.websocket.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

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

}
