package com.hccake.ballcat.admin.modules.sys.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 角色菜单表
 * </p>
 *
 * @author
 * @since 2017-10-29
 */
@Data
@TableName("sys_role_permission")
@ApiModel(value = "角色菜单")
@EqualsAndHashCode(callSuper = true)
public class SysRolePermission extends Model<SysRolePermission> {

	private static final long serialVersionUID = 1L;

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
