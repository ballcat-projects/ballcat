package com.hccake.ballcat.admin.modules.notify.event;

import com.hccake.ballcat.admin.modules.notify.model.entity.Announcement;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 公告发布事件
 *
 * @author Hccake 2020/12/17
 * @version 1.0
 */
@Getter
@RequiredArgsConstructor
public class AnnouncementPublishEvent {

	/**
	 * 公告
	 */
	private final Announcement announcement;

}
