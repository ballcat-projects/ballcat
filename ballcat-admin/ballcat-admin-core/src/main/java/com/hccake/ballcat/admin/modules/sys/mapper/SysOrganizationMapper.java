package com.hccake.ballcat.admin.modules.sys.mapper;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysOrganization;
import com.hccake.extend.mybatis.plus.mapper.ExtendMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 组织架构
 *
 * @author hccake 2020-09-23 12:09:43
 */
public interface SysOrganizationMapper extends ExtendMapper<SysOrganization> {

	/**
	 * 根据组织ID 查询除该组织下的所有儿子组织
	 * @param organizationId 组织机构ID
	 * @return List<SysOrganization> 该组织的儿子组织
	 */
	default List<SysOrganization> listSubOrganization(Integer organizationId) {
		return this
				.selectList(Wrappers.<SysOrganization>lambdaQuery().eq(SysOrganization::getParentId, organizationId));
	}

	/**
	 * 跟随父节点移动子节点
	 * @param originHierarchy 原始父级层级
	 * @param targetHierarchy 移动后的父级层级
	 * @param depthDiff 移动的深度差
	 */
	void followMoveChildNode(@Param("originHierarchy") String originHierarchy,
			@Param("targetHierarchy") String targetHierarchy, @Param("depthDiff") int depthDiff);

	/**
	 * 根据组织机构Id，查询该组织下的所有子部门
	 * @param organizationId 组织机构ID
	 * @return 子部门集合
	 */
	List<SysOrganization> listChildOrganization(@Param("organizationId") Integer organizationId);

}