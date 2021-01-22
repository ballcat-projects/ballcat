package com.hccake.ballcat.common.core.desensite;

/**
 * 脱敏处理器
 *
 * TODO 复用中央脱敏和正则脱敏，在注解中自定义属性而不必实现一个 Handler
 *
 * @author Hccake 2021/1/22
 * @version 1.0
 */
public interface DesensitizationHandler {

	/**
	 * 脱敏类型
	 * @return 类型
	 */
	String getType();

	/**
	 * 脱敏处理
	 * @param origin 原始字符串
	 * @return 脱敏处理后的字符串
	 */
	String handle(String origin);

}
