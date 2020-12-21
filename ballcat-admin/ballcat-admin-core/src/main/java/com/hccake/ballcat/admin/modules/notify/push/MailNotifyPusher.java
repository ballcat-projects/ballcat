package com.hccake.ballcat.admin.modules.notify.push;

import com.hccake.ballcat.admin.constants.NotifyChannel;
import com.hccake.ballcat.admin.modules.notify.model.entity.Announcement;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysUser;
import com.hccake.ballcat.common.mail.sender.MailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 通知邮件发布
 *
 * @author Hccake 2020/12/21
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class MailNotifyPusher implements NotifyPusher {

	private final MailSender mailSender;

	/**
	 * 当前发布者的推送方式
	 * @return 推送方式
	 */
	@Override
	public Integer notifyChannel() {
		return NotifyChannel.MAIL.getValue();
	}

	@Override
	public void push(Announcement announcement, List<SysUser> userList) {
		List<String> emails = userList.stream().map(SysUser::getEmail).collect(Collectors.toList());
		mailSender.sendHtmlMail(announcement.getTitle(), announcement.getContent(), emails);
	}

}
