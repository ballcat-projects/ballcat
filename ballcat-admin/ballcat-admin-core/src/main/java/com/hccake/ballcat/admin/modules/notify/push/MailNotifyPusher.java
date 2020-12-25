package com.hccake.ballcat.admin.modules.notify.push;

import com.hccake.ballcat.admin.constants.NotifyChannel;
import com.hccake.ballcat.admin.modules.notify.model.domain.NotifyInfo;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysUser;
import com.hccake.ballcat.common.mail.model.MailDetails;
import com.hccake.ballcat.common.mail.sender.MailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

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
	 * @see com.hccake.ballcat.admin.constants.NotifyChannel
	 * @return 推送方式
	 */
	@Override
	public Integer notifyChannel() {
		return NotifyChannel.MAIL.getValue();
	}

	@Override
	public void push(NotifyInfo notifyInfo, List<SysUser> userList) {
		String[] emails = userList.stream().map(SysUser::getEmail).toArray(String[]::new);

		// 密送群发，不展示其他收件人
		MailDetails mailDetails = new MailDetails();
		mailDetails.setShowHtml(true);
		mailDetails.setSubject(notifyInfo.getTitle());
		mailDetails.setContent(notifyInfo.getContent());
		mailDetails.setBcc(emails);
		mailSender.sendMail(mailDetails);
	}

}
