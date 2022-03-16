package com.hccake.ballcat.common.util.tree;

import lombok.ToString;

import java.util.List;

/**
 * @author Hccake
 * @version 1.0
 * @date 2020/6/21 17:08
 */
@ToString
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
	public I getId() {
		return id;
	}

	public void setId(I id) {
		this.id = id;
	}

	@Override
	public I getParentId() {
		return parentId;
	}

	public void setParentId(I parentId) {
		this.parentId = parentId;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends TreeNode<I>> void setChildren(List<T> children) {
		this.children = (List<SimpleTreeNode<I>>) children;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends TreeNode<I>> List<T> getChildren() {
		return (List<T>) children;
	}

}
