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

package org.ballcat.common.core.constant;

/**
 * @author Hccake 2020/6/9 17:17
 */
public final class GlobalConstants {

	private GlobalConstants() {
	}

	/**
	 * 未被逻辑删除的标识，即有效数据标识 逻辑删除标识，普通情况下可以使用 1 标识删除，0 标识存活 但在有唯一索引的情况下，会导致索引冲突，所以用 0 标识存活，
	 * 已删除数据记录为删除时间戳
	 */
	public static final Long NOT_DELETED_FLAG = 0L;

	/**
	 * 生产环境
	 */
	public static final String ENV_PROD = "prod";

	/**
	 * 树根节点ID
	 */
	public static final Integer TREE_ROOT_ID = 0;

	/**
	 * 树根节点ID
	 */
	public static final Long TREE_ROOT_ID_LONG = Long.valueOf(TREE_ROOT_ID);

}
