package com.hccake.ballcat.admin.modules.notify.push;

import com.hccake.ballcat.admin.modules.notify.model.entity.Announcement;
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
	 * @return 推送方式对应的标识
	 */
	Integer notifyChannel();

	/**
	 * 推送通知
	 * @param announcement 公告
	 * @param userList 用户列表
	 */
	void push(Announcement announcement, List<SysUser> userList);

}
