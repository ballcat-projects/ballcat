package com.hccake.ballcat.common.desensitize.handler;

/**
 * 【6*脱敏】，不管原文是什么，一律返回6个* eg. ******
 *
 * @author Hccake 2021/1/22
 * @version 1.0
 */
public class SixAsteriskDesensitizationHandler implements SimpleDesensitizationHandler {

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
