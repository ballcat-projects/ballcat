package com.hccake.ballcat.common.core.desensite.handler;

import com.hccake.ballcat.common.core.desensite.DesensitizationHandler;
import com.hccake.ballcat.common.core.desensite.DesensitizationTypeConstant;

/**
 * 【固定长度】，不管原文是什么，一律返回6个* eg. ******
 *
 * @author Hccake 2021/1/22
 * @version 1.0
 */
public class FixedLengthDesensitizationHandler implements DesensitizationHandler {

	/**
	 * 脱敏类型
	 * @return 类型
	 */
	@Override
	public String getType() {
		return DesensitizationTypeConstant.FIXED_LENGTH;
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
