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

	private static ThreadLocal<AtomicInteger> matchNumTreadLocal;

	/**
	 * 创建 SQL 数据权限匹配次数的 TreadLocal，每次 SQL 执行解析前创建
	 */
	public static void create() {
		matchNumTreadLocal = ThreadLocal.withInitial(AtomicInteger::new);
	}

	/**
	 * 获取当前 SQL 解析后被数据权限匹配中的次数
	 * @return int 次数
	 */
	public static int getMatchNum() {
		AtomicInteger matchNum = matchNumTreadLocal.get();
		return matchNum.get();
	}

	/**
	 * 如果存在计数器，则次数 +1
	 */
	public static void incrementMatchNumIfPresent() {
		Optional.ofNullable(matchNumTreadLocal).map(ThreadLocal::get).ifPresent(AtomicInteger::incrementAndGet);
	}

	/**
	 * 删除 matchNumTreadLocal，在 SQL 执行解析后调用
	 */
	public static void remove() {
		matchNumTreadLocal.remove();
	}

}
