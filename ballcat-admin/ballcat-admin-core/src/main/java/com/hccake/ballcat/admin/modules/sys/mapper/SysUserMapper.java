package com.hccake.ballcat.admin.modules.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysUser;
import com.hccake.ballcat.common.core.vo.SelectData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统用户表
 *
 * @author Hccake
 */
public interface SysUserMapper extends BaseMapper<SysUser> {

	/**
	 * 根据RoleCode 查询对应用户
	 * @param roleCode
	 * @return
	 */
	List<SysUser> selectUsersByRoleCode(String roleCode);

	/**
	 * 返回用户的select数据 name=> username value => userId
	 * @param type
	 * @return List<SelectData>
	 */
	List<SelectData<?>> getSelectData(@Param("type") Integer type);

}
