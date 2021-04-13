package com.hccake.ballcat.admin.modules.system.converter;

import com.hccake.ballcat.admin.modules.system.model.entity.SysMenu;
import com.hccake.ballcat.admin.modules.system.model.vo.SysMenuRouterVO;
import com.hccake.ballcat.admin.modules.system.model.vo.SysMenuPageVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 菜单权限模型转换器
 *
 * @author hccake 2021-04-06 17:59:51
 */
@Mapper
public interface SysMenuConverter {

	SysMenuConverter INSTANCE = Mappers.getMapper(SysMenuConverter.class);

	/**
	 * PO 转 PageVO
	 * @param sysMenu 菜单权限实体
	 * @return SysMenuPageVO 菜单权限PageVO
	 */
	SysMenuPageVO poToPageVo(SysMenu sysMenu);

	/**
	 * PO 转 VO
	 * @param sysMenu 菜单权限实体
	 * @return SysMenuVO
	 */
	SysMenuRouterVO poToRouterVo(SysMenu sysMenu);

}
