package com.hccake.ballcat.admin.modules.sys.mapper;

import com.hccake.ballcat.admin.modules.sys.model.entity.SysRole;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysUserRole;
import com.hccake.extend.mybatis.plus.mapper.ExtendMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户角色表 Mapper 接口
 * </p>
 *
 * @author hccake
 * @since 2017-10-29
 */
public interface SysUserRoleMapper extends ExtendMapper<SysUserRole> {

	/**
	 * 通过用户ID，查询角色
	 * @param userId 用户ID
	 * @return 用户拥有的角色集合
	 */
	List<SysRole> getRoles(Integer userId);

	/**
	 * 删除用户关联关系
	 * @param userId 用户ID
	 * @return boolean 删除是否成功
	 */
	Boolean deleteByUserId(@Param("userId") Integer userId);

	/**
	 * 插入用户关联关系
	 * @param userId 用户ID
	 * @param roleCodes 角色标识集合
	 * @return boolean 插入是否成功
	 */
	Boolean insertUserRoles(@Param("userId") Integer userId, @Param("roleCodes") List<String> roleCodes);

}
