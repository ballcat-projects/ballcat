package com.hccake.ballcat.system.model.qo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 角色绑定用户查询对象
 *
 * @author Hccake
 */
@Data
@ApiModel(value = "角色绑定用户查询对象")
public class RoleBindUserQO {

	@NotNull(message = "角色标识不能为空！")
	@ApiModelProperty(value = "角色标识")
	private String roleCode;

	@ApiModelProperty(value = "用户ID")
	private Integer userId;

	@ApiModelProperty(value = "用户名")
	private String username;

	@ApiModelProperty(value = "组织ID")
	private Integer organizationId;

}
