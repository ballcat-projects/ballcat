package com.hccake.ballcat.common.util.tree;

import java.util.List;

/**
 * @author Hccake
 * @version 1.0
 * @date 2020/6/21 17:05
 */
public interface TreeNode<I> {

	/**
	 * 获取节点 key
	 * @return 树节点 key
	 */
	I getKey();

	/**
	 * 获取该节点的父节点 key
	 * @return 父节点 key
	 */
	I getParentKey();

	/**
	 * 设置节点的子节点列表
	 * @param children 子节点
	 */
	<T extends TreeNode<I>> void setChildren(List<T> children);

	/**
	 * 获取所有子节点
	 * @return 子节点列表
	 */
	<T extends TreeNode<I>> List<T> getChildren();

}
