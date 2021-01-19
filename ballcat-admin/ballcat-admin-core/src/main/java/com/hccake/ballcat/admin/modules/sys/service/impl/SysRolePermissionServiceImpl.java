
package com.hccake.ballcat.admin.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hccake.ballcat.admin.modules.sys.mapper.SysRolePermissionMapper;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysRolePermission;
import com.hccake.ballcat.admin.modules.sys.service.SysRolePermissionService;
import com.hccake.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class SysRolePermissionServiceImpl extends ExtendServiceImpl<SysRolePermissionMapper, SysRolePermission>
		implements SysRolePermissionService {

	/**
	 * @param roleCode 角色
	 * @param permissionIds 权限ID集合
	 * @return boolean
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean saveRolePermissions(String roleCode, Integer[] permissionIds) {
		this.remove(Wrappers.<SysRolePermission>query().lambda().eq(SysRolePermission::getRoleCode, roleCode));

		if (permissionIds == null || permissionIds.length == 0) {
			return Boolean.TRUE;
		}
		List<SysRolePermission> rolePermissionList = Arrays.stream(permissionIds).map(permissionId -> {
			SysRolePermission rolePermission = new SysRolePermission();
			rolePermission.setRoleCode(roleCode);
			rolePermission.setPermissionId(permissionId);
			return rolePermission;
		}).collect(Collectors.toList());

		return this.saveBatch(rolePermissionList);
	}

}
