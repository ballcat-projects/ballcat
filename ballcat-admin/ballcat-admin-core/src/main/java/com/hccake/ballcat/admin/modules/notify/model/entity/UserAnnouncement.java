package com.hccake.ballcat.admin.modules.notify.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("notify_user_announcement")
@ApiModel(value = "用户公告表")
public class UserAnnouncement {

	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@TableId
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
	@TableField(fill = FieldFill.INSERT)
	@ApiModelProperty(value = "拉取时间")
	private LocalDateTime createTime;

}
