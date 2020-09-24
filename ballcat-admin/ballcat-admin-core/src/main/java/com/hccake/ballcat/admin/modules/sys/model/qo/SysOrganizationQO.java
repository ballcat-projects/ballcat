package com.hccake.ballcat.admin.modules.sys.model.qo;

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
	 * ID
	 */
	@ApiModelProperty(value = "ID")
	private Integer id;

}