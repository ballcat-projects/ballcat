package com.hccake.ballcat.common.util.tree;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import lombok.experimental.UtilityClass;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Hccake 2020/6/21 17:21
 * @version 1.0
 */
@UtilityClass
public class TreeUtils {

	/**
	 * 根据一个TreeNode集合，返回构建好的树列表
	 * @param nodes TreeNode集合
	 * @param rootId 根节点Id
	 * @param <T> TreeNode的子类
	 * @param <I> TreeNodeId的类型
	 * @return 树列表
	 */
	public <T extends TreeNode<I>, I> List<T> buildTree(List<T> nodes, I rootId) {
		return TreeUtils.buildTree(nodes, rootId, Function.identity(), null);
	}

	/**
	 * 根据一个TreeNode集合，返回构建好的树列表
	 * @param nodes TreeNode集合
	 * @param rootId 根节点Id
	 * @param comparator 树节点排序规则
	 * @param <T> TreeNode的子类
	 * @param <I> TreeNodeId的类型
	 * @return 树列表
	 */
	public <T extends TreeNode<I>, I> List<T> buildTree(List<T> nodes, I rootId, Comparator<? super T> comparator) {
		return TreeUtils.buildTree(nodes, rootId, Function.identity(), comparator);
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
		return TreeUtils.buildTree(list, rootId, convertToTree, null);
	}

	/**
	 * 根据源数据列表转换为树
	 * @param list 源数据列表
	 * @param rootId 根节点Id
	 * @param convertToTree 转换方法
	 * @param comparator 树节点排序规则
	 * @param <T> TreeNode的子类
	 * @param <I> TreeNodeId的类型
	 * @param <R> 源数据类型
	 * @return 树列表
	 */
	public <T extends TreeNode<I>, I, R> List<T> buildTree(List<R> list, I rootId, Function<R, T> convertToTree,
			Comparator<? super T> comparator) {
		if (list == null || list.isEmpty()) {
			return new ArrayList<>();
		}

		// 转换为 TreeNode
		Stream<T> tStream = list.stream().map(convertToTree);
		// 如果需要排序，则在收集时进行排序处理
		if (comparator != null) {
			tStream = tStream.sorted(comparator);
		}
		// 根据 parentId 进行分组
		Map<I, List<T>> childrenMap = tStream
				.collect(Collectors.groupingBy(T::getParentId, LinkedHashMap::new, Collectors.toList()));

		// 根据根节点ID拿到一级节点
		List<T> treeList = childrenMap.get(rootId);
		// 异常数据校验
		Assert.notEmpty(treeList, "错误的数据，找不到根节点的子节点");
		// 遍历所有一级节点，赋值其子节点
		treeList.forEach(node -> TreeUtils.setChildren(node, childrenMap));
		return treeList;
	}

	/**
	 * 从所有节点列表中查找并设置parent的所有子节点
	 * @param parent 父节点
	 * @param childrenMap 子节点集合Map(k: parentId, v: Node)
	 */
	public <T extends TreeNode<I>, I> void setChildren(T parent, Map<I, List<T>> childrenMap) {
		I parentId = parent.getId();
		List<T> children = childrenMap.get(parentId);
		// 如果有孩子节点则赋值，且给孩子节点的孩子节点赋值
		if (CollectionUtil.isNotEmpty(children)) {
			parent.setChildren(children);
			children.forEach(node -> TreeUtils.setChildren(node, childrenMap));
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
		if (CollectionUtil.isEmpty(children)) {
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
		if (CollectionUtil.isEmpty(treeList)) {
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

	/**
	 * 将一颗树的所有节点平铺到一个 list 中
	 * @param treeNode 树节点
	 * @param <T> 树节点的类型
	 * @return 所有树节点组成的列表
	 */
	public <T extends TreeNode<?>> List<T> treeToList(T treeNode) {
		return treeToList(treeNode, Function.identity());
	}

	/**
	 * 将一颗树的所有节点平铺到 list 中
	 * @param treeNode 树节点
	 * @param converter 转换器，用于将树节点的类型进行转换，再存储到 list 中
	 * @param <T> 树节点类型
	 * @param <R> 转换器转换后的类型
	 * @return List<R>
	 */
	@SuppressWarnings("unchecked")
	public <T extends TreeNode<?>, R> List<R> treeToList(T treeNode, Function<T, R> converter) {
		List<R> list = new ArrayList<>();

		// 使用队列存储未处理的树节点
		Queue<T> queue = new LinkedList<>();
		queue.add(treeNode);

		while (!queue.isEmpty()) {
			// 弹出一个树节点
			T node = queue.poll();
			if (node == null) {
				continue;
			}

			// 如果当前节点的含有子节点，则添加到队列中
			List<? extends TreeNode<?>> children = node.getChildren();
			if (CollectionUtil.isNotEmpty(children)) {
				queue.addAll((List<T>) children);
			}

			// 不再保留对子节点的引用
			node.setChildren(null);
			// 转换树节点，并将结果添加到 list 中
			list.add(converter.apply(node));
		}
		return list;
	}

	/**
	 * 将一组树的所有节点平铺到一个 list 中
	 * @param treeNodes 树节点集合
	 * @param <T> 树节点的类型
	 * @return 所有树节点组成的列表
	 */
	public <T extends TreeNode<?>> List<T> treeToList(List<T> treeNodes) {
		return treeToList(treeNodes, Function.identity());
	}

	/**
	 * 将一组树的所有节点平铺到一个 list 中
	 * @param treeNodes 树节点集合
	 * @param converter 转换器，用于将树节点的类型进行转换，再存储到 list 中
	 * @param <T> 树节点的类型
	 * @param <R> 转换器转换后的类型
	 * @return 所有树节点组成的列表
	 */
	public <T extends TreeNode<?>, R> List<R> treeToList(List<T> treeNodes, Function<T, R> converter) {
		return treeNodes.stream().map(node -> treeToList(node, converter)).flatMap(Collection::stream)
				.collect(Collectors.toList());
	}

}
