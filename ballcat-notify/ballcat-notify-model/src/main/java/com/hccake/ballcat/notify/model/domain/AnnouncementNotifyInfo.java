package com.hccake.ballcat.notify.model.domain;

import com.hccake.ballcat.notify.enums.NotifyChannelEnum;
import com.hccake.ballcat.notify.enums.NotifyRecipientFilterTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 公告通知信息
 *
 * @author Hccake 2020/12/23
 * @version 1.0
 */
@Data
@Accessors(chain = true)
public class AnnouncementNotifyInfo implements NotifyInfo {

	/**
	 * ID
	 */
	@ApiModelProperty(value = "公告ID")
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
	 * 接收人筛选方式
	 * @see NotifyRecipientFilterTypeEnum
	 */
	@ApiModelProperty(value = "接收人筛选方式")
	private Integer recipientFilterType;

	/**
	 * 对应接收人筛选方式的条件信息
	 */
	@ApiModelProperty(value = "对应接收人筛选方式的条件信息")
	private List<Object> recipientFilterCondition;

	/**
	 * 接收方式，值与通知渠道一一对应
	 * @see NotifyChannelEnum
	 */
	@ApiModelProperty(value = "接收方式")
	private List<Integer> receiveMode;

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

}
