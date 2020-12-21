package com.hccake.ballcat.admin.modules.sys.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysUser;
import com.hccake.ballcat.admin.modules.sys.model.vo.SysUserVO;
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
	 * 分页查询
	 * @param page 分页对象
	 * @param wrapper 查询条件wrapper
	 * @return Page<SysUserVO>
	 */
	IPage<SysUserVO> selectPageVo(IPage<?> page, @Param(Constants.WRAPPER) QueryWrapper<SysUser> wrapper);

	/**
	 * 根据RoleCode 查询对应用户
	 * @param roleCodes 角色标识
	 * @return List<SysUser> 该角色标识对应的用户列表
	 */
	List<SysUser> selectUsersByRoleCodes(@Param("roleCodes") List<String> roleCodes);

	/**
	 * 返回用户的select数据 name=> username value => userId
	 * @param userTypes 用户类型
	 * @return List<SelectData>
	 */
	List<SelectData<?>> getSelectData(@Param("userTypes") List<Integer> userTypes);

}
