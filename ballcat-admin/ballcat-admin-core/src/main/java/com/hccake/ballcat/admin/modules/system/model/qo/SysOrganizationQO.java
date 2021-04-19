package com.hccake.ballcat.admin.modules.system.model.qo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 组织架构 查询对象
 *
 * @author hccake 2020-09-23 12:09:43
 */
@Data
@ApiModel(value = "组织架构查询对象")
public class SysOrganizationQO {

	private static final long serialVersionUID = 1L;

	/**
	 * 组织名称
	 */
	@ApiModelProperty(value = "组织名称")
	private String name;

}