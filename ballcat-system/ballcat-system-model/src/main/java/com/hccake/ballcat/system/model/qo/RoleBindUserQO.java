package com.hccake.ballcat.system.model.qo;

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
	@Schema(title = "角色标识")
	private String roleCode;

	@Schema(title = "用户ID")
	private Integer userId;

	@Schema(title = "用户名")
	private String username;

	@Schema(title = "组织ID")
	private Integer organizationId;

}
