package com.hccake.ballcat.common.tenant.core;

import lombok.Data;

/**
 * 连接池属性
 *
 * @author huyuanzhi
 */
@Data
public class TenantDataSource {

	/**
	 * 连接池名称
	 */
	private String poolName;

	/**
	 * JDBC url 地址
	 */
	private String url;

	/**
	 * JDBC 用户名
	 */
	private String username;

	/**
	 * JDBC 密码
	 */
	private String password;

}
