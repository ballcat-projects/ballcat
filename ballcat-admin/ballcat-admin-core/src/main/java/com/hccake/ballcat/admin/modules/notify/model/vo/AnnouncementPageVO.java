package com.hccake.ballcat.admin.modules.notify.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 公告信息
 *
 * @author hccake 2020-12-15 17:01:15
 */
@Data
@ApiModel(value = "公告信息分页VO")
public class AnnouncementPageVO {

	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@ApiModelProperty(value = "ID")
	private Long id;

	/**
	 * 标题
	 */
	@ApiModelProperty(value = "标题")
	private String title;

	/**
	 * 内容
	 */
	@ApiModelProperty(value = "内容")
	private String content;

	/**
	 * 接收人筛选方式，1：全部 2：用户角色 3：组织机构 4：用户类型 5：自定义用户
	 * @see com.hccake.ballcat.admin.constants.NotifyRecipientFilterType
	 */
	@ApiModelProperty(value = "接收人筛选方式")
	private Integer recipientFilterType;

	/**
	 * 对应接收人筛选方式的条件信息，多个用逗号分割。如角色标识，组织ID，用户类型，用户ID等
	 */
	@ApiModelProperty(value = "对应接收人筛选方式的条件信息。如角色标识，组织ID，用户类型，用户ID等")
	@TableField(typeHandler = JacksonTypeHandler.class)
	private List<Object> recipientFilterCondition;

	/**
	 * 接收方式，值与通知渠道一一对应
	 * @see com.hccake.ballcat.admin.constants.NotifyChannel
	 */
	@ApiModelProperty(value = "接收方式")
	@TableField(typeHandler = JacksonTypeHandler.class)
	private List<Integer> receiveMode;

	/**
	 * 状态
	 * @see com.hccake.ballcat.admin.constants.AnnouncementStatusEnum
	 */
	@ApiModelProperty(value = "状态")
	private Integer status;

	/**
	 * 永久有效的
	 * @see com.hccake.ballcat.common.core.constant.enums.BooleanEnum
	 */
	@ApiModelProperty(value = "永久有效的")
	private Integer immortal;

	/**
	 * 截止日期
	 */
	@ApiModelProperty(value = "截止日期")
	private LocalDateTime deadline;

	/**
	 * 创建人ID
	 */
	@ApiModelProperty(value = "创建人ID")
	private Integer createBy;

	/**
	 * 创建人名称
	 */
	@ApiModelProperty(value = "创建人名称")
	private String createUsername;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	@ApiModelProperty(value = "更新时间")
	private LocalDateTime updateTime;

}