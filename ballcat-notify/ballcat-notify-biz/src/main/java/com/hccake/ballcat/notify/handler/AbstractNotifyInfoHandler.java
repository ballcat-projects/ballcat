package com.hccake.ballcat.notify.handler;

import cn.hutool.core.collection.CollUtil;
import com.hccake.ballcat.common.util.JsonUtils;
import com.hccake.ballcat.common.websocket.distribute.MessageDO;
import com.hccake.ballcat.common.websocket.distribute.MessageDistributor;
import com.hccake.ballcat.common.websocket.message.AbstractJsonWebSocketMessage;
import com.hccake.ballcat.notify.model.domain.NotifyInfo;
import com.hccake.ballcat.system.model.entity.SysUser;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 公告通知
 *
 * @author huyuanzhi
 * @param <T>event消息对象
 * @param <M> websocket发送消息对象
 */
public abstract class AbstractNotifyInfoHandler<T extends NotifyInfo, M extends AbstractJsonWebSocketMessage>
		implements NotifyInfoHandler<T> {

	@Autowired
	private MessageDistributor messageDistributor;

	protected final Class<T> clz;

	@SuppressWarnings("unchecked")
	protected AbstractNotifyInfoHandler() {
		Type superClass = getClass().getGenericSuperclass();
		ParameterizedType type = (ParameterizedType) superClass;
		clz = (Class<T>) type.getActualTypeArguments()[0];
	}

	@Override
	public void handle(List<SysUser> userList, T notifyInfo) {
		M message = createMessage(notifyInfo);
		String msg = JsonUtils.toJson(message);
		List<Object> sessionKeys = userList.stream().map(SysUser::getUserId).collect(Collectors.toList());
		persistMessage(userList, notifyInfo);
		MessageDO messageDO = new MessageDO().setMessageText(msg).setSessionKeys(sessionKeys)
				.setNeedBroadcast(CollUtil.isEmpty(sessionKeys));
		messageDistributor.distribute(messageDO);
	}

	@Override
	public Class<T> getNotifyClass() {
		return this.clz;
	}

	/**
	 * 持久化通知
	 * @param userList 通知用户列表
	 * @param notifyInfo 消息内容
	 */
	protected abstract void persistMessage(List<SysUser> userList, T notifyInfo);

	/**
	 * 产生推送消息
	 * @param notifyInfo 消息内容
	 * @return 分发消息
	 */
	protected abstract M createMessage(T notifyInfo);

}
