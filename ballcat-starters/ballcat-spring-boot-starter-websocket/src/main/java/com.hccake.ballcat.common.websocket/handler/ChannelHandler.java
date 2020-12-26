package com.hccake.ballcat.common.websocket.handler;

import com.hccake.ballcat.common.websocket.function.SessionFunction;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * channel handler
 *
 * @author Yakir
 */
public interface ChannelHandler<K, V extends WebSocketSession> {

	/**
	 * 根据key 获取管道
	 * @param key
	 * @return
	 */
	V getChannelByKey(K key);

	/**
	 * 设置管道
	 * @param key
	 * @param v
	 * @return
	 */
	void putChannel(K key, V v);

	/**
	 * 删除管道
	 * @param key
	 */
	void removeChannel(K key);

	/**
	 * 得到管道列表
	 * @return
	 */
	List<V> getChannels();

	/**
	 * 判断是否打开
	 * @param v
	 * @return
	 */
	boolean isOpen(V v);

}
