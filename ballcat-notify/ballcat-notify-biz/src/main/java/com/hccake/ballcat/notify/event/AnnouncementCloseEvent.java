package com.hccake.ballcat.notify.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 公告关闭事件
 *
 * @author Hccake 2021/1/7
 * @version 1.0
 */
@Getter
@ToString
@AllArgsConstructor
public class AnnouncementCloseEvent {

	/**
	 * ID
	 */
	private final Long id;

}
