package com.hccake.ballcat.admin.modules.sys.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 角色菜单表
 *
 * @author ballcat code generator
 * @date 2019-10-14 17:42:23
 */
@Data
@TableName("sys_role_permission")
@ApiModel(value = "角色菜单")
public class SysRolePermission {

	private static final long serialVersionUID = 1L;

	public SysRolePermission() {
	}

	public SysRolePermission(String roleCode, Integer permissionId) {
		this.roleCode = roleCode;
		this.permissionId = permissionId;
	}

	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * 角色 Code
	 */
	@ApiModelProperty(value = "角色 Code")
	private String roleCode;

	/**
	 * 权限ID
	 */
	@ApiModelProperty(value = "菜单id")
	private Integer permissionId;

}
