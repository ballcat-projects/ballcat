package com.hccake.ballcat.notify.model.qo;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

/**
 * 公告信息 查询对象
 *
 * @author hccake 2020-12-15 17:01:15
 */
@Data
@Schema(title = "公告信息查询对象")
public class AnnouncementQO {

	private static final long serialVersionUID = 1L;

	/**
	 * 标题
	 */
	@Schema(title = "标题")
	private String title;

	/**
	 * 接收人筛选方式
	 * @see NotifyRecipientFilterTypeEnum
	 */
	@Schema(title = "接收人筛选方式")
	private Integer recipientFilterType;

	@Schema(title = "状态")
	private Integer[] status;

}