package com.hccake.ballcat.admin.modules.sys.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.hccake.ballcat.admin.modules.sys.mapper.SysUserRoleMapper;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysRole;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysUserRole;
import com.hccake.ballcat.admin.modules.sys.service.SysUserRoleService;
import com.hccake.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户角色关联关系表
 *
 * @author Hccake
 */
@Service
public class SysUserRoleServiceImpl extends ExtendServiceImpl<SysUserRoleMapper, SysUserRole>
		implements SysUserRoleService {

	/**
	 * 根据UserId删除该用户角色关联关系
	 * @param userId 用户ID
	 * @return boolean
	 */
	@Override
	public Boolean deleteByUserId(Integer userId) {
		return baseMapper.deleteByUserId(userId);
	}

	/**
	 * 插入用户角色关联关系
	 * @param userId 用户ID
	 * @param roleCodes 角色标识集合
	 * @return boolean
	 */
	@Override
	public Boolean insertUserRoles(Integer userId, List<String> roleCodes) {
		return baseMapper.insertUserRoles(userId, roleCodes);
	}

	/**
	 * 更新用户关联关系
	 * @param userId 用户ID
	 * @param roleCodes 角色标识集合
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean updateUserRoles(Integer userId, List<String> roleCodes) {
		// 先清空，后插入
		baseMapper.deleteByUserId(userId);
		if (CollectionUtil.isNotEmpty(roleCodes)) {
			baseMapper.insertUserRoles(userId, roleCodes);
		}
		return true;
	}

	/**
	 * 通过用户ID 获取用户所有角色ID
	 * @param userId 用户ID
	 * @return 用户拥有的角色集合
	 */
	@Override
	public List<SysRole> getRoles(Integer userId) {
		return baseMapper.getRoles(userId);
	}

}
