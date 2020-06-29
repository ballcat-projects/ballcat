package com.hccake.ballcat.common.core.util;

import cn.hutool.core.collection.CollectionUtil;
import com.hccake.ballcat.common.core.tree.TreeNode;
import lombok.experimental.UtilityClass;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Hccake 2020/6/21 17:21
 * @version 1.0
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
		if (list == null || list.size() == 0) {
			return new ArrayList<>();
		}
		// 根据 parentId 进行分组
		Map<I, List<T>> childrenMap = list.stream().map(convertToTree).collect(Collectors.groupingBy(T::getParentId));

		// 根据根节点ID拿到一级节点
		List<T> treeList = childrenMap.get(rootId);
		// 遍历所有一级节点，赋值其子节点
		treeList.forEach(node -> TreeUtil.setChildren(node, childrenMap));
		return treeList;
	}

	/**
	 * 从所有节点列表中查找并设置parent的所有子节点
	 * @param parent 父节点
	 * @param childrenMap 子节点集合Map(k: parentId, v: Node)
	 */
	public <T extends TreeNode<I>, I, R> void setChildren(T parent, Map<I, List<T>> childrenMap) {
		I parentId = parent.getId();
		List<T> children = childrenMap.get(parentId);
		// 如果有孩子节点则赋值，且给孩子节点的孩子节点赋值
		if (CollectionUtil.isNotEmpty(children)) {
			parent.setChildren(children);
			children.forEach(node -> TreeUtil.setChildren(node, childrenMap));
		}
		else {
			parent.setChildren(new ArrayList<>());
		}
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
