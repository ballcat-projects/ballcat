package com.hccake.ballcat.system.service.impl;

import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.hccake.ballcat.system.mapper.SysMenuMapper;
import com.hccake.ballcat.system.model.entity.SysMenu;
import com.hccake.ballcat.system.model.qo.SysMenuQO;
import com.hccake.ballcat.system.service.SysMenuService;
import com.hccake.ballcat.system.service.SysRoleMenuService;
import com.hccake.ballcat.common.core.constant.GlobalConstants;
import com.hccake.ballcat.common.core.exception.BusinessException;
import com.hccake.ballcat.common.model.result.BaseResultCode;
import com.hccake.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * 菜单权限
 *
 * @author hccake 2021-04-06 17:59:51
 */
@Service
@RequiredArgsConstructor
public class SysMenuServiceImpl extends ExtendServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

	private final SysRoleMenuService sysRoleMenuService;

	/**
	 * 插入一条记录（选择字段，策略插入）
	 * @param sysMenu 实体对象
	 */
	@Override
	public boolean save(SysMenu sysMenu) {
		// 逻辑删除初始值
		sysMenu.setDeleted(GlobalConstants.NOT_DELETED_FLAG);
		return SqlHelper.retBool(baseMapper.insert(sysMenu));
	}

	/**
	 * 查询权限集合，并按sort排序（升序）
	 * @param sysMenuQO 查询条件
	 * @return List<SysMenu>
	 */
	@Override
	public List<SysMenu> listOrderBySort(SysMenuQO sysMenuQO) {
		return baseMapper.listOrderBySort(sysMenuQO);
	}

	/**
	 * 根据角色标识查询对应的菜单
	 * @param roleCode 角色标识
	 * @return List<SysMenu>
	 */
	@Override
	public List<SysMenu> listByRoleCode(String roleCode) {
		return baseMapper.listByRoleCode(roleCode);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean removeById(Serializable id) {
		// 查询当前权限是否有子权限
		Integer subMenu = baseMapper.countSubMenu(id);
		if (subMenu != null && subMenu > 0) {
			throw new BusinessException(BaseResultCode.LOGIC_CHECK_ERROR.getCode(), "菜单含有下级不能删除");
		}
		// 删除角色权限关联数据
		sysRoleMenuService.deleteByMenuId(id);
		// 删除当前菜单及其子菜单
		return SqlHelper.retBool(baseMapper.deleteById(id));
	}

}
