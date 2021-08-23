package com.hccake.ballcat.common.util.tree;

import lombok.Data;

import java.util.List;

/**
 * @author Hccake
 * @version 1.0
 * @date 2020/6/21 17:08
 */
@Data
public class SimpleTreeNode<T> implements TreeNode<T> {

	/**
	 * 节点ID
	 */
	private T id;

	/**
	 * 父节点ID
	 */
	private T parentId;

	/**
	 * 子节点集合
	 */
	private List<? extends TreeNode<T>> children;

}
