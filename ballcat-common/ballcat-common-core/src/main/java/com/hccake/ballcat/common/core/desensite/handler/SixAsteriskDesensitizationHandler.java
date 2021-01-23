package com.hccake.ballcat.common.core.desensite.handler;

/**
 * 【6*脱敏】，不管原文是什么，一律返回6个* eg. ******
 *
 * @author Hccake 2021/1/22
 * @version 1.0
 */
public class SixAsteriskDesensitizationHandler implements SimpleDesensitizationHandler {

	/**
	 * 定长脱敏，总是6个星号，不管原文是啥
	 * @see SixAsteriskDesensitizationHandler
	 */
	public static final String TYPE = "SIX_ASTERISK";

	/**
	 * 脱敏类型
	 * @return 类型
	 */
	@Override
	public String getType() {
		return TYPE;
	}

	/**
	 * 脱敏处理
	 * @param origin 原始字符串
	 * @return 脱敏处理后的字符串
	 */
	@Override
	public String handle(String origin) {
		return "******";
	}

}
