package com.hccake.ballcat.system.model.qo;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;
import org.springdoc.api.annotations.ParameterObject;

import javax.validation.constraints.NotNull;

/**
 * 角色绑定用户查询对象
 *
 * @author Hccake
 */
@Data
@Schema(title = "角色绑定用户查询对象")
@ParameterObject
public class RoleBindUserQO {

	@NotNull(message = "角色标识不能为空！")
	@Parameter(description = "角色标识")
	private String roleCode;

	@Parameter(description = "用户ID")
	private Integer userId;

	@Parameter(description = "用户名")
	private String username;

	@Parameter(description = "组织ID")
	private Integer organizationId;

}
