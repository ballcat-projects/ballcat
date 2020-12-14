package com.hccake.ballcat.common.websocket.handler;

import com.hccake.ballcat.common.websocket.function.SessionFunction;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.function.Consumer;

/**
 * 消息处理
 *
 * @author Yakir
 */
public interface MessageHandler<K, V extends WebSocketSession> {

	/**
	 * 广播消息
	 * @param msg
	 */
	<T extends WebSocketMessage> void broadCastMsg(T msg);

	/**
	 * 广播文本消息
	 * @param msg
	 */
	void broadTextCastMsg(String msg);

	/**
	 * 发送消息
	 * @param key
	 * @param msg
	 * @return
	 */
	<T extends WebSocketMessage> boolean sendMsg(K key, T msg);

	/**
	 * 发送文本消息
	 * @param key
	 * @param msg
	 * @return
	 */
	boolean sendTextMsg(K key, String msg);

	/**
	 * 发送消息 自定义执行逻辑
	 * @param key
	 * @param consumer
	 * @return
	 */
	boolean sendMsg(K key, SessionFunction<WebSocketSession, Boolean> consumer);

	/**
	 * 发送消息只消费
	 * @param key
	 * @param consumer
	 * @return
	 */
	boolean sendMsg(Integer key, Consumer<V> consumer);

}
