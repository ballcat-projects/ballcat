package com.hccake.ballcat.notify.event;

import com.hccake.ballcat.notify.model.domain.NotifyInfo;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 通知发布事件
 *
 * @author Hccake 2020/12/17
 * @version 1.0
 */
@Getter
public class NotifyPublishEvent extends ApplicationEvent {

	/**
	 * 通知信息
	 */
	private final NotifyInfo notifyInfo;

	public NotifyPublishEvent(NotifyInfo notifyInfo) {
		super(notifyInfo);
		this.notifyInfo = notifyInfo;
	}

}
