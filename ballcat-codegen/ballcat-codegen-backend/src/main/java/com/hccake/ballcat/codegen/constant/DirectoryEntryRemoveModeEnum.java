package com.hccake.ballcat.codegen.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Hccake
 * @version 1.0
 * @date 2020/6/23 14:36 模板目录项类型
 */
@Getter
@RequiredArgsConstructor
public enum DirectoryEntryRemoveModeEnum {

	/**
	 * 保留子节点（子节点上移）
	 */
	RESERVED_CHILD_NODE(1),
	/**
	 * 同时删除子节点
	 */
	REMOVE_CHILD_NODE(2);

	private final Integer type;

}
