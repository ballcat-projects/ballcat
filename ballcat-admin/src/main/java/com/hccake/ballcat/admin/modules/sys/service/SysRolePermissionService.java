package com.hccake.ballcat.admin.modules.sys.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysRolePermission;

/**
 * <p>
 * 角色菜单表 服务类
 * </p>
 *
 * @author
 * @since 2017-10-29
 */
public interface SysRolePermissionService extends IService<SysRolePermission> {

	/**
	 * 更新角色菜单
	 *
	 * @param roleId  角色
	 * @param permissionIds 权限ID数组
	 * @return
	 */
	Boolean saveRolePermissions(Integer roleId, Integer[] permissionIds);
}
