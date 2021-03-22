package com.hccake.ballcat.admin.modules.sys.converter;

import com.hccake.ballcat.admin.modules.sys.model.dto.SysRoleUpdateDTO;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysRole;
import com.hccake.ballcat.admin.modules.sys.model.vo.SysRolePageVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 系统角色POJO转换器
 *
 * @author Hccake 2020/7/6
 * @version 1.0
 */
@Mapper
public interface SysRoleConverter {

	SysRoleConverter INSTANCE = Mappers.getMapper(SysRoleConverter.class);

	/**
	 * PO 转 PageVO
	 * @param sysRole 系统角色
	 * @return SysRolePageVO 系统角色分页VO
	 */
	SysRolePageVO poToPageVo(SysRole sysRole);

	/**
	 * 修改DTO 转 PO
	 * @param dto 修改DTO
	 * @return SysRole PO
	 */
	SysRole dtoToPo(SysRoleUpdateDTO dto);

}
