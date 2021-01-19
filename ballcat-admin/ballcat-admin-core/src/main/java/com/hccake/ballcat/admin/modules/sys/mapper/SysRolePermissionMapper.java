package com.hccake.ballcat.admin.modules.sys.mapper;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysRolePermission;
import com.hccake.extend.mybatis.plus.mapper.ExtendMapper;

import java.io.Serializable;

/**
 * <p>
 * 角色菜单表 Mapper 接口
 * </p>
 *
 * @author hccake
 * @since 2017-10-29
 */
public interface SysRolePermissionMapper extends ExtendMapper<SysRolePermission> {

	/**
	 * 根据权限ID删除角色权限关联关系
	 * @param permissionId 权限ID
	 */
	default void deleteByPermissionId(Serializable permissionId) {
		this.delete(Wrappers.<SysRolePermission>query().lambda().eq(SysRolePermission::getPermissionId, permissionId));
	}

	/**
	 * 根据角色标识删除角色权限关联关系
	 * @param roleCode 角色标识
	 */
	default void deleteByRoleCode(String roleCode) {
		this.delete(Wrappers.<SysRolePermission>query().lambda().eq(SysRolePermission::getRoleCode, roleCode));
	}

}
