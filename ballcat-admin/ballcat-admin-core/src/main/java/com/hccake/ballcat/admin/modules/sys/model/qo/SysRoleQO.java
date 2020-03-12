package com.hccake.ballcat.admin.modules.sys.model.qo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 角色查询对象
 * @author Hccake
 */
@Data
@ApiModel(value = "角色查询对象")
public class SysRoleQO {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "角色名称")
	private String name;

	@ApiModelProperty(value = "角色标识")
	private String code;
}
