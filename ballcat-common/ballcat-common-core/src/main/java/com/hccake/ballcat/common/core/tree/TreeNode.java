package com.hccake.ballcat.common.core.tree;

import java.util.List;

/**
 * @author Hccake
 * @version 1.0
 * @date 2020/6/21 17:05
 */
public interface TreeNode<T> {

	/**
	 * 获取节点id
	 * @return 树节点id
	 */
	T getId();

	/**
	 * 获取该节点的父节点id
	 * @return 父节点id
	 */
	T getParentId();

	/**
	 * 设置节点的子节点列表
	 * @param children 子节点
	 */
	void setChildren(List<? extends TreeNode<T>> children);

	/**
	 * 获取所有子节点
	 * @return 子节点列表
	 */
	List<? extends TreeNode<T>> getChildren();

}
