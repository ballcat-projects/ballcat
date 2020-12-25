package com.hccake.ballcat.admin.modules.notify.model.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author Hccake 2020/12/23
 * @version 1.0
 */
@Data
@Accessors(chain = true)
public class NotifyInfo {

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
	 * @see com.hccake.ballcat.admin.constants.NotifyRecipientFilterType
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
	 * @see com.hccake.ballcat.admin.constants.NotifyChannel
	 */
	@ApiModelProperty(value = "接收方式")
	private List<Integer> receiveMode;

}
