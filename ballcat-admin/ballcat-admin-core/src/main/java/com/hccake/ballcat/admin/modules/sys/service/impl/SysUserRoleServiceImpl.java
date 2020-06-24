package com.hccake.ballcat.admin.modules.sys.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysRole;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysUserRole;
import com.hccake.ballcat.admin.modules.sys.mapper.SysUserRoleMapper;
import com.hccake.ballcat.admin.modules.sys.service.SysUserRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户角色关联关系表
 *
 * @author Hccake
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService {

	/**
	 * 根据UserId删除该用户角色关联关系
	 * @param userId
	 * @return
	 */
	@Override
	public Boolean deleteByUserId(Integer userId) {
		return baseMapper.deleteByUserId(userId);
	}

	/**
	 * 插入用户角色关联关系
	 * @param userId
	 * @param roleIds
	 * @return
	 */
	@Override
	public Boolean insertUserRoles(Integer userId, List<Integer> roleIds) {
		return baseMapper.insertUserRoles(userId, roleIds);
	}

	/**
	 * 更新用户关联关系
	 * @param userId
	 * @param roleIds
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean updateUserRoles(Integer userId, List<Integer> roleIds) {
		// 先清空，后插入
		baseMapper.deleteByUserId(userId);
		if (CollectionUtil.isNotEmpty(roleIds)) {
			baseMapper.insertUserRoles(userId, roleIds);
		}
		return true;
	}

	/**
	 * 通过用户ID 获取用户所有角色ID
	 * @param userId
	 * @return
	 */
	@Override
	public List<SysRole> getRoles(Integer userId) {
		return baseMapper.getRoles(userId);
	}

}
