package com.hccake.ballcat.common.util.tree;

import lombok.ToString;

import java.util.List;

/**
 * @author Hccake
 * @version 1.0
 * @date 2020/6/21 17:08
 */
@ToString
public abstract class AbstractIdTreeNode<I> implements TreeNode<I> {

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
	private List<AbstractIdTreeNode<I>> children;

	@Override
	public I getKey() {
		return id;
	}

	@Override
	public I getParentKey() {
		return parentId;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends TreeNode<I>> void setChildren(List<T> children) {
		this.children = (List<AbstractIdTreeNode<I>>) children;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends TreeNode<I>> List<T> getChildren() {
		return (List<T>) children;
	}

	public I getId() {
		return id;
	}

	public void setId(I id) {
		this.id = id;
	}

	public I getParentId() {
		return parentId;
	}

	public void setParentId(I parentId) {
		this.parentId = parentId;
	}

}
