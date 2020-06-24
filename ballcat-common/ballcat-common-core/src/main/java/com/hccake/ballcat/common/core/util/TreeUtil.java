package com.hccake.ballcat.common.core.util;

import cn.hutool.core.collection.CollectionUtil;
import com.hccake.ballcat.common.core.tree.TreeNode;
import lombok.experimental.UtilityClass;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * @author Hccake
 * @version 1.0
 * @date 2020/6/21 17:21
 */
@UtilityClass
public class TreeUtil {

	/**
	 * 根据一个TreeNode集合，返回构建好的树列表
	 * @param nodes TreeNode集合
	 * @param rootId 根节点Id
	 * @param <T> TreeNode的子类
	 * @param <I> TreeNodeId的类型
	 * @return 树列表
	 */
	public <T extends TreeNode<I>, I> List<T> buildTree(List<T> nodes, I rootId) {
		return TreeUtil.buildTree(nodes, rootId, Function.identity());
	}

	/**
	 * 根据源数据列表转换为树
	 * @param list 源数据列表
	 * @param rootId 根节点Id
	 * @param convertToTree 转换方法
	 * @param <T> TreeNode的子类
	 * @param <I> TreeNodeId的类型
	 * @param <R> 源数据类型
	 * @return 树列表
	 */
	public <T extends TreeNode<I>, I, R> List<T> buildTree(List<R> list, I rootId, Function<R, T> convertToTree) {

		List<T> roots = new ArrayList<>();
		for (Iterator<R> ite = list.iterator(); ite.hasNext();) {
			T node = convertToTree.apply(ite.next());
			if (Objects.equals(rootId, node.getParentId())) {
				roots.add(node);
				ite.remove();
			}
		}

		roots.forEach(r -> {
			TreeUtil.setChildren(r, list, convertToTree);
		});
		return roots;
	}

	/**
	 * 从所有节点列表中查找并设置parent的所有子节点
	 * @param parent 父节点
	 * @param nodes 所有树节点列表
	 */
	public <T extends TreeNode<I>, I, R> void setChildren(T parent, List<R> nodes, Function<R, T> convertToTree) {
		List<T> children = new ArrayList<>();
		Object parentId = parent.getId();
		for (Iterator<R> ite = nodes.iterator(); ite.hasNext();) {
			T node = convertToTree.apply(ite.next());
			if (Objects.equals(node.getParentId(), parentId)) {
				children.add(node);
				ite.remove();
			}
		}

		// 如果孩子为空，则直接返回,否则继续递归设置孩子的孩子
		if (children.isEmpty()) {
			return;
		}
		parent.setChildren(children);
		children.forEach(m -> {
			// 递归设置子节点
			setChildren(m, nodes, convertToTree);
		});
	}

	/**
	 * 获取指定树节点下的所有叶子节点
	 * @param parent 父节点
	 * @param <T> 实际节点类型
	 * @return 叶子节点
	 */
	public <T extends TreeNode<?>> List<T> getLeafs(T parent) {
		List<T> leafs = new ArrayList<>();
		fillLeaf(parent, leafs);
		return leafs;
	}

	/**
	 * 将parent的所有叶子节点填充至leafs列表中
	 * @param parent 父节点
	 * @param leafs 叶子节点列表
	 * @param <T> 实际节点类型
	 */
	@SuppressWarnings("rawtypes, unchecked")
	public <T extends TreeNode> void fillLeaf(T parent, List<T> leafs) {
		List<T> children = parent.getChildren();
		// 如果节点没有子节点则说明为叶子节点
		if (CollectionUtils.isEmpty(children)) {
			leafs.add(parent);
			return;
		}
		// 递归调用子节点，查找叶子节点
		for (T child : children) {
			fillLeaf(child, leafs);
		}
	}

	/**
	 * 获取树节点Id
	 * @param treeList 树列表
	 * @param <T> TreeNode实现类
	 * @param <I> TreeNodeId 类型
	 * @return List<I> 节点Id列表
	 */
	public <T extends TreeNode<I>, I> List<I> getTreeNodeIds(List<T> treeList) {
		List<I> ids = new ArrayList<>();
		fillTreeNodeIds(ids, treeList);
		return ids;
	}

	/**
	 * 填充树节点Id
	 * @param ids 节点Id列表
	 * @param treeList 树列表
	 * @param <T> TreeNode实现类
	 * @param <I> TreeNodeId 类型
	 */
	public <T extends TreeNode<I>, I> void fillTreeNodeIds(List<I> ids, List<T> treeList) {
		// 如果节点没有子节点则说明为叶子节点
		if (CollectionUtils.isEmpty(treeList)) {
			return;
		}
		for (T treeNode : treeList) {
			ids.add(treeNode.getId());
			List<? extends TreeNode<I>> children = treeNode.getChildren();
			if (CollectionUtil.isNotEmpty(children)) {
				fillTreeNodeIds(ids, children);
			}
		}
	}

}
