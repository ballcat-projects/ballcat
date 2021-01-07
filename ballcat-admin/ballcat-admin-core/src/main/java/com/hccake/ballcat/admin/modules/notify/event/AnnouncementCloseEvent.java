package com.hccake.ballcat.admin.modules.notify.event;

import io.swagger.annotations.ApiModelProperty;
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
	@ApiModelProperty(value = "公告ID")
	private final Long id;

}
