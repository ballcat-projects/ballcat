package com.hccake.ballcat.admin.modules.system.converter;

import com.hccake.ballcat.admin.modules.system.model.dto.SysOrganizationDTO;
import com.hccake.ballcat.admin.modules.system.model.entity.SysOrganization;
import com.hccake.ballcat.admin.modules.system.model.vo.SysOrganizationTree;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author Hccake 2020/9/23
 * @version 1.0
 */
@Mapper
public interface SysOrganizationConverter {

	SysOrganizationConverter INSTANCE = Mappers.getMapper(SysOrganizationConverter.class);

	/**
	 * 实体转树节点实体
	 * @param sysOrganization 组织架构实体
	 * @return 组织架构树类型
	 */
	SysOrganizationTree poToTree(SysOrganization sysOrganization);

	/**
	 * 新增传输对象转实体
	 * @param sysOrganizationDTO 组织机构DTO
	 * @return 组织机构实体
	 */
	SysOrganization dtoToPo(SysOrganizationDTO sysOrganizationDTO);

}
