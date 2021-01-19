package com.hccake.ballcat.admin.modules.sys.service;

import com.hccake.ballcat.admin.modules.sys.model.entity.SysRole;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysUserRole;
import com.hccake.extend.mybatis.plus.service.ExtendService;

import java.util.List;

/**
 * @author Hccake
 *
 * 用户角色关联表
 */
public interface SysUserRoleService extends ExtendService<SysUserRole> {

	/**
	 * 删除用户的角色
	 * @param userId 用户ID
	 * @return 删除是否程
	 */
	Boolean deleteByUserId(Integer userId);

	/**
	 * 插入用户角色关联关系
	 * @param userId 用户ID
	 * @param roleCodes 角色标识集合
	 * @return 插入是否成功
	 */
	Boolean insertUserRoles(Integer userId, List<String> roleCodes);

	/**
	 * 更新用户关联关系
	 * @param userId 用户ID
	 * @param roleCodes 角色标识集合
	 * @return boolean
	 */
	boolean updateUserRoles(Integer userId, List<String> roleCodes);

	/**
	 * 通过用户ID，查询角色列表
	 * @param userId 用户ID
	 * @return List<SysRole>
	 */
	List<SysRole> listRoles(Integer userId);

}
