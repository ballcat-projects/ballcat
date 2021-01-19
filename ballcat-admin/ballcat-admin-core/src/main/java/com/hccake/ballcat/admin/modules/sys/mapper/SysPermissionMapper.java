package com.hccake.ballcat.admin.modules.sys.mapper;

import com.hccake.ballcat.admin.modules.sys.model.entity.SysPermission;
import com.hccake.ballcat.admin.modules.sys.model.vo.PermissionVO;
import com.hccake.extend.mybatis.plus.mapper.ExtendMapper;

import java.util.List;

/**
 * 菜单权限表 Mapper 接口
 *
 * @author hccake
 */
public interface SysPermissionMapper extends ExtendMapper<SysPermission> {

	/**
	 * 通过角色ID查询权限
	 * @param roleCode 角色ID
	 * @return 指定角色拥有的权限列表
	 */
	List<PermissionVO> listPermissionVOsByRoleCode(String roleCode);

}
