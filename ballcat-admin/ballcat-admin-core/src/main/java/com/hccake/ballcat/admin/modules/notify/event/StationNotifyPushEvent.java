package com.hccake.ballcat.admin.modules.notify.event;

import com.hccake.ballcat.admin.modules.notify.model.domain.NotifyInfo;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysUser;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * @author Hccake 2021/1/5
 * @version 1.0
 */
@Getter
@RequiredArgsConstructor
public class StationNotifyPushEvent {

	/**
	 * 通知信息
	 */
	private final NotifyInfo notifyInfo;

	/**
	 * 推送用户列表
	 */
	private final List<SysUser> userList;

}
