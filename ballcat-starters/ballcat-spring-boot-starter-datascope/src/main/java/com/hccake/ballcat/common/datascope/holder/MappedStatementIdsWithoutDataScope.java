package com.hccake.ballcat.common.datascope.holder;

import com.hccake.ballcat.common.datascope.DataScope;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * 该类用于存储，不需数据权限处理的 mappedStatementId 集合
 *
 * @author hccake
 */
public class MappedStatementIdsWithoutDataScope {

	/**
	 * key: DataScope class，value: 该 DataScope 不需要处理的 mappedStatementId 集合
	 */
	private static final Map<Class<? extends DataScope>, HashSet<String>> WITHOUT_MAPPED_STATEMENT_ID_MAP = new HashMap<>();

	/**
	 * 给所有的 DataScope 对应的忽略列表添加对应的 mappedStatementId
	 * @param dataScopeList 数据范围集合
	 * @param mappedStatementId mappedStatementId
	 */
	public static void addToWithoutSet(List<DataScope> dataScopeList, String mappedStatementId) {
		for (DataScope dataScope : dataScopeList) {
			Class<? extends DataScope> dataScopeClass = dataScope.getClass();
			HashSet<String> set = WITHOUT_MAPPED_STATEMENT_ID_MAP.computeIfAbsent(dataScopeClass,
					key -> new HashSet<>());
			set.add(mappedStatementId);
		}
	}

	/**
	 * 是否可以忽略权限控制，检查当前 mappedStatementId 是否存在于所有需要控制的 dataScope 对应的忽略列表中
	 * @param dataScopeList 数据范围集合
	 * @param mappedStatementId mappedStatementId
	 * @return 忽略控制返回 true
	 */
	public static boolean onAllWithoutSet(List<DataScope> dataScopeList, String mappedStatementId) {
		for (DataScope dataScope : dataScopeList) {
			Class<? extends DataScope> dataScopeClass = dataScope.getClass();
			HashSet<String> set = WITHOUT_MAPPED_STATEMENT_ID_MAP.computeIfAbsent(dataScopeClass,
					key -> new HashSet<>());
			if (!set.contains(mappedStatementId)) {
				return false;
			}
		}
		return true;
	}

}
