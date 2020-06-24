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
public enum DirectoryEntryTypeEnum {

	/**
	 * 文件夹
	 */
	FOLDER(1),
	/**
	 * 文件
	 */
	FILE(2);

	private final Integer type;

}
