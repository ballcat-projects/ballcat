package com.hccake.ballcat.notify.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.hccake.ballcat.notify.enums.NotifyChannelEnum;
import com.hccake.ballcat.notify.enums.NotifyRecipientFilterTypeEnum;
import com.hccake.ballcat.notify.enums.AnnouncementStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 公告信息
 *
 * @author hccake 2020-12-15 17:01:15
 */
@Data
@Schema(title = "公告信息分页VO")
public class AnnouncementPageVO {

	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
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
	 * 接收人筛选方式，1：全部 2：用户角色 3：组织机构 4：用户类型 5：自定义用户
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

	/**
	 * 创建人ID
	 */
	@Schema(title = "创建人ID")
	private Integer createBy;

	/**
	 * 创建人名称
	 */
	@Schema(title = "创建人名称")
	private String createUsername;

	/**
	 * 创建时间
	 */
	@Schema(title = "创建时间")
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	@Schema(title = "更新时间")
	private LocalDateTime updateTime;

}