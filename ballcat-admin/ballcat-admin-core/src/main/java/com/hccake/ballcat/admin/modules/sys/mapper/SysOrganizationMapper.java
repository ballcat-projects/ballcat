package com.hccake.ballcat.admin.modules.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysOrganization;
import org.apache.ibatis.annotations.Param;

/**
 * 组织架构
 *
 * @author hccake 2020-09-23 12:09:43
 */
public interface SysOrganizationMapper extends BaseMapper<SysOrganization> {

	/**
	 * 跟随父节点移动子节点
	 * @param originHierarchy 原始父级层级
	 * @param targetHierarchy 移动后的父级层级
	 * @param depthDiff 移动的深度差
	 */
	void followMoveChildNode(@Param("originHierarchy") String originHierarchy,
			@Param("targetHierarchy") String targetHierarchy, @Param("depthDiff") int depthDiff);

}