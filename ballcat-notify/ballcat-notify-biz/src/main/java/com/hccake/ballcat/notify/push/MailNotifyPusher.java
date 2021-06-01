package com.hccake.ballcat.notify.push;

import cn.hutool.core.util.StrUtil;
import com.hccake.ballcat.common.mail.model.MailDetails;
import com.hccake.ballcat.common.mail.sender.MailSender;
import com.hccake.ballcat.notify.enums.NotifyChannelEnum;
import com.hccake.ballcat.notify.model.domain.NotifyInfo;
import com.hccake.ballcat.system.model.entity.SysUser;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * 通知邮件发布
 *
 * @author Hccake 2020/12/21
 * @version 1.0
 */
@RequiredArgsConstructor
public class MailNotifyPusher implements NotifyPusher {

	private final MailSender mailSender;

	/**
	 * 当前发布者的推送方式
	 * @see NotifyChannelEnum
	 * @return 推送方式
	 */
	@Override
	public Integer notifyChannel() {
		return NotifyChannelEnum.MAIL.getValue();
	}

	@Override
	public void push(NotifyInfo notifyInfo, List<SysUser> userList) {
		String[] emails = userList.stream().map(SysUser::getEmail).filter(StrUtil::isNotBlank).toArray(String[]::new);

		// 密送群发，不展示其他收件人
		MailDetails mailDetails = new MailDetails();
		mailDetails.setShowHtml(true);
		mailDetails.setSubject(notifyInfo.getTitle());
		mailDetails.setContent(notifyInfo.getContent());
		mailDetails.setBcc(emails);
		mailSender.sendMail(mailDetails);
	}

}
