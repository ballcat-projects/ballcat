package com.hccake.ballcat.system.model.vo;

import com.hccake.ballcat.common.util.tree.TreeNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 组织架构
 *
 * @author hccake 2020-09-23 12:09:43
 */
@Data
@ApiModel(value = "组织架构")
public class SysOrganizationTree implements TreeNode<Integer> {

	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@ApiModelProperty(value = "ID")
	private Integer id;

	/**
	 * 组织名称
	 */
	@ApiModelProperty(value = "组织名称")
	private String name;

	/**
	 * 父级ID
	 */
	@ApiModelProperty(value = "父级ID")
	private Integer parentId;

	/**
	 * 层级信息，从根节点到当前节点的最短路径，使用-分割节点ID
	 */
	@ApiModelProperty(value = "层级信息，从根节点到当前节点的最短路径，使用-分割节点ID")
	private String hierarchy;

	/**
	 * 当前节点深度
	 */
	@ApiModelProperty(value = "当前节点深度")
	private Integer depth;

	/**
	 * 排序字段，由小到大
	 */
	@ApiModelProperty(value = "排序字段，由小到大")
	private Integer sort;

	/**
	 * 描述信息
	 */
	@ApiModelProperty(value = "描述信息")
	private String remarks;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	@ApiModelProperty(value = "更新时间")
	private LocalDateTime updateTime;

	/**
	 * 下级组织
	 */
	@ApiModelProperty(value = "下级组织")
	List<SysOrganizationTree> children = new ArrayList<>();

	/**
	 * 设置节点的子节点列表
	 * @param children 子节点
	 */
	@Override
	public void setChildren(List<? extends TreeNode<Integer>> children) {
		// noinspection unchecked
		this.children = (List<SysOrganizationTree>) children;
	}

}