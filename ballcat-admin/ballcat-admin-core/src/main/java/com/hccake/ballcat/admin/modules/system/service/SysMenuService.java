package com.hccake.ballcat.admin.modules.system.service;

import com.hccake.ballcat.admin.modules.system.model.entity.SysMenu;
import com.hccake.ballcat.admin.modules.system.model.qo.SysMenuQO;
import com.hccake.extend.mybatis.plus.service.ExtendService;

import java.util.List;

/**
 * 菜单权限
 *
 * @author hccake 2021-04-06 17:59:51
 */
public interface SysMenuService extends ExtendService<SysMenu> {

	/**
	 * 查询权限集合，并按sort排序（升序）
	 * @param sysMenuQO 查询条件
	 * @return List<SysMenu>
	 */
	List<SysMenu> listOrderBySort(SysMenuQO sysMenuQO);

	/**
	 * 根据角色标识查询对应的菜单
	 * @param roleCode 角色标识
	 * @return List<SysMenu>
	 */
	List<SysMenu> listByRoleCode(String roleCode);

}