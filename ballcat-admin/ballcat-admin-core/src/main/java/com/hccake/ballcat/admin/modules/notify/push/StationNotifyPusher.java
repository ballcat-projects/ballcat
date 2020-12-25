package com.hccake.ballcat.admin.modules.notify.push;

import com.hccake.ballcat.admin.constants.NotifyChannel;
import com.hccake.ballcat.admin.modules.notify.model.domain.NotifyInfo;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysUser;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 消息通知站内推送
 *
 * @author Hccake 2020/12/21
 * @version 1.0
 */
@Component
public class StationNotifyPusher implements NotifyPusher {

	/**
	 * 当前发布者对应的接收方式
	 * @see com.hccake.ballcat.admin.constants.NotifyChannel
	 * @return 推送方式
	 */
	@Override
	public Integer notifyChannel() {
		return NotifyChannel.STATION.getValue();
	}

	@Override
	public void push(NotifyInfo notifyInfo, List<SysUser> userList) {
		// TODO websocket 推送
		System.out.println("站内推送");
	}

}
