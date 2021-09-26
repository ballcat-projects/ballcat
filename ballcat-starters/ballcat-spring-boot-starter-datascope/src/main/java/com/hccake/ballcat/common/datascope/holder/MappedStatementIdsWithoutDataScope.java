package com.hccake.ballcat.common.datascope.holder;

import java.util.HashSet;
import java.util.Set;

/**
 * 该类用于存储，不需数据权限处理的 mappedStatementId 集合
 *
 * @author hccake
 */
public class MappedStatementIdsWithoutDataScope {

	private static final Set<String> MAPPED_STATEMENT_IDS = new HashSet<>();

	public static void addStatementId(String mappedStatementId) {
		MAPPED_STATEMENT_IDS.add(mappedStatementId);
	}

	public static boolean contains(String mappedStatementId) {
		return MAPPED_STATEMENT_IDS.contains(mappedStatementId);
	}

}
