package com.hccake.ballcat.common.util;

import com.hccake.ballcat.common.util.tree.SimpleTreeNode;
import com.hccake.ballcat.common.util.tree.TreeUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @author hccake
 */
class TreeUtilsTest {

	/**
	 * 构建一棵树
	 * @param levelNums 每个节点的子节点数量
	 * @return List
	 */
	private List<SimpleTreeNode<Integer>> buildTreeNodes(List<Integer> levelNums) {
		List<SimpleTreeNode<Integer>> list = new ArrayList<>();
		List<Integer> parentIds = new ArrayList<>(levelNums.size());
		parentIds.add(0);

		int id = 0;
		for (int i = 0; i < levelNums.size(); i++) {
			int levelNum = levelNums.get(i);
			int parentId = parentIds.get(i);

			for (int j = 0; j < levelNum; j++) {
				id++;
				SimpleTreeNode<Integer> node = new SimpleTreeNode<>();
				node.setParentId(parentId);
				node.setId(id);
				list.add(node);
				parentIds.add(id);
			}
		}

		return list;
	}

	@Test
	void treeTest() {
		// 构建一个树节点列表
		List<SimpleTreeNode<Integer>> list = buildTreeNodes(Arrays.asList(2, 1, 1, 2, 3, 4));
		// 树节点列表转树结构
		List<SimpleTreeNode<Integer>> nodes = TreeUtils.buildTree(list, 0);
		// 树结构转回树节点列表
		List<SimpleTreeNode<Integer>> simpleTreeNodes = TreeUtils.treeToList(nodes);
		// 排序处理
		simpleTreeNodes.sort(Comparator.comparingInt(SimpleTreeNode::getId));

		// 比较初始的树节点列表和通过两次转换完毕的结果是否一致
		Assertions.assertEquals(list, simpleTreeNodes);
	}

}
