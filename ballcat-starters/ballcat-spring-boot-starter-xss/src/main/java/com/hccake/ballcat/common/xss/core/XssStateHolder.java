package com.hccake.ballcat.common.xss.core;

/**
 * Xss状态持有者
 *
 * @author Hccake 2021/3/16
 * @version 1.0
 */
public final class XssStateHolder {

	private XssStateHolder() {
	}

	private static final ThreadLocal<Boolean> STATE = new ThreadLocal<>();

	/**
	 * 打开 Xss 过滤
	 */
	public static void open() {
		STATE.set(Boolean.TRUE);
	}

	/**
	 * Xss 过滤是否开启
	 * @return 开启：true
	 */
	public static boolean enabled() {
		return Boolean.TRUE.equals(STATE.get());
	}

	/**
	 * 删除状态
	 */
	public static void remove() {
		STATE.remove();
	}

}
