package com.hccake.ballcat.common.tenant.core;


/**
 * 保存租户信息
 *
 * @author huyuanzhi
 */
public final class TenantContext {

	private static final ThreadLocal<String> THREAD_LOCAL = new InheritableThreadLocal<>();

	private TenantContext() {
	}

	public static void set(String value) {
		THREAD_LOCAL.set(value);
	}

	public static void clear() {
		THREAD_LOCAL.remove();
	}

	public static String get() {
		return THREAD_LOCAL.get();
	}

}
