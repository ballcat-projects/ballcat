package com.hccake.ballcat.common.core.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Boolean 类型常量
 *
 * @author Hccake 2020/4/6 21:52
 */
@AllArgsConstructor
public enum BooleanEnum {

	/**
	 * 是
	 */
	TRUE(true, 1),
	/**
	 * 否
	 */
	FALSE(false, 0);

	private final Boolean booleanValue;

	private final Integer intValue;

	public Boolean booleanValue() {
		return booleanValue;
	}

	public Integer intValue() {
		return intValue;
	}

}
