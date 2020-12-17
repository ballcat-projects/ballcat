package com.hccake.ballcat.admin.modules.notify.model.qo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 公告信息 查询对象
 *
 * @author hccake 2020-12-15 17:01:15
 */
@Data
@ApiModel(value = "公告信息查询对象")
public class AnnouncementQO {

	private static final long serialVersionUID = 1L;

	/**
	 * 标题
	 */
	@ApiModelProperty(value = "标题")
	private String title;

	/**
	 * 接收人筛选方式，1：全部 2：用户角色 3：组织机构 4：用户类型 5：自定义用户
	 */
	@ApiModelProperty(value = "接收人筛选方式，1：全部 2：用户角色 3：组织机构 4：用户类型 5：自定义用户")
	private Integer recipientFilterType;

	@ApiModelProperty(value = "状态")
	private Integer[] status;

}