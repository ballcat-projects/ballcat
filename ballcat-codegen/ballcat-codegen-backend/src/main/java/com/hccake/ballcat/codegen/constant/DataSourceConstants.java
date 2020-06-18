package com.hccake.ballcat.codegen.constant;

/**
 * @author Hccake
 * @version 1.0
 * @date 2020/6/15 17:53
 * 配置数据源使用的常量
 */
public final class DataSourceConstants {
	private DataSourceConstants(){}


	/**
	 * 默认数据源名称（master）
	 */
	public static final String DEFAULT_DS_NAME = "master";

	/**
	 * 数据源名称 KEY
	 */
	public static final String DS_NAME_KEY = "name";

	/**
	 * 数据源连接 username KEY
	 */
	public static final String DS_USERNAME_KEY = "username";

	/**
	 * 数据源连接 password KEY
	 */
	public static final String DS_PASSWORD_KEY = "password";

	/**
	 * 数据源连接 url KEY
	 */
	public static final String DS_URL_KEY = "url";

}
