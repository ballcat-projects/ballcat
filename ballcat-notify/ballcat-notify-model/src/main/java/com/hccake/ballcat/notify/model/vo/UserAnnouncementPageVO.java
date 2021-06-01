package com.hccake.ballcat.notify.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户公告表
 *
 * @author hccake 2020-12-25 08:04:53
 */
@Data
@ApiModel(value = "用户公告分页VO")
public class UserAnnouncementPageVO {

	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@ApiModelProperty(value = "ID")
	private Long id;

	/**
	 * 公告id
	 */
	@ApiModelProperty(value = "公告id")
	private Long announcementId;

	/**
	 * 用户ID
	 */
	@ApiModelProperty(value = "用户ID")
	private Integer userId;

	/**
	 * 状态，已读(1)|未读(0)
	 */
	@ApiModelProperty(value = "状态，已读(1)|未读(0)")
	private Integer state;

	/**
	 * 阅读时间
	 */
	@ApiModelProperty(value = "阅读时间")
	private LocalDateTime readTime;

	/**
	 * 拉取时间
	 */
	@ApiModelProperty(value = "拉取时间")
	private LocalDateTime createTime;

}