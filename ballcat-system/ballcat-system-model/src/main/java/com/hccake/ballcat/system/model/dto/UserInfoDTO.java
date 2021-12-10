package com.hccake.ballcat.system.model.dto;

import com.hccake.ballcat.system.model.entity.SysMenu;
import com.hccake.ballcat.system.model.entity.SysRole;
import com.hccake.ballcat.system.model.entity.SysUser;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

import java.util.Collection;

/**
 * 用户信息
 *
 * @author Hccake
 */
@Data
@Schema(title = "用户信息")
public class UserInfoDTO {

	/**
	 * 用户基本信息
	 */
	@Schema(title = "用户基本信息")
	private SysUser sysUser;

	/**
	 * 权限标识集合
	 */
	@Schema(title = "权限标识集合")
	private Collection<String> permissions;

	/**
	 * 角色标识集合
	 */
	@Schema(title = "角色标识集合")
	private Collection<String> roleCodes;

	/**
	 * 菜单对象集合
	 */
	@Schema(title = "菜单对象集合")
	private Collection<SysMenu> menus;

	/**
	 * 角色对象集合
	 */
	@Schema(title = "角色对象集合")
	private Collection<SysRole> roles;

}
