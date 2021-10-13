package com.hccake.ballcat.common.datascope.holder;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * DataScope 匹配数
 *
 * @author hccake
 */
public final class DataScopeMatchNumHolder {

	private DataScopeMatchNumHolder() {
	}

	private static final ThreadLocal<AtomicInteger> matchNumTreadLocal = new ThreadLocal<>();

	/**
	 * 每次 SQL 执行解析前初始化匹配次数为 0
	 */
	public static void initMatchNum() {
		matchNumTreadLocal.set(new AtomicInteger());
	}

	/**
	 * 获取当前 SQL 解析后被数据权限匹配中的次数
	 * @return int 次数
	 */
	public static int getMatchNum() {
		return matchNumTreadLocal.get().get();
	}

	/**
	 * 如果存在计数器，则次数 +1
	 */
	public static void incrementMatchNumIfPresent() {
		Optional.ofNullable(matchNumTreadLocal.get()).ifPresent(AtomicInteger::incrementAndGet);
	}

	/**
	 * 删除 matchNumTreadLocal，在 SQL 执行解析后调用
	 */
	public static void remove() {
		matchNumTreadLocal.remove();
	}

}
