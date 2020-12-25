package com.hccake.ballcat.admin.modules.notify.model.qo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户公告表 查询对象
 *
 * @author hccake 2020-12-25 08:04:53
 */
@Data
@ApiModel(value = "用户公告表查询对象")
public class UserAnnouncementQO {

	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@ApiModelProperty(value = "ID")
	private Long id;

}