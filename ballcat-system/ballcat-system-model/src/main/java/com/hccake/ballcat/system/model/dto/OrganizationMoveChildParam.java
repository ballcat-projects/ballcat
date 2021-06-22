package com.hccake.ballcat.system.model.dto;

import lombok.Data;

/**
 * 组织机构移动子节点时的参数封装对象
 *
 * @author hccake
 */
@Data
public class OrganizationMoveChildParam {

	/**
	 * 父级id
	 */
	private Integer parentId;

	/**
	 * 父级节点原始的层级信息
	 */
	private String originParentHierarchy;

	/**
	 * 父级节点原始的层级信息长度 + 1
	 */
	private int originParentHierarchyLengthPlusOne;

	/**
	 * 父级节点移动后的层级信息
	 */
	private String targetParentHierarchy;

	/**
	 * 移动前后的节点深度差
	 */
	private Integer depthDiff;

	/**
	 * 查询孙子节点的条件语句
	 */
	private String grandsonConditionalStatement;

}
