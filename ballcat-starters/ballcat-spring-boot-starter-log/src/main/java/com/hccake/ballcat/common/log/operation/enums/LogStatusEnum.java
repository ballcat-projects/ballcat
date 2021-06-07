package com.hccake.ballcat.common.log.operation.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Hccake
 * @version 1.0
 * @date 2020/5/15 16:47 操作状态枚举
 */
@Getter
@AllArgsConstructor
public enum LogStatusEnum {

	/**
	 * 成功
	 */
	SUCCESS(1),
	/**
	 * 失败
	 */
	FAIL(0);

	private final int value;

}
