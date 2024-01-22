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

package org.ballcat.datascope.handler;

import java.util.List;

import org.ballcat.datascope.DataScope;

/**
 * 数据权限处理器
 *
 * @author Hccake 2020/9/28
 *
 */
public interface DataPermissionHandler {

	/**
	 * 系统配置的所有的数据范围
	 * @return 数据范围集合
	 */
	List<DataScope> dataScopes();

	/**
	 * 根据权限注解过滤后的数据范围集合
	 * @param mappedStatementId Mapper方法ID
	 * @return 数据范围集合
	 */
	List<DataScope> filterDataScopes(String mappedStatementId);

	/**
	 * 是否忽略权限控制，用于及早的忽略控制，例如管理员直接放行，而不必等到DataScope中再进行过滤处理，提升效率
	 * @return boolean true: 忽略，false: 进行权限控制
	 * @param dataScopeList 当前应用的 dataScope 集合
	 * @param mappedStatementId Mapper方法ID
	 */
	boolean ignorePermissionControl(List<DataScope> dataScopeList, String mappedStatementId);

}
