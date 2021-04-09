package com.hccake.ballcat.admin.modules.sys.service;

import com.hccake.ballcat.admin.modules.sys.model.entity.SysRoleMenu;
import com.hccake.extend.mybatis.plus.service.ExtendService;

import java.io.Serializable;

/**
 * <p>
 * 角色菜单表 服务类
 * </p>
 *
 * @author hccake
 * @since 2017-10-29
 */
public interface SysRoleMenuService extends ExtendService<SysRoleMenu> {

	/**
	 * 更新角色菜单
	 * @param roleCode 角色
	 * @param menuIds 权限ID数组
	 * @return 更新角色权限关联关系是否成功
	 */
	Boolean saveRoleMenus(String roleCode, Integer[] menuIds);

	/**
	 * 根据权限ID删除角色权限关联数据
	 * @param menuId 权限ID
	 */
	void deleteByMenuId(Serializable menuId);

	/**
	 * 根据角色标识删除角色权限关联关系
	 * @param roleCode 角色标识
	 */
	void deleteByRoleCode(String roleCode);

}
