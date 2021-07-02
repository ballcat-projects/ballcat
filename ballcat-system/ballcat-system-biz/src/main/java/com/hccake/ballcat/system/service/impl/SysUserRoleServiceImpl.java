package com.hccake.ballcat.system.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.hccake.ballcat.system.mapper.SysUserRoleMapper;
import com.hccake.ballcat.system.model.entity.SysRole;
import com.hccake.ballcat.system.model.entity.SysUserRole;
import com.hccake.ballcat.system.model.qo.RoleBindUserQO;
import com.hccake.ballcat.system.model.vo.RoleBindUserVO;
import com.hccake.ballcat.system.service.SysUserRoleService;
import com.hccake.ballcat.common.core.exception.BusinessException;
import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.ballcat.common.model.result.BaseResultCode;
import com.hccake.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户角色关联关系表
 *
 * @author Hccake
 */
@Slf4j
@Service
public class SysUserRoleServiceImpl extends ExtendServiceImpl<SysUserRoleMapper, SysUserRole>
		implements SysUserRoleService {

	/**
	 * 根据UserId删除该用户角色关联关系
	 * @param userId 用户ID
	 * @return boolean
	 */
	@Override
	public boolean deleteByUserId(Integer userId) {
		return baseMapper.deleteByUserId(userId);
	}

	/**
	 * 更新用户关联关系
	 * @param userId 用户ID
	 * @param roleCodes 角色标识集合
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean updateUserRoles(@NonNull Integer userId, @NonNull List<String> roleCodes) {
		// 是否存在用户角色绑定关系，存在则先清空
		boolean existsRoleBind = baseMapper.existsRoleBind(userId, null);
		if (existsRoleBind) {
			boolean deleteSuccess = baseMapper.deleteByUserId(userId);
			Assert.isTrue(deleteSuccess, () -> {
				log.error("[updateUserRoles] 删除用户角色关联关系失败，userId：{}，roleCodes：{}", userId, roleCodes);
				return new BusinessException(BaseResultCode.UPDATE_DATABASE_ERROR.getCode(), "删除用户角色关联关系失败");
			});
		}

		// 没有的新授权的角色直接返回
		if (CollectionUtil.isNotEmpty(roleCodes)) {
			return true;
		}

		// 保存新的用户角色关联关系
		return addUserRoles(userId, roleCodes);
	}

	/**
	 * 插入用户角色关联关系
	 * @param userId 用户ID
	 * @param roleCodes 角色标识集合
	 * @return boolean
	 */
	@Override
	public boolean addUserRoles(@NonNull Integer userId, @NonNull List<String> roleCodes) {
		List<SysUserRole> list = prodSysUserRoles(userId, roleCodes);
		// 批量插入
		boolean insertSuccess = SqlHelper.retBool(baseMapper.insertBatchSomeColumn(list));
		Assert.isTrue(insertSuccess, () -> {
			log.error("[addUserRoles] 插入用户角色关联关系失败，userId：{}，roleCodes：{}", userId, roleCodes);
			return new BusinessException(BaseResultCode.UPDATE_DATABASE_ERROR.getCode(), "插入用户角色关联关系失败");
		});
		return insertSuccess;
	}

	/**
	 * 根据用户ID 和 角色Code 生成SysUserRole实体集合
	 * @param userId 用户ID
	 * @param roleCodes 角色标识集合
	 * @return List<SysUserRole>
	 */
	private List<SysUserRole> prodSysUserRoles(Integer userId, List<String> roleCodes) {
		// 转换为 SysUserRole 实体集合
		List<SysUserRole> list = new ArrayList<>();
		for (String roleCode : roleCodes) {
			SysUserRole sysUserRole = new SysUserRole();
			sysUserRole.setUserId(userId);
			sysUserRole.setRoleCode(roleCode);
			list.add(sysUserRole);
		}
		return list;
	}

	/**
	 * 通过用户ID 获取用户所有角色ID
	 * @param userId 用户ID
	 * @return 用户拥有的角色集合
	 */
	@Override
	public List<SysRole> listRoles(Integer userId) {
		return baseMapper.listRoleByUserId(userId);
	}

	/**
	 * 通过角色标识，查询用户列表
	 * @param pageParam 分页参数
	 * @param roleBindUserQO 查询条件
	 * @return PageResult<RoleBindUserVO> 角色授权的用户列表
	 */
	@Override
	public PageResult<RoleBindUserVO> queryUserPageByRoleCode(PageParam pageParam, RoleBindUserQO roleBindUserQO) {
		return baseMapper.queryUserPageByRoleCode(pageParam, roleBindUserQO);
	}

	/**
	 * 解绑角色和用户关系
	 * @param userId 用户ID
	 * @param roleCode 角色标识
	 * @return 解绑成功：true
	 */
	@Override
	public boolean unbindRoleUser(Integer userId, String roleCode) {
		// 不存在则不需要进行删除，直接返回true
		return !baseMapper.existsRoleBind(userId, roleCode) || baseMapper.deleteUserRole(userId, roleCode);
	}

}
