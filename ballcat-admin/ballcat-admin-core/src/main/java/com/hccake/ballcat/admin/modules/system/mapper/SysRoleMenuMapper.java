package com.hccake.ballcat.admin.modules.system.mapper;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hccake.ballcat.admin.modules.system.model.entity.SysRoleMenu;
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
public interface SysRoleMenuMapper extends ExtendMapper<SysRoleMenu> {

	/**
	 * 根据权限ID删除角色权限关联关系
	 * @param menuId 权限ID
	 */
	default void deleteByMenuId(Serializable menuId) {
		this.delete(Wrappers.<SysRoleMenu>query().lambda().eq(SysRoleMenu::getMenuId, menuId));
	}

	/**
	 * 根据角色标识删除角色权限关联关系
	 * @param roleCode 角色标识
	 */
	default void deleteByRoleCode(String roleCode) {
		this.delete(Wrappers.<SysRoleMenu>query().lambda().eq(SysRoleMenu::getRoleCode, roleCode));
	}

}
