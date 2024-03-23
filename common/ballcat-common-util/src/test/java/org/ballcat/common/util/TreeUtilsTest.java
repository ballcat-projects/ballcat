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
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.ballcat.common.model.domain.TreeNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author hccake
 */
class TreeUtilsTest {

	/**
	 * 构建一棵树
	 * @param levelNums 每个节点的子节点数量
	 * @return List
	 */
	private List<TreeNode<Long>> buildTreeNodes(List<Integer> levelNums) {
		List<TreeNode<Long>> list = new ArrayList<>();
		List<Long> parentIds = new ArrayList<>(levelNums.size());
		parentIds.add(0L);

		long id = 0L;
		for (int i = 0; i < levelNums.size(); i++) {
			int levelNum = levelNums.get(i);
			Long parentId = parentIds.get(i);

			for (int j = 0; j < levelNum; j++) {
				id++;
				TestTreeNode node = new TestTreeNode();
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
		List<TreeNode<Long>> list = buildTreeNodes(Arrays.asList(2, 1, 1, 2, 3, 4));
		// 树节点列表转树结构
		List<TreeNode<Long>> nodes = TreeUtils.buildTree(list, 0L);
		// 树结构转回树节点列表
		List<TreeNode<Long>> abstractIdTreeNodes = TreeUtils.treeToList(nodes);
		// 排序处理
		abstractIdTreeNodes.sort(Comparator.comparingLong(TreeNode::getKey));

		// 比较初始的树节点列表和通过两次转换完毕的结果是否一致
		Assertions.assertEquals(list, abstractIdTreeNodes);
	}

	@lombok.Setter
	@lombok.Getter
	static class TestTreeNode implements TreeNode<Long> {

		/**
		 * 节点ID
		 */
		private Long id;

		/**
		 * 父节点ID
		 */
		private Long parentId;

		/**
		 * 子节点集合
		 */
		private List<TestTreeNode> children;

		@Override
		public Long getKey() {
			return this.id;
		}

		@Override
		public Long getParentKey() {
			return this.parentId;
		}

		@Override
		@SuppressWarnings("unchecked")
		public <T extends TreeNode<Long>> void setChildren(List<T> children) {
			this.children = (List<TestTreeNode>) children;
		}

		@Override
		@SuppressWarnings("unchecked")
		public <T extends TreeNode<Long>> List<T> getChildren() {
			return (List<T>) this.children;
		}

	}

}
