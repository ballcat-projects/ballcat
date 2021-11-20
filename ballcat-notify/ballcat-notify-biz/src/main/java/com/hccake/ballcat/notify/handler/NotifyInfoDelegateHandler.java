package com.hccake.ballcat.notify.handler;

import com.hccake.ballcat.notify.model.domain.NotifyInfo;
import com.hccake.ballcat.system.model.entity.SysUser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息处理代理
 *
 * @author huyuanzhi
 * @param <T> 消息类型
 */
@Slf4j
@Component
@AllArgsConstructor
public class NotifyInfoDelegateHandler<T extends NotifyInfo> {

	private final List<NotifyInfoHandler<T>> notifyInfoHandlers;

	private Map<Class<?>, NotifyInfoHandler<T>> handlerMap;

	@PostConstruct
	public void init() {
		handlerMap = new HashMap<>(notifyInfoHandlers.size());
		for (NotifyInfoHandler<T> handler : notifyInfoHandlers) {
			handlerMap.put(handler.getNotifyClass(), handler);
		}
	}

	/**
	 * 代理方法
	 * @param userList 发送用户列表
	 * @param info 消息
	 */
	public void handle(List<SysUser> userList, T info) {
		Assert.notNull(info, "event message cant be null!");
		NotifyInfoHandler<T> notifyInfoHandler = handlerMap.get(info.getClass());
		if (notifyInfoHandler == null) {
			log.warn("no notifyHandler bean for class:{},please check!", info.getClass().getName());
			return;
		}
		notifyInfoHandler.handle(userList, info);
	}

}
