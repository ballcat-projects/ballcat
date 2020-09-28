package com.hccake.ballcat.common.datascope;

import java.util.HashMap;
import java.util.Map;

/**
 * DataScope 持有类 内部维护一个 ThreadLocal，用于存储当前用户的所有 DataScope
 *
 * @author Hccake 2020/9/27
 * @version 1.0
 */
public class DataScopeHolder {

	private DataScopeHolder() {
	}

	private static final ThreadLocal<Map<String, DataScope>> DATA_SCOPE_LOCAL = new InheritableThreadLocal<>();

	public static Map<String, DataScope> getDataScopes() {
		return DataScopeHolder.DATA_SCOPE_LOCAL.get();
	}

	public static void setDataScopes(Map<String, DataScope> dataScopes) {
		DataScopeHolder.DATA_SCOPE_LOCAL.set(dataScopes);
	}

	public static DataScope putDataScope(String key, DataScope dataScope) {
		if (DataScopeHolder.getDataScopes() == null) {
			setDataScopes(new HashMap<>(8));
		}
		return DataScopeHolder.getDataScopes().put(key, dataScope);
	}

	public static DataScope removeDataScope(String key) {
		return DataScopeHolder.getDataScopes().remove(key);
	}

	public static void clear() {
		DATA_SCOPE_LOCAL.remove();
	}

}
