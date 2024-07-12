/*
 * Copyright 2023-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ballcat.common.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.ballcat.common.model.domain.TreeFunctionWrapper;
import org.ballcat.common.model.domain.TreeNode;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

/**
 * @author Hccake 2020/6/21 17:21
 */
public final class TreeUtils {

	private TreeUtils() {
	}

	/**
	 * 根据一个TreeNode集合，返回构建好的树列表
	 * @param nodes TreeNode集合
	 * @param rootId 根节点Id
	 * @param <T> TreeNode的子类
	 * @param <I> TreeNodeId的类型
	 * @return 树列表
	 */
	public static <T extends TreeNode<I>, I> List<T> buildTree(List<T> nodes, I rootId) {
		TreeFunctionWrapper<T, I> treeFunctionWrapper = new TreeFunctionWrapper<>(TreeNode::getKey,
				TreeNode::getParentKey, TreeNode::setChildren, TreeNode::getChildren);
		return TreeUtils.buildTree(nodes, rootId, treeFunctionWrapper);
	}

	/**
	 * 根据一个TreeNode集合，返回构建好的树列表
	 * @param nodes TreeNode集合
	 * @param rootId 根节点Id
	 * @param treeFunctionWrapper 树相关方法包装器
	 * @param <T> 树节点类型
	 * @param <I> TreeNodeId的类型
	 * @return 树列表
	 */
	public static <T, I> List<T> buildTree(List<T> nodes, I rootId, TreeFunctionWrapper<T, I> treeFunctionWrapper) {
		return TreeUtils.buildTree(nodes, rootId, treeFunctionWrapper, Function.identity());
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
	public static <T extends TreeNode<I>, I> List<T> buildTree(List<T> nodes, I rootId,
			Comparator<? super T> comparator) {
		TreeFunctionWrapper<T, I> treeFunctionWrapper = new TreeFunctionWrapper<>(TreeNode::getKey,
				TreeNode::getParentKey, TreeNode::setChildren, TreeNode::getChildren);
		return TreeUtils.buildTree(nodes, rootId, treeFunctionWrapper, comparator);
	}

	/**
	 * 根据一个TreeNode集合，返回构建好的树列表
	 * @param nodes TreeNode集合
	 * @param rootId 根节点Id
	 * @param treeFunctionWrapper 树相关方法包装器
	 * @param comparator 树节点排序规则
	 * @param <T> TreeNode的子类
	 * @param <I> TreeNodeId的类型
	 * @return 树列表
	 */
	public static <T, I> List<T> buildTree(List<T> nodes, I rootId, TreeFunctionWrapper<T, I> treeFunctionWrapper,
			Comparator<? super T> comparator) {
		return TreeUtils.buildTree(nodes, rootId, treeFunctionWrapper, Function.identity(), comparator);
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
	public static <T extends TreeNode<I>, I, R> List<T> buildTree(List<R> list, I rootId,
			Function<R, T> convertToTree) {
		TreeFunctionWrapper<T, I> treeFunctionWrapper = new TreeFunctionWrapper<>(TreeNode::getKey,
				TreeNode::getParentKey, TreeNode::setChildren, TreeNode::getChildren);
		return TreeUtils.buildTree(list, rootId, treeFunctionWrapper, convertToTree);
	}

	/**
	 * 根据源数据列表转换为树
	 * @param list 源数据列表
	 * @param rootId 根节点Id
	 * @param treeFunctionWrapper 树相关方法包装器
	 * @param convertToTree 转换方法
	 * @param <T> TreeNode的子类
	 * @param <I> TreeNodeId的类型
	 * @param <R> 源数据类型
	 * @return 树列表
	 */
	public static <T, I, R> List<T> buildTree(List<R> list, I rootId, TreeFunctionWrapper<T, I> treeFunctionWrapper,
			Function<R, T> convertToTree) {
		return TreeUtils.buildTree(list, rootId, treeFunctionWrapper, convertToTree, null);
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
	public static <T extends TreeNode<I>, I, R> List<T> buildTree(List<R> list, I rootId, Function<R, T> convertToTree,
			Comparator<? super T> comparator) {
		TreeFunctionWrapper<T, I> treeFunctionWrapper = new TreeFunctionWrapper<>(TreeNode::getKey,
				TreeNode::getParentKey, TreeNode::setChildren, TreeNode::getChildren);
		return TreeUtils.buildTree(list, rootId, treeFunctionWrapper, convertToTree, comparator);
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
	public static <T, I, R> List<T> buildTree(List<R> list, I rootId, TreeFunctionWrapper<T, I> treeFunctionWrapper,
			Function<R, T> convertToTree, Comparator<? super T> comparator) {
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
		Map<I, List<T>> childrenMap = tStream.collect(Collectors.groupingBy(
				x -> treeFunctionWrapper.parentKeyExtractor().apply(x), LinkedHashMap::new, Collectors.toList()));

		// 根据根节点ID拿到一级节点
		List<T> treeList = childrenMap.get(rootId);
		// 异常数据校验
		Assert.notEmpty(treeList, "错误的数据，找不到根节点的子节点");
		// 遍历所有一级节点，赋值其子节点
		treeList.forEach(node -> TreeUtils.setChildren(node, childrenMap, treeFunctionWrapper));
		return treeList;
	}

	/**
	 * 从所有节点列表中查找并设置parent的所有子节点
	 * @param parent 父节点
	 * @param childrenMap 子节点集合Map(k: parentId, v: Node)
	 */
	public static <T extends TreeNode<I>, I> void setChildren(T parent, Map<I, List<T>> childrenMap) {
		TreeFunctionWrapper<T, I> treeFunctionWrapper = new TreeFunctionWrapper<>(TreeNode::getKey,
				TreeNode::getParentKey, TreeNode::setChildren, TreeNode::getChildren);
		TreeUtils.setChildren(parent, childrenMap, treeFunctionWrapper);
	}

	/**
	 * 从所有节点列表中查找并设置parent的所有子节点
	 * @param parent 父节点
	 * @param childrenMap 子节点集合Map(k: parentId, v: Node)
	 * @param treeFunctionWrapper 树相关方法包装器
	 */
	public static <T, I> void setChildren(T parent, Map<I, List<T>> childrenMap,
			TreeFunctionWrapper<T, I> treeFunctionWrapper) {
		Function<T, I> keyExtractor = treeFunctionWrapper.keyExtractor();
		BiConsumer<T, List<T>> childrenSetter = treeFunctionWrapper.childrenSetter();

		I parentId = keyExtractor.apply(parent);
		List<T> children = childrenMap.get(parentId);
		// 如果有孩子节点则赋值，且给孩子节点的孩子节点赋值
		if (!CollectionUtils.isEmpty(children)) {
			childrenSetter.accept(parent, children);
			children.forEach(node -> TreeUtils.setChildren(node, childrenMap, treeFunctionWrapper));
		}
		else {
			childrenSetter.accept(parent, children);
		}
	}

	/**
	 * 获取指定树节点下的所有叶子节点
	 * @param parent 父节点
	 * @param <T> 树节点的类型
	 * @param <I> 树节点的 id 类型
	 * @return 叶子节点
	 */
	public static <T extends TreeNode<I>, I> List<T> getLeafs(T parent) {
		TreeFunctionWrapper<T, I> treeFunctionWrapper = new TreeFunctionWrapper<>(TreeNode::getKey,
				TreeNode::getParentKey, TreeNode::setChildren, TreeNode::getChildren);
		return getLeafs(parent, treeFunctionWrapper);
	}

	/**
	 * 获取指定树节点下的所有叶子节点
	 * @param parent 父节点
	 * @param <T> 树节点的类型
	 * @param <I> 树节点的 id 类型
	 * @return 叶子节点
	 */
	public static <T, I> List<T> getLeafs(T parent, TreeFunctionWrapper<T, I> treeFunctionWrapper) {
		List<T> leafs = new ArrayList<>();
		fillLeaf(parent, leafs, treeFunctionWrapper);
		return leafs;
	}

	/**
	 * 将parent的所有叶子节点填充至leafs列表中
	 * @param parent 父节点
	 * @param leafs 叶子节点列表
	 * @param <T> 实际节点类型
	 */
	public static <T extends TreeNode<I>, I> void fillLeaf(T parent, List<T> leafs) {
		TreeFunctionWrapper<T, I> treeFunctionWrapper = new TreeFunctionWrapper<>(TreeNode::getKey,
				TreeNode::getParentKey, TreeNode::setChildren, TreeNode::getChildren);
		fillLeaf(parent, leafs, treeFunctionWrapper);
	}

	/**
	 * 将parent的所有叶子节点填充至leafs列表中
	 * @param parent 父节点
	 * @param leafs 叶子节点列表
	 * @param <T> 实际节点类型
	 */
	public static <T, I> void fillLeaf(T parent, List<T> leafs, TreeFunctionWrapper<T, I> treeFunctionWrapper) {
		List<T> children = treeFunctionWrapper.childrenGetter().apply(parent);
		if (CollectionUtils.isEmpty(children)) {
			leafs.add(parent);
			return;
		}
		for (T child : children) {
			fillLeaf(child, leafs, treeFunctionWrapper);
		}
	}

	/**
	 * 获取树节点Id
	 * @param treeList 树列表
	 * @param <T> TreeNode实现类
	 * @param <I> TreeNodeId 类型
	 * @return List<I> 节点Id列表
	 */
	public static <T extends TreeNode<I>, I> List<I> getTreeNodeIds(List<T> treeList) {
		TreeFunctionWrapper<T, I> treeFunctionWrapper = new TreeFunctionWrapper<>(TreeNode::getKey,
				TreeNode::getParentKey, TreeNode::setChildren, TreeNode::getChildren);
		return getTreeNodeIds(treeList, treeFunctionWrapper);
	}

	/**
	 * 获取树节点Id
	 * @param treeList 树列表
	 * @param <T> TreeNode实现类
	 * @param <I> TreeNodeId 类型
	 * @return List<I> 节点Id列表
	 */
	public static <T, I> List<I> getTreeNodeIds(List<T> treeList, TreeFunctionWrapper<T, I> treeFunctionWrapper) {
		List<I> ids = new ArrayList<>();
		fillTreeNodeIds(ids, treeList, treeFunctionWrapper);
		return ids;
	}

	/**
	 * 填充树节点Id
	 * @param ids 节点Id列表
	 * @param treeList 树列表
	 * @param <T> TreeNode实现类
	 * @param <I> TreeNodeId 类型
	 */
	public static <T extends TreeNode<I>, I> void fillTreeNodeIds(List<I> ids, List<T> treeList) {
		TreeFunctionWrapper<T, I> treeFunctionWrapper = new TreeFunctionWrapper<>(TreeNode::getKey,
				TreeNode::getParentKey, TreeNode::setChildren, TreeNode::getChildren);
		fillTreeNodeIds(ids, treeList, treeFunctionWrapper);
	}

	/**
	 * 填充树节点Id
	 * @param ids 节点Id列表
	 * @param treeList 树列表
	 * @param <T> TreeNode实现类
	 * @param <I> TreeNodeId 类型
	 */
	public static <T, I> void fillTreeNodeIds(List<I> ids, List<T> treeList,
			TreeFunctionWrapper<T, I> treeFunctionWrapper) {
		// 如果节点没有子节点则说明为叶子节点
		if (CollectionUtils.isEmpty(treeList)) {
			return;
		}
		for (T treeNode : treeList) {
			ids.add(treeFunctionWrapper.keyExtractor().apply(treeNode));
			List<T> children = treeFunctionWrapper.childrenGetter().apply(treeNode);
			if (!CollectionUtils.isEmpty(children)) {
				fillTreeNodeIds(ids, children, treeFunctionWrapper);
			}
		}
	}

	/**
	 * 将一颗树的所有节点平铺到一个 list 中
	 * @param treeNode 树节点
	 * @param <T> 树节点的类型
	 * @param <I> 树节点的 id 类型
	 * @return 所有树节点组成的列表
	 */
	public static <T extends TreeNode<I>, I> List<T> treeToList(T treeNode) {
		TreeFunctionWrapper<T, I> treeFunctionWrapper = new TreeFunctionWrapper<>(TreeNode::getKey,
				TreeNode::getParentKey, TreeNode::setChildren, TreeNode::getChildren);
		return treeToList(treeNode, treeFunctionWrapper);
	}

	/**
	 * 将一颗树的所有节点平铺到一个 list 中
	 * @param treeNode 树节点
	 * @param <T> 树节点的类型
	 * @param <I> 树节点的 id 类型
	 * @return 所有树节点组成的列表
	 */
	public static <T, I> List<T> treeToList(T treeNode, TreeFunctionWrapper<T, I> treeFunctionWrapper) {
		return treeToList(treeNode, treeFunctionWrapper, Function.identity());
	}

	/**
	 * 将一组树的所有节点平铺到一个 list 中
	 * @param treeNodes 树节点集合
	 * @param <T> 树节点的类型
	 * @param <I> 树节点的 id 类型
	 * @return 所有树节点组成的列表
	 */
	public static <T extends TreeNode<I>, I> List<T> treeToList(List<T> treeNodes) {
		TreeFunctionWrapper<T, I> treeFunctionWrapper = new TreeFunctionWrapper<>(TreeNode::getKey,
				TreeNode::getParentKey, TreeNode::setChildren, TreeNode::getChildren);
		return treeToList(treeNodes, treeFunctionWrapper);
	}

	/**
	 * 将一组树的所有节点平铺到一个 list 中
	 * @param treeNodes 树节点集合
	 * @param <T> 树节点的类型
	 * @param <I> 树节点的 id 类型
	 * @return 所有树节点组成的列表
	 */
	public static <T, I> List<T> treeToList(List<T> treeNodes, TreeFunctionWrapper<T, I> treeFunctionWrapper) {
		return treeToList(treeNodes, treeFunctionWrapper, Function.identity());
	}

	/**
	 * 将一组树的所有节点平铺到一个 list 中
	 * @param treeNodes 树节点集合
	 * @param converter 转换器，用于将树节点的类型进行转换，再存储到 list 中
	 * @param <T> 树节点的类型
	 * @param <I> 树节点的 id 类型
	 * @param <R> 转换器转换后的类型
	 * @return 所有树节点组成的列表
	 */
	public static <T extends TreeNode<I>, I, R> List<R> treeToList(List<T> treeNodes, Function<T, R> converter) {
		TreeFunctionWrapper<T, I> treeFunctionWrapper = new TreeFunctionWrapper<>(TreeNode::getKey,
				TreeNode::getParentKey, TreeNode::setChildren, TreeNode::getChildren);
		return treeToList(treeNodes, treeFunctionWrapper, converter);
	}

	/**
	 * 将一组树的所有节点平铺到一个 list 中
	 * @param treeNodes 树节点集合
	 * @param converter 转换器，用于将树节点的类型进行转换，再存储到 list 中
	 * @param <T> 树节点的类型
	 * @param <I> 树节点的 id 类型
	 * @param <R> 转换器转换后的类型
	 * @return 所有树节点组成的列表
	 */
	public static <T, I, R> List<R> treeToList(List<T> treeNodes, TreeFunctionWrapper<T, I> treeFunctionWrapper,
			Function<T, R> converter) {
		return treeNodes.stream()
			.map(node -> treeToList(node, treeFunctionWrapper, converter))
			.flatMap(Collection::stream)
			.collect(Collectors.toList());
	}

	/**
	 * 将一颗树的所有节点平铺到 list 中, 同时清除所有 node 的子节点引用。
	 * @param treeNode 树节点
	 * @param converter 转换器，用于将树节点的类型进行转换，再存储到 list 中
	 * @param <T> 树节点的类型
	 * @param <I> 树节点的 id 类型
	 * @param <R> 转换器转换后的类型
	 * @return List<R>
	 */
	public static <T extends TreeNode<I>, I, R> List<R> treeToList(T treeNode, Function<T, R> converter) {
		TreeFunctionWrapper<T, I> treeFunctionWrapper = new TreeFunctionWrapper<>(TreeNode::getKey,
				TreeNode::getParentKey, TreeNode::setChildren, TreeNode::getChildren);
		return treeToList(treeNode, treeFunctionWrapper, converter);
	}

	/**
	 * 将一颗树的所有节点平铺到 list 中, 同时清除所有 node 的子节点引用。
	 * @param treeNode 树节点
	 * @param converter 转换器，用于将树节点的类型进行转换，再存储到 list 中
	 * @param <T> 树节点的类型
	 * @param <I> 树节点的 id 类型
	 * @param <R> 转换器转换后的类型
	 * @return List<R>
	 */
	public static <T, I, R> List<R> treeToList(T treeNode, TreeFunctionWrapper<T, I> treeFunctionWrapper,
			Function<T, R> converter) {
		// 默认不保留子节点
		return collectNode(treeNode, treeFunctionWrapper, converter, true);
	}

	/**
	 * 收集所有树节点的数据到一个 List 中。
	 * @param treeNode 树节点
	 * @param converter 转换器，用于将树节点的类型进行转换，再存储到 list 中
	 * @param clearChildrenReference 收集过程中是否清除树节点的子节点引用
	 * @param <T> 树节点的类型
	 * @param <I> 树节点的 id 类型
	 * @param <R> 转换器转换后的类型
	 * @return List<R>
	 */
	public static <T extends TreeNode<I>, I, R> List<R> collectNode(T treeNode, Function<T, R> converter,
			boolean clearChildrenReference) {
		TreeFunctionWrapper<T, I> treeFunctionWrapper = new TreeFunctionWrapper<>(TreeNode::getKey,
				TreeNode::getParentKey, TreeNode::setChildren, TreeNode::getChildren);
		return collectNode(treeNode, treeFunctionWrapper, converter, clearChildrenReference);
	}

	/**
	 * 收集所有树节点的数据到一个 List 中。
	 * @param treeNode 树节点
	 * @param converter 转换器，用于将树节点的类型进行转换，再存储到 list 中
	 * @param clearChildrenReference 收集过程中是否清除树节点的子节点引用
	 * @param <T> 树节点的类型
	 * @param <I> 树节点的 id 类型
	 * @param <R> 转换器转换后的类型
	 * @return List<R>
	 */
	public static <T, I, R> List<R> collectNode(T treeNode, TreeFunctionWrapper<T, I> treeFunctionWrapper,
			Function<T, R> converter, boolean clearChildrenReference) {
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
			List<T> children = treeFunctionWrapper.childrenGetter().apply(node);
			if (!CollectionUtils.isEmpty(children)) {
				queue.addAll(children);
			}

			// 不再保留对子节点的引用
			if (clearChildrenReference) {
				treeFunctionWrapper.childrenSetter().accept(node, null);
			}

			// 转换树节点，并将结果添加到 list 中
			list.add(converter.apply(node));
		}
		return list;
	}

	/**
	 * 根据指定规则进行树剪枝
	 * @param treeNodes 待剪枝的树节点列表
	 * @param <T> TreeNode
	 * @param matcher 匹配规则
	 * @return 剪枝完成后的树节点列表
	 */
	public static <T extends TreeNode<I>, I> List<T> pruneTree(List<T> treeNodes, Predicate<T> matcher) {
		TreeFunctionWrapper<T, I> treeFunctionWrapper = new TreeFunctionWrapper<>(TreeNode::getKey,
				TreeNode::getParentKey, TreeNode::setChildren, TreeNode::getChildren);
		return pruneTree(treeNodes, matcher, treeFunctionWrapper);
	}

	/**
	 * 根据指定规则进行树剪枝
	 * @param treeNodes 待剪枝的树节点列表
	 * @param <T> TreeNode
	 * @param matcher 匹配规则
	 * @return 剪枝完成后的树节点列表
	 */
	public static <T, I> List<T> pruneTree(List<T> treeNodes, Predicate<T> matcher,
			TreeFunctionWrapper<T, I> treeFunctionWrapper) {
		List<T> result = new ArrayList<>();
		if (CollectionUtils.isEmpty(treeNodes)) {
			return result;
		}
		Function<T, List<T>> childrenGetter = treeFunctionWrapper.childrenGetter();
		BiConsumer<T, List<T>> childrenSetter = treeFunctionWrapper.childrenSetter();

		for (T treeNode : treeNodes) {
			List<T> children = pruneTree(childrenGetter.apply(treeNode), matcher, treeFunctionWrapper);
			if (!CollectionUtils.isEmpty(children)) {
				childrenSetter.accept(treeNode, children);
				result.add(treeNode);
			}
			else if (matcher.test(treeNode)) {
				childrenSetter.accept(treeNode, null);
				result.add(treeNode);
			}
		}
		return result;
	}

	/**
	 * 根据指定规则进行树剪枝
	 * @param treeNode 待剪枝的树节点
	 * @param <T> TreeNode
	 * @param matcher 匹配规则
	 * @return 剪枝完成后的树节点
	 */
	public static <T extends TreeNode<I>, I> T pruneTree(T treeNode, Predicate<T> matcher) {
		TreeFunctionWrapper<T, I> treeFunctionWrapper = new TreeFunctionWrapper<>(TreeNode::getKey,
				TreeNode::getParentKey, TreeNode::setChildren, TreeNode::getChildren);
		return pruneTree(treeNode, matcher, treeFunctionWrapper);
	}

	/**
	 * 根据指定规则进行树剪枝
	 * @param treeNode 待剪枝的树节点
	 * @param <T> TreeNode
	 * @param matcher 匹配规则
	 * @return 剪枝完成后的树节点
	 */
	public static <T, I> T pruneTree(T treeNode, Predicate<T> matcher, TreeFunctionWrapper<T, I> treeFunctionWrapper) {
		Function<T, List<T>> childrenGetter = treeFunctionWrapper.childrenGetter();
		BiConsumer<T, List<T>> childrenSetter = treeFunctionWrapper.childrenSetter();

		List<T> children = pruneTree(childrenGetter.apply(treeNode), matcher, treeFunctionWrapper);
		boolean childrenMatched = !CollectionUtils.isEmpty(children);
		if (childrenMatched) {
			childrenSetter.accept(treeNode, children);
		}

		boolean nodeMatched = matcher.test(treeNode);
		return (nodeMatched || childrenMatched) ? treeNode : null;
	}

	/**
	 * 遍历树节点（深度优先）
	 */
	public static <T extends TreeNode<I>, I> void forEachDFS(T treeNode, T parentTreeNode, BiConsumer<T, T> action) {
		TreeFunctionWrapper<T, I> treeFunctionWrapper = new TreeFunctionWrapper<>(TreeNode::getKey,
				TreeNode::getParentKey, TreeNode::setChildren, TreeNode::getChildren);
		forEachDFS(treeNode, parentTreeNode, action, treeFunctionWrapper);
	}

	/**
	 * 遍历树节点（深度优先）
	 */
	public static <T, I> void forEachDFS(T treeNode, T parentTreeNode, BiConsumer<T, T> action,
			TreeFunctionWrapper<T, I> treeFunctionWrapper) {
		action.accept(treeNode, parentTreeNode);
		List<T> children = treeFunctionWrapper.childrenGetter().apply(treeNode);
		forEachDFS(children, treeNode, action, treeFunctionWrapper);
	}

	/**
	 * 遍历树节点（深度优先）
	 */
	public static <T extends TreeNode<I>, I> void forEachDFS(List<T> treeNodes, T parentTreeNode,
			BiConsumer<T, T> action) {
		TreeFunctionWrapper<T, I> treeFunctionWrapper = new TreeFunctionWrapper<>(TreeNode::getKey,
				TreeNode::getParentKey, TreeNode::setChildren, TreeNode::getChildren);
		forEachDFS(treeNodes, parentTreeNode, action, treeFunctionWrapper);
	}

	/**
	 * 遍历树节点（深度优先）
	 */
	public static <T, I> void forEachDFS(List<T> treeNodes, T parentTreeNode, BiConsumer<T, T> action,
			TreeFunctionWrapper<T, I> treeFunctionWrapper) {
		if (treeNodes == null || treeNodes.isEmpty()) {
			return;
		}
		for (T treeNode : treeNodes) {
			List<T> children = treeFunctionWrapper.childrenGetter().apply(treeNode);
			action.accept(treeNode, parentTreeNode);
			forEachDFS(children, treeNode, action, treeFunctionWrapper);
		}
	}

}
