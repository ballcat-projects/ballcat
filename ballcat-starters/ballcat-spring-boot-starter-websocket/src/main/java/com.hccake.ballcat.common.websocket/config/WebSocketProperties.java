package com.hccake.ballcat.common.websocket.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * websocket props
 *
 * @author Yakir
 */
@Data
@ConfigurationProperties("ws")
public class WebSocketProperties {

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
	 * 会话属性 获取唯一标识key
	 */
	private String attrKey = "uid";

	/**
	 * 是否支持部分消息
	 */
	private Boolean supportPartialMessages = false;

	/**
	 * 会话存储类型 local内存 redis 缓存 sql数据库
	 */
	private String type = "local";

	/**
	 * 服务器类型 搭配cache使用 {@link RequestUpgradeStrategy} 执行升级到websocket的具体策略 目前 standard
	 * jetty
	 */
	private String serverType = "standard";

}
