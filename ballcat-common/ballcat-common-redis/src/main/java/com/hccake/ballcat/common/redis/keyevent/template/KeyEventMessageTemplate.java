package com.hccake.ballcat.common.redis.keyevent.template;

import org.springframework.lang.Nullable;

/**
 * common key event message template
 *
 * @author lishangbu
 * @date 2023/1/12
 */
public interface KeyEventMessageTemplate {

	/**
	 * handle actual message with notified key
	 * @param key notified key from redis server
	 */
	void handleMessage(@Nullable String key);

	/**
	 * support this template or not
	 * @param key notified key from redis server
	 * @return {@code true} is support and {@code false} is not support
	 */
	boolean support(@Nullable String key);

}
