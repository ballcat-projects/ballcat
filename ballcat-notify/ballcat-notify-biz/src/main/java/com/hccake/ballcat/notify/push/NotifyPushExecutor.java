package com.hccake.ballcat.notify.push;

import cn.hutool.core.collection.CollectionUtil;
import com.hccake.ballcat.notify.model.domain.NotifyInfo;
import com.hccake.ballcat.notify.recipient.RecipientHandler;
import com.hccake.ballcat.system.model.entity.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 通知消息推送执行器
 *
 * @author Hccake 2020/12/21
 * @version 1.0
 */
@Slf4j
@Component
public class NotifyPushExecutor {

	private final RecipientHandler recipientHandler;

	private final Map<Integer, NotifyPusher> notifyPusherMap = new LinkedHashMap<>();

	public NotifyPushExecutor(RecipientHandler recipientHandler, List<NotifyPusher> notifyPusherList) {
		this.recipientHandler = recipientHandler;
		if (CollectionUtil.isNotEmpty(notifyPusherList)) {
			for (NotifyPusher notifyPusher : notifyPusherList) {
				this.addNotifyPusher(notifyPusher);
			}
		}
	}

	/**
	 * 添加通知推送者
	 * @param notifyPusher 通知推送者
	 */
	public void addNotifyPusher(NotifyPusher notifyPusher) {
		this.notifyPusherMap.put(notifyPusher.notifyChannel(), notifyPusher);
	}

	/**
	 * 执行通知推送
	 * @param notifyInfo 通知信息
	 *
	 */
	public void push(NotifyInfo notifyInfo) {
		// 获取通知接收人
		Integer recipientFilterType = notifyInfo.getRecipientFilterType();
		List<Object> recipientFilterCondition = notifyInfo.getRecipientFilterCondition();
		List<SysUser> userList = recipientHandler.query(recipientFilterType, recipientFilterCondition);

		// 执行推送
		// TODO 返回推送失败渠道信息，以便重试
		for (Integer notifyChannel : notifyInfo.getReceiveMode()) {
			try {
				NotifyPusher notifyPusher = notifyPusherMap.get(notifyChannel);

				if (notifyPusher == null) {
					log.error("Unknown notify channel：[{}]，notifyInfo title：[{}]", notifyChannel,
							notifyInfo.getTitle());
				}
				else {
					notifyPusher.push(notifyInfo, userList);
				}
			}
			catch (Exception e) {
				log.error("push notify error in channel：[{}]，notifyInfo title：[{}]", notifyChannel,
						notifyInfo.getTitle(), e);
			}
		}
	}

}
