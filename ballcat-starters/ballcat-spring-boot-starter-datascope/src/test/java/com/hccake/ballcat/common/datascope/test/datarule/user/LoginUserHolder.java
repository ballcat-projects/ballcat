package com.hccake.ballcat.common.datascope.test.datarule.user;

/**
 * 登录用户上下文
 *
 * @author hccake
 */
public final class LoginUserHolder {

	private LoginUserHolder() {
	}

	private static final ThreadLocal<LoginUser> LOGIN_USER = new ThreadLocal<>();

	/**
	 * 获取当前登录用户
	 */
	public static LoginUser get() {
		return LOGIN_USER.get();
	}

	/**
	 * 设置当前登录用户
	 */
	public static void set(LoginUser loginUser) {
		LOGIN_USER.set(loginUser);
	}

	/**
	 * 清除当前登录用户
	 */
	public static void remove() {
		LOGIN_USER.remove();
	}

}
