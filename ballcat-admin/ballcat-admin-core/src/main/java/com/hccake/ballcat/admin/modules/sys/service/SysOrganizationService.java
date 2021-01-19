package com.hccake.ballcat.admin.modules.sys.service;

import com.hccake.ballcat.admin.modules.sys.model.dto.SysOrganizationDTO;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysOrganization;
import com.hccake.ballcat.admin.modules.sys.model.vo.SysOrganizationTree;
import com.hccake.extend.mybatis.plus.service.ExtendService;

import java.util.List;

/**
 * 组织架构
 *
 * @author hccake 2020-09-23 12:09:43
 */
public interface SysOrganizationService extends ExtendService<SysOrganization> {

	/**
	 * 返回组织架构的树形结构
	 * @return OrganizationTree
	 */
	List<SysOrganizationTree> tree();

	/**
	 * 创建一个新的组织机构
	 * @param sysOrganizationDTO 组织机构DTO
	 * @return boolean 创建成功/失败
	 */
	boolean create(SysOrganizationDTO sysOrganizationDTO);

	/**
	 * 更新一个已有的组织机构
	 * @param sysOrganizationDTO 组织机构DTO
	 * @return boolean 更新成功/失败
	 */
	boolean update(SysOrganizationDTO sysOrganizationDTO);

	/**
	 * 根据组织ID 查询除该组织下的所有儿子组织
	 * @param organizationId 组织机构ID
	 * @return List<SysOrganization> 该组织的儿子组织
	 */
	List<SysOrganization> selectSubOrganization(Integer organizationId);

	/**
	 * 根据组织ID 查询除该组织下的所有孩子（子孙）组织
	 * @param organizationId 组织机构ID
	 * @return List<SysOrganization> 该组织的孩子组织
	 */
	List<SysOrganization> selectChildOrganization(Integer organizationId);

}