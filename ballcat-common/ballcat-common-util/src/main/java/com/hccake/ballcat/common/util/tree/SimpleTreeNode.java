package com.hccake.ballcat.common.util.tree;

import lombok.Data;

import java.util.List;

/**
 * @author Hccake
 * @version 1.0
 * @date 2020/6/21 17:08
 */
@Data
public class SimpleTreeNode<I> implements TreeNode<I> {

	/**
	 * 节点ID
	 */
	private I id;

	/**
	 * 父节点ID
	 */
	private I parentId;

	/**
	 * 子节点集合
	 */
	private List<SimpleTreeNode<I>> children;

	@Override
	@SuppressWarnings("unchecked")
	public <T extends TreeNode<I>> void setChildren(List<T> children) {
		this.children = (List<SimpleTreeNode<I>>) children;
	}

}
