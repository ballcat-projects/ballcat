package com.hccake.ballcat.oauth.constant;

/**
 * @author Hccake
 */
public final class SecurityConst {

	private SecurityConst() {
	}

	/**
	 * 刷新
	 */
	public static final String REFRESH_TOKEN = "refresh_token";

	/**
	 * 角色前缀
	 */
	public static final String ROLE_PREFIX = "ROLE_";

	/**
	 * 缓存 oauth 相关前缀
	 */
	public static final String OAUTH_PREFIX = "oauth:";

	/**
	 * 登陆地址
	 */
	public static final String LOGIN_URL = "/oauth/token";

	/**
	 * 测试客户端的client_id
	 */
	public static final String TEST_CLIENT_ID = "test";

}
