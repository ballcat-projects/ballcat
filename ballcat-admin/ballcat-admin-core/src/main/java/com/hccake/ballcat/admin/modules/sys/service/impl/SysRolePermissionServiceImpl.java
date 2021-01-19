
package com.hccake.ballcat.admin.modules.sys.service.impl;

import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.hccake.ballcat.admin.modules.sys.mapper.SysRolePermissionMapper;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysRolePermission;
import com.hccake.ballcat.admin.modules.sys.service.SysRolePermissionService;
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
		// 1、先删除旧数据
		baseMapper.deleteByRoleCode(roleCode);
		if (permissionIds == null || permissionIds.length == 0) {
			return Boolean.TRUE;
		}

		// 2、再批量插入新数据
		List<SysRolePermission> list = Arrays.stream(permissionIds).map(id -> new SysRolePermission(roleCode, id))
				.collect(Collectors.toList());
		int i = baseMapper.insertBatchSomeColumn(list);
		return SqlHelper.retBool(i);
	}

	/**
	 * 根据权限ID删除角色权限关联数据
	 * @param permissionId 权限ID
	 */
	@Override
	public void deleteByPermissionId(Serializable permissionId) {
		baseMapper.deleteByPermissionId(permissionId);
	}

	/**
	 * 根据角色标识删除角色权限关联关系
	 * @param roleCode 角色标识
	 */
	@Override
	public void deleteByRoleCode(String roleCode) {
		baseMapper.deleteByRoleCode(roleCode);
	}

}
