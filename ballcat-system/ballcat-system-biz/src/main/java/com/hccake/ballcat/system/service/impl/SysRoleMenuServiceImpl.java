
package com.hccake.ballcat.system.service.impl;

import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.hccake.ballcat.system.mapper.SysRoleMenuMapper;
import com.hccake.ballcat.system.model.entity.SysRoleMenu;
import com.hccake.ballcat.system.service.SysRoleMenuService;
import com.hccake.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色菜单表 服务实现类
 * </p>
 *
 * @author hccake
 */
@Service
public class SysRoleMenuServiceImpl extends ExtendServiceImpl<SysRoleMenuMapper, SysRoleMenu>
		implements SysRoleMenuService {

	/**
	 * @param roleCode 角色
	 * @param menuIds 权限ID集合
	 * @return boolean
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean saveRoleMenus(String roleCode, Integer[] menuIds) {
		// 1、先删除旧数据
		baseMapper.deleteByRoleCode(roleCode);
		if (menuIds == null || menuIds.length == 0) {
			return Boolean.TRUE;
		}

		// 2、再批量插入新数据
		List<SysRoleMenu> list = Arrays.stream(menuIds).map(menuId -> new SysRoleMenu(roleCode, menuId))
				.collect(Collectors.toList());
		int i = baseMapper.insertBatchSomeColumn(list);
		return SqlHelper.retBool(i);
	}

	/**
	 * 根据权限ID删除角色权限关联数据
	 * @param menuId 权限ID
	 */
	@Override
	public void deleteByMenuId(Serializable menuId) {
		baseMapper.deleteByMenuId(menuId);
	}

	/**
	 * 根据角色标识删除角色权限关联关系
	 * @param roleCode 角色标识
	 */
	@Override
	public void deleteByRoleCode(String roleCode) {
		baseMapper.deleteByRoleCode(roleCode);
	}

	/**
	 * 更新某个菜单的 id
	 * @param originalId 原菜单ID
	 * @param menuId 修改后的菜单Id
	 * @return 被更新的菜单数
	 */
	@Override
	public int updateMenuId(Integer originalId, Integer menuId) {
		return baseMapper.updateMenuId(originalId, menuId);
	}

}
