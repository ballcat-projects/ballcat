package com.hccake.ballcat.admin.modules.notify.push;

import cn.hutool.core.collection.CollectionUtil;
import com.hccake.ballcat.admin.modules.notify.model.entity.Announcement;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysUser;
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
public class NotifyPushRunner {

	private final Map<Integer, NotifyPusher> notifyPusherMap = new LinkedHashMap<>();

	public NotifyPushRunner(List<NotifyPusher> notifyPusherList) {
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
	 * @param announcement 公告信息
	 * @param userList 用户列表
	 * @param receiveModes 接收模式（推送渠道）
	 */
	public void run(Announcement announcement, List<SysUser> userList, List<Integer> receiveModes) {
		for (Integer notifyChannel : receiveModes) {
			try {
				NotifyPusher notifyPusher = notifyPusherMap.get(notifyChannel);

				if (notifyPusher == null) {
					log.error("Unknown notify channel：[{}]，announcement id：[{}]", notifyChannel, announcement.getId());
				}
				else {
					notifyPusher.push(announcement, userList);
				}
			}
			catch (Exception e) {
				log.error("push notify error in channel：[{}]，announcement id：[{}]", notifyChannel,
						announcement.getId());
			}
		}
	}

}
