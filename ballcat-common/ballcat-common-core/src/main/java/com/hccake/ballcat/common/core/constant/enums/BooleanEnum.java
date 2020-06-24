package com.hccake.ballcat.common.core.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Hccake
 * @version 1.0
 * @date 2020/4/6 21:52
 */
@Getter
@AllArgsConstructor
public enum BooleanEnum {

	/**
	 * 是
	 */
	TRUE(1),
	/**
	 * 否
	 */
	FALSE(0);

	private final int value;

}
