package com.hccake.ballcat.common.core.desensite;

import com.hccake.ballcat.common.core.desensite.handler.SixAsteriskDesensitizationHandler;

/**
 * 默认的一些脱敏方式
 *
 * @author Hccake 2021/1/22
 * @version 1.0
 */
public final class SimpleDesensitizationTypeConstant {

	private SimpleDesensitizationTypeConstant() {
	}

	/**
	 * 定长脱敏，总是6个星号，不管原文是啥
	 * @see SixAsteriskDesensitizationHandler
	 */
	public static final String SIX_ASTERISK = "FIXED_LENGTH";

}
