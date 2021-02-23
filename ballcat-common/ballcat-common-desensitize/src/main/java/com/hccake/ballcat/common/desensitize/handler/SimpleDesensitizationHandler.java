package com.hccake.ballcat.common.desensitize.handler;

/**
 * 简单的脱敏处理器，传入源数据直接返回脱敏后的数据
 *
 * @author Hccake 2021/1/23
 * @version 1.0
 */
public interface SimpleDesensitizationHandler extends DesensitizationHandler {

	/**
	 * 脱敏处理
	 * @param origin 原始字符串
	 * @return 脱敏处理后的字符串
	 */
	String handle(String origin);

}
