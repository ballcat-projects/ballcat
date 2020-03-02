package com.hccake.ballcat.admin.modules.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysRole;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysUserRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户角色表 Mapper 接口
 * </p>
 *
 * @author
 * @since 2017-10-29
 */
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {


	/**
	 * 通过用户ID，查询角色s
	 *
	 * @param userId
	 * @return
	 */
	List<SysRole> getRoles(Integer userId);

	/**
	 * 删除用户关联关系
	 * @param userId
	 * @return
	 */
	Boolean deleteByUserId(@Param("userId") Integer userId);

	/**
	 * 插入用户关联关系
	 * @param userId
	 * @param roleIds
	 * @return
	 */
    Boolean insertUserRoles(@Param("userId") Integer userId, @Param("roleIds") List<Integer> roleIds);
}
