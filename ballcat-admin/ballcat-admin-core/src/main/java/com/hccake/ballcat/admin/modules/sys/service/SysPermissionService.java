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
	List<PermissionVO> listVOByRoleCode(String roleCode);

	/**
	 * 查询权限集合，并按sort排序（升序）
	 * @return List<SysPermission>
	 */
	List<SysPermission> listOrderBySort();

}
