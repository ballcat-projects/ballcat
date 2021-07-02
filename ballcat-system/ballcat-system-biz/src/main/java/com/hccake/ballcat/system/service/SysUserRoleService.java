package com.hccake.ballcat.system.service;

import com.hccake.ballcat.system.model.entity.SysRole;
import com.hccake.ballcat.system.model.entity.SysUserRole;
import com.hccake.ballcat.system.model.qo.RoleBindUserQO;
import com.hccake.ballcat.system.model.vo.RoleBindUserVO;
import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
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
	boolean deleteByUserId(Integer userId);

	/**
	 * 更新用户关联关系
	 * @param userId 用户ID
	 * @param roleCodes 角色标识集合
	 * @return boolean
	 */
	boolean updateUserRoles(Integer userId, List<String> roleCodes);

	/**
	 * 添加用户角色关联关系
	 * @param userId 用户ID
	 * @param roleCodes 角色标识集合
	 * @return 插入是否成功
	 */
	boolean addUserRoles(Integer userId, List<String> roleCodes);

	/**
	 * 通过用户ID，查询角色列表
	 * @param userId 用户ID
	 * @return List<SysRole>
	 */
	List<SysRole> listRoles(Integer userId);

	/**
	 * 通过角色标识，查询用户列表
	 * @param pageParam 分页参数
	 * @param roleCode 角色标识
	 * @return PageResult<RoleBindUserVO> 角色授权的用户列表
	 */
	PageResult<RoleBindUserVO> queryUserPageByRoleCode(PageParam pageParam, RoleBindUserQO roleCode);

	/**
	 * 解绑角色和用户关系
	 * @param userId 用户ID
	 * @param roleCode 角色标识
	 * @return 解绑成功：true
	 */
	boolean unbindRoleUser(Integer userId, String roleCode);

}
