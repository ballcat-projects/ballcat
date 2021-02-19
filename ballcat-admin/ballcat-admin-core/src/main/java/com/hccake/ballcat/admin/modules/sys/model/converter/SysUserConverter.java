package com.hccake.ballcat.admin.modules.sys.model.converter;

import com.hccake.ballcat.admin.modules.sys.model.dto.SysUserDTO;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysUser;
import com.hccake.ballcat.admin.modules.sys.model.vo.SysUserVO;
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
	 * PO 转 DTO
	 * @param sysUser 系统用户
	 * @return SysUserVO 系统用户VO
	 */
	SysUserVO poToVo(SysUser sysUser);

}
