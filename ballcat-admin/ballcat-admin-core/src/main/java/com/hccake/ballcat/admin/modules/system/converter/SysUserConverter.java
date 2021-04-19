package com.hccake.ballcat.admin.modules.system.converter;

import com.hccake.ballcat.admin.modules.system.model.dto.SysUserDTO;
import com.hccake.ballcat.admin.modules.system.model.entity.SysUser;
import com.hccake.ballcat.admin.modules.system.model.vo.SysUserInfo;
import com.hccake.ballcat.admin.modules.system.model.vo.SysUserPageVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/9/17 15:26
 */
@Mapper
public interface SysUserConverter {

	SysUserConverter INSTANCE = Mappers.getMapper(SysUserConverter.class);

	/**
	 * 转换DTO 为 PO
	 * @param sysUserDTO 系统用户DTO
	 * @return SysUser 系统用户
	 */
	@Mapping(target = "password", ignore = true)
	SysUser dtoToPo(SysUserDTO sysUserDTO);

	/**
	 * PO 转 PageVO
	 * @param sysUser 系统用户
	 * @return SysUserPageVO 系统用户PageVO
	 */
	SysUserPageVO poToPageVo(SysUser sysUser);

	/**
	 * PO 转 Info
	 * @param sysUser 系统用户
	 * @return SysUserInfo 用户信息
	 */
	SysUserInfo poToInfo(SysUser sysUser);

}
