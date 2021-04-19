package com.hccake.ballcat.admin.modules.system.mapper;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hccake.ballcat.admin.modules.system.model.entity.SysMenu;
import com.hccake.ballcat.admin.modules.system.model.qo.SysMenuQO;
import com.hccake.extend.mybatis.plus.conditions.query.LambdaQueryWrapperX;
import com.hccake.extend.mybatis.plus.mapper.ExtendMapper;
import com.hccake.extend.mybatis.plus.toolkit.WrappersX;

import java.io.Serializable;
import java.util.List;

/**
 * 菜单权限
 *
 * @author hccake 2021-04-01 22:08:13
 */
public interface SysMenuMapper extends ExtendMapper<SysMenu> {

	/**
	 * 查询权限集合，并按sort排序（升序）
	 * @param sysMenuQO 查询条件
	 * @return List<SysMenu>
	 */
	default List<SysMenu> listOrderBySort(SysMenuQO sysMenuQO) {
		LambdaQueryWrapperX<SysMenu> wrapper = WrappersX.lambdaQueryX(SysMenu.class)
				.likeIfPresent(SysMenu::getId, sysMenuQO.getId()).likeIfPresent(SysMenu::getTitle, sysMenuQO.getTitle())
				.likeIfPresent(SysMenu::getPermission, sysMenuQO.getPermission())
				.likeIfPresent(SysMenu::getPath, sysMenuQO.getPath());
		wrapper.orderByAsc(SysMenu::getSort);
		return this.selectList(wrapper);
	}

	/**
	 * 根据角色标识查询对应的菜单
	 * @param roleCode 角色标识
	 * @return List<SysMenu>
	 */
	List<SysMenu> listByRoleCode(String roleCode);

	/**
	 * 查询指定权限的下级权限总数
	 * @param id 权限ID
	 * @return 下级权限总数
	 */
	default Integer countSubMenu(Serializable id) {
		return this.selectCount(Wrappers.<SysMenu>query().lambda().eq(SysMenu::getParentId, id));
	}

}