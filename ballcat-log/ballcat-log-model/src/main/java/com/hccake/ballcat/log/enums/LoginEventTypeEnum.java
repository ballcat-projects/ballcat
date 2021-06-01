package com.hccake.ballcat.log.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 登陆事件
 * @author Hccake 2020/9/17
 * @version 1.0
 */
@Getter
@AllArgsConstructor
public enum LoginEventTypeEnum {

	/**
	 * 登录
	 */
	LOGIN(1),
	/**
	 * 登出
	 */
	LOGOUT(2);

	private final int value;

}
