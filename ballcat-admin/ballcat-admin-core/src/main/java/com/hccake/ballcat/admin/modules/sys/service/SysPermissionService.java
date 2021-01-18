package com.hccake.ballcat.admin.modules.sys.service;

import com.hccake.ballcat.admin.modules.sys.model.entity.SysPermission;
import com.hccake.ballcat.admin.modules.sys.model.vo.PermissionVO;
import com.hccake.extend.mybatis.plus.service.ExtendService;

import java.util.List;

/**
 * <p>
 * 菜单权限表 服务类
 * </p>
 *
 * @author hccake
 * @since 2017-10-29
 */
public interface SysPermissionService extends ExtendService<SysPermission> {

	/**
	 * 通过角色编号查询URL 权限
	 * @param roleCode 角色Code
	 * @return 菜单列表
	 */
	List<PermissionVO> findPermissionVOsByRoleCode(String roleCode);

	/**
	 * 级联删除菜单
	 * @param id 菜单ID
	 * @return 成功、失败
	 */
	boolean removePermissionById(Integer id);

	/**
	 * 更新菜单信息
	 * @param sysPermission 菜单信息
	 * @return 成功、失败
	 */
	Boolean updatePermissionById(SysPermission sysPermission);

}
