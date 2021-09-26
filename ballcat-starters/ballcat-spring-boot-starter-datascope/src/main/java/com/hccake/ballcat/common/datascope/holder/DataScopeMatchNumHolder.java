package com.hccake.ballcat.common.datascope.holder;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * DataScope 匹配数
 *
 * @author hccake
 */
public final class DataScopeMatchNumHolder {

	private DataScopeMatchNumHolder() {
	}

	private static ThreadLocal<AtomicInteger> matchNumTreadLocal;

	public static void create() {
		matchNumTreadLocal = ThreadLocal.withInitial(AtomicInteger::new);
	}

	/**
	 * get dataScope
	 * @return dataScopes
	 */
	public static int getMatchNum() {
		AtomicInteger matchNum = matchNumTreadLocal.get();
		return matchNum.get();
	}

	/**
	 * 添加 dataScope
	 */
	public static void incrementMatchNum() {
		AtomicInteger matchNum = matchNumTreadLocal.get();
		matchNum.incrementAndGet();
	}

	/**
	 * 删除 dataScope
	 */
	public static void remove() {
		matchNumTreadLocal.remove();
	}

}
