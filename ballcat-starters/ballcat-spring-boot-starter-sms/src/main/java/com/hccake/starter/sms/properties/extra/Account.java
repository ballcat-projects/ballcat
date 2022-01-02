package com.hccake.starter.sms.properties.extra;

import lombok.Data;

/**
 * 用于需要 账号密码的 平台
 *
 * @author lingting 2020/4/26 19:51
 */
@Data
public class Account {

	/**
	 * 账号
	 */
	private String username;

	/**
	 * 密码
	 */
	private String password;

}
