package com.hccake.ballcat.common.mail.listener;

import com.hccake.ballcat.common.mail.event.MailSendEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;

/**
 * 邮件发送事件监听器 可在这处理邮件记录
 *
 * @author Hccake
 * @version 1.0
 * @date 2020/2/27 18:11
 */
@Slf4j
public class MailSentListener implements ApplicationListener<MailSendEvent> {

	@Override
	public void onApplicationEvent(MailSendEvent mailSendEvent) {
		log.info(mailSendEvent.toString());
	}

}
