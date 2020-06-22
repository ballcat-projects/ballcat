/**
 * 数组转树形结构
 * @param list 源数组
 * @param parentId 父ID
 * @param attributeFill 数据处理函数
 */
export const listToTree = (list, parentId, attributeFill) => {
  let tree = []
  fillTree(list, tree, parentId, attributeFill)
  return tree
}

/**
 * 数组转树形结构
 * @param list 源数组
 * @param tree 树
 * @param parentId 父ID
 * @param attributeFill 属性填充函数
 */
export const fillTree = (list, tree, parentId, attributeFill) => {
  list.forEach(item => {
    // 判断是否为父级菜单
    if (item.parentId === parentId) {
      const treeNode = {
        ...item,
        key: item.key || item.id,
        children: []
      }

      // 额外的数据转换处理
      if (typeof attributeFill === 'function') {
        attributeFill(treeNode, item)
      }

      // 迭代 list， 找到当前菜单相符合的所有子菜单
      fillTree(list, treeNode.children, item.id, attributeFill)
      // 删掉不存在 children 值的属性
      if (treeNode.children.length <= 0) {
        delete treeNode.children
      }
      // 加入到树中
      tree.push(treeNode)
    }
  })
}

/*
option {
  parentId: num,
  mapping: {
    x : y
  }
}*/
