package com.hccake.ballcat.admin.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 公告状态
 * @author Hccake 2020/12/17
 * @version 1.0
 */
@Getter
@AllArgsConstructor
public enum AnnouncementStatusEnum {

	/**
	 * 关闭的
	 */
	DISABLED(0),

	/**
	 * 开启的
	 */
	ENABLED(1),

	/**
	 * 待发布
	 */
	UNPUBLISHED(2);

	private final int value;

}
