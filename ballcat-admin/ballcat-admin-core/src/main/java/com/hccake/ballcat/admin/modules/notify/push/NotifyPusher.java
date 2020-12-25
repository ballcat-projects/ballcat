package com.hccake.ballcat.admin.modules.notify.push;

import com.hccake.ballcat.admin.modules.notify.model.domain.NotifyInfo;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysUser;

import java.util.List;

/**
 * 通知发布者
 *
 * @author Hccake 2020/12/21
 * @version 1.0
 */
public interface NotifyPusher {

	/**
	 * 当前发布者对应的推送渠道
	 * @see com.hccake.ballcat.admin.constants.NotifyChannel
	 * @return 推送方式对应的标识
	 */
	Integer notifyChannel();

	/**
	 * 推送通知
	 * @param notifyInfo 通知信息
	 * @param userList 用户列表
	 */
	void push(NotifyInfo notifyInfo, List<SysUser> userList);

}
