/*
 * Copyright 2023-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ballcat.datascope.holder;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.ballcat.datascope.DataScope;

/**
 * 该类用于存储，不需数据权限处理的 mappedStatementId 集合
 *
 * @author hccake
 */
public final class MappedStatementIdsWithoutDataScope {

	private MappedStatementIdsWithoutDataScope() {
	}

	/**
	 * key: DataScope class，value: 该 DataScope 不需要处理的 mappedStatementId 集合
	 */
	private static final Map<Class<? extends DataScope>, HashSet<String>> WITHOUT_MAPPED_STATEMENT_ID_MAP = new ConcurrentHashMap<>();

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
