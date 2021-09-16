package com.hccake.ballcat.system.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.hccake.ballcat.common.core.exception.BusinessException;
import com.hccake.ballcat.common.i18n.I18nMessage;
import com.hccake.ballcat.common.i18n.I18nMessageCreateEvent;
import com.hccake.ballcat.common.model.result.BaseResultCode;
import com.hccake.ballcat.system.converter.SysMenuConverter;
import com.hccake.ballcat.system.mapper.SysMenuMapper;
import com.hccake.ballcat.system.model.dto.SysMenuCreateDTO;
import com.hccake.ballcat.system.model.dto.SysMenuUpdateDTO;
import com.hccake.ballcat.system.model.entity.SysMenu;
import com.hccake.ballcat.system.model.qo.SysMenuQO;
import com.hccake.ballcat.system.service.SysMenuService;
import com.hccake.ballcat.system.service.SysRoleMenuService;
import com.hccake.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * 菜单权限
 *
 * @author hccake 2021-04-06 17:59:51
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysMenuServiceImpl extends ExtendServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

	private final SysRoleMenuService sysRoleMenuService;

	private final ApplicationEventPublisher eventPublisher;

	/**
	 * 插入一条记录（选择字段，策略插入）
	 * @param sysMenu 实体对象
	 */
	@Override
	public boolean save(SysMenu sysMenu) {
		Integer menuId = sysMenu.getId();
		SysMenu existingMenu = baseMapper.selectById(menuId);
		if (existingMenu != null) {
			String errorMessage = String.format("ID [%s] 已被菜单 [%s] 使用，请更换其他菜单ID", menuId, existingMenu.getTitle());
			throw new BusinessException(BaseResultCode.LOGIC_CHECK_ERROR.getCode(), errorMessage);
		}
		return SqlHelper.retBool(baseMapper.insert(sysMenu));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean create(SysMenuCreateDTO sysMenuCreateDTO) {

		SysMenu sysMenu = SysMenuConverter.INSTANCE.createDtoToPo(sysMenuCreateDTO);
		Integer menuId = sysMenu.getId();
		SysMenu existingMenu = baseMapper.selectById(menuId);
		if (existingMenu != null) {
			String errorMessage = String.format("ID [%s] 已被菜单 [%s] 使用，请更换其他菜单ID", menuId, existingMenu.getTitle());
			throw new BusinessException(BaseResultCode.LOGIC_CHECK_ERROR.getCode(), errorMessage);
		}

		boolean saveSuccess = SqlHelper.retBool(baseMapper.insert(sysMenu));
		Assert.isTrue(saveSuccess, () -> {
			log.error("[create] 创建菜单失败，sysMenuCreateDTO: {}", sysMenuCreateDTO);
			return new BusinessException(BaseResultCode.UPDATE_DATABASE_ERROR.getCode(), "创建菜单失败");
		});

		// 多语言保存事件发布
		List<I18nMessage> i18nMessages = sysMenuCreateDTO.getI18nMessages();
		if (CollectionUtil.isNotEmpty(i18nMessages)) {
			eventPublisher.publishEvent(new I18nMessageCreateEvent(i18nMessages));
		}

		return saveSuccess;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean removeById(Serializable id) {
		// 查询当前权限是否有子权限
		Long subMenu = baseMapper.countSubMenu(id);
		if (subMenu != null && subMenu > 0) {
			throw new BusinessException(BaseResultCode.LOGIC_CHECK_ERROR.getCode(), "菜单含有下级不能删除");
		}
		// 删除角色权限关联数据
		sysRoleMenuService.deleteByMenuId(id);
		// 删除当前菜单及其子菜单
		return SqlHelper.retBool(baseMapper.deleteById(id));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void update(SysMenuUpdateDTO sysMenuUpdateDTO) {
		// 原来的菜单 Id
		Integer originalId = sysMenuUpdateDTO.getOriginalId();
		SysMenu sysMenu = SysMenuConverter.INSTANCE.updateDtoToPo(sysMenuUpdateDTO);

		// 更新菜单信息
		boolean updateSuccess = baseMapper.updateMenuAndId(originalId, sysMenu);
		Assert.isTrue(updateSuccess, () -> {
			log.error("[update] 更新菜单权限时，sql 执行异常，originalId：{}，sysMenu：{}", originalId, sysMenu);
			return new BusinessException(BaseResultCode.UPDATE_DATABASE_ERROR.getCode(), "更新菜单权限时，sql 执行异常");
		});

		// 如果未修改过 菜单id 直接返回
		Integer menuId = sysMenuUpdateDTO.getId();
		if (originalId.equals(menuId)) {
			return;
		}

		// 修改过菜单id，则需要对应修改角色菜单的关联表信息，这里不需要 check，因为可能没有授权过该菜单，所以返回值为 0
		sysRoleMenuService.updateMenuId(originalId, menuId);
		// 更新子菜单的 parentId
		baseMapper.updateParentId(originalId, menuId);
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

}
