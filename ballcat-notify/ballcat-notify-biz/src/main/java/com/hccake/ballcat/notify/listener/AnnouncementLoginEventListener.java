package com.hccake.ballcat.notify.listener;

import com.hccake.ballcat.notify.enums.NotifyChannelEnum;
import com.hccake.ballcat.notify.model.entity.Announcement;
import com.hccake.ballcat.notify.model.entity.UserAnnouncement;
import com.hccake.ballcat.notify.recipient.RecipientHandler;
import com.hccake.ballcat.notify.service.AnnouncementService;
import com.hccake.ballcat.notify.service.UserAnnouncementService;
import com.hccake.ballcat.common.security.userdetails.User;
import com.hccake.ballcat.system.model.entity.SysUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Hccake 2020/12/23
 * @version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AnnouncementLoginEventListener {

	private final AnnouncementService announcementService;

	private final RecipientHandler recipientHandler;

	private final UserAnnouncementService userAnnouncementService;

	/**
	 * 登陆成功时间监听 用户未读公告生成
	 * @param event 登陆成功 event
	 */
	@EventListener(AuthenticationSuccessEvent.class)
	public void onAuthenticationSuccessEvent(AuthenticationSuccessEvent event) throws InterruptedException {

		AbstractAuthenticationToken source = (AbstractAuthenticationToken) event.getSource();
		Object details = source.getDetails();
		if (!(details instanceof HashMap)) {
			return;
		}
		// https://github.com/spring-projects-experimental/spring-authorization-server
		if ("password".equals(((HashMap) details).get("grant_type"))) {
			User user = (User) source.getPrincipal();
			SysUser sysUser = getSysUser(user);

			// 获取当前用户未拉取过的公告信息
			Integer userId = sysUser.getUserId();
			List<Announcement> announcements = announcementService.listUnPulled(userId);
			// 获取当前用户的各个过滤属性
			Map<Integer, Object> filterAttrs = recipientHandler.getFilterAttrs(sysUser);
			// 获取符合当前用户条件的，且接收类型包含站内的公告，保存其关联关系
			List<UserAnnouncement> userAnnouncements = announcements.stream()
					.filter(x -> x.getReceiveMode().contains(NotifyChannelEnum.STATION.getValue()))
					.filter(x -> filterMatched(x, filterAttrs)).map(Announcement::getId)
					.map(id -> userAnnouncementService.prodUserAnnouncement(userId, id)).collect(Collectors.toList());
			try {
				userAnnouncementService.saveBatch(userAnnouncements);
			}
			catch (Exception exception) {
				log.error("用户公告保存失败：[{}]", userAnnouncements, exception);
			}
		}
	}

	private SysUser getSysUser(User user) {
		SysUser sysUser = new SysUser();
		sysUser.setUserId(user.getUserId());
		sysUser.setUsername(user.getUsername());
		sysUser.setNickname(user.getNickname());
		sysUser.setAvatar(user.getAvatar());
		sysUser.setOrganizationId(user.getOrganizationId());
		sysUser.setType(user.getType());
		return sysUser;
	}

	private boolean filterMatched(Announcement announ, Map<Integer, Object> filterAttrs) {
		Integer type = announ.getRecipientFilterType();
		return recipientHandler.match(type, filterAttrs.get(type), announ.getRecipientFilterCondition());
	}

}
