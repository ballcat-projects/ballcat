package com.hccake.ballcat.admin.modules.sys.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysRole;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysUserRole;

import java.util.List;

/**
 * @author Hccake
 *
 * 用户角色关联表
 */
public interface SysUserRoleService extends IService<SysUserRole> {

	/**
	 * 删除用户的角色
	 * @param userId
	 * @return
	 */
	Boolean deleteByUserId(Integer userId);

	/**
	 * 插入用户角色关联关系
	 * @param userId
	 * @param roleIds
	 * @return
	 */
	Boolean insertUserRoles(Integer userId, List<Integer> roleIds);


	/**
	 * 更新用户关联关系
	 * @param userId
	 * @param roleIds
	 * @return boolean
	 */
	boolean updateUserRoles(Integer userId, List<Integer> roleIds);

	/**
	 * 通过用户ID，查询角色列表
	 *
	 * @param userId
	 * @return List<SysRole>
	 */
	List<SysRole> getRoles(Integer userId);
}
