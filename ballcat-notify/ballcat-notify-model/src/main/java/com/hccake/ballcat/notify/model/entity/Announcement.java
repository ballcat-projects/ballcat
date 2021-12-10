package com.hccake.ballcat.notify.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.hccake.ballcat.common.model.entity.LogicDeletedBaseEntity;
import com.hccake.ballcat.notify.enums.AnnouncementStatusEnum;
import com.hccake.ballcat.notify.enums.NotifyChannelEnum;
import com.hccake.ballcat.notify.enums.NotifyRecipientFilterTypeEnum;
import com.hccake.extend.mybatis.plus.alias.TableAlias;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 公告信息
 *
 * @author hccake 2020-12-15 17:01:15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableAlias(Announcement.TABLE_ALIAS)
@TableName(value = "notify_announcement", autoResultMap = true)
@Schema(title = "公告信息")
public class Announcement extends LogicDeletedBaseEntity {

	private static final long serialVersionUID = 1L;

	public static final String TABLE_ALIAS = "a";

	/**
	 * ID
	 */
	@TableId
	@Schema(title = "ID")
	private Long id;

	/**
	 * 标题
	 */
	@Schema(title = "标题")
	private String title;

	/**
	 * 内容
	 */
	@Schema(title = "内容")
	private String content;

	/**
	 * 接收人筛选方式
	 * @see NotifyRecipientFilterTypeEnum
	 */
	@Schema(title = "接收人筛选方式")
	private Integer recipientFilterType;

	/**
	 * 对应接收人筛选方式的条件信息，多个用逗号分割。如角色标识，组织ID，用户类型，用户ID等
	 */
	@TableField(typeHandler = JacksonTypeHandler.class)
	@Schema(title = "对应接收人筛选方式的条件信息。如角色标识，组织ID，用户类型，用户ID等")
	private List<Object> recipientFilterCondition;

	/**
	 * 接收方式，值与通知渠道一一对应
	 * @see NotifyChannelEnum
	 */
	@TableField(typeHandler = JacksonTypeHandler.class)
	@Schema(title = "接收方式")
	private List<Integer> receiveMode;

	/**
	 * 状态
	 * @see AnnouncementStatusEnum
	 */
	@Schema(title = "状态")
	private Integer status;

	/**
	 * 永久有效的
	 * @see com.hccake.ballcat.common.core.constant.enums.BooleanEnum
	 */
	@Schema(title = "永久有效的")
	private Integer immortal;

	/**
	 * 截止日期
	 */
	@Schema(title = "截止日期")
	private LocalDateTime deadline;

}
