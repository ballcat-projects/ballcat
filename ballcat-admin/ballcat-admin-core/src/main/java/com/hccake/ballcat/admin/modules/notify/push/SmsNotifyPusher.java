package com.hccake.ballcat.admin.modules.notify.push;

import com.hccake.ballcat.admin.constants.NotifyChannel;
import com.hccake.ballcat.admin.modules.notify.model.entity.Announcement;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysUser;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 短信通知发布
 *
 * @author Hccake 2020/12/21
 * @version 1.0
 */
@Component
public class SmsNotifyPusher implements NotifyPusher {

	/**
	 * 当前发布者对应的接收方式
	 * @return 推送方式
	 */
	@Override
	public Integer notifyChannel() {
		return NotifyChannel.SMS.getValue();
	}

	@Override
	public void push(Announcement announcement, List<SysUser> userList) {
		List<String> phoneList = userList.stream().map(SysUser::getPhone).collect(Collectors.toList());
		System.out.println("短信推送");
	}

}
