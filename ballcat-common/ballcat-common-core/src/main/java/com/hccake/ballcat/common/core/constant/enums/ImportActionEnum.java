package com.hccake.ballcat.common.core.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 当数据以存在时的导入动作
 * @author Hccake
 */
@Getter
@AllArgsConstructor
public enum ImportActionEnum {

	/**
	 * 跳过已存在的数据
	 */
	SKIP_EXISTING,

	/**
	 * 覆盖已存在的数据
	 */
	OVERWRITE_EXISTING;

}
