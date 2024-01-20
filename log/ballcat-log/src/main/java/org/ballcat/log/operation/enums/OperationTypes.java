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

package org.ballcat.log.operation.enums;

/**
 * 操作类型的接口，用户可以继承此接口以便自定义类型
 *
 * @author hccake
 */
public final class OperationTypes {

	private OperationTypes() {
	}

	/**
	 * 其他操作
	 */
	public static final int OTHER = 0;

	/**
	 * 导入操作
	 */
	public static final int IMPORT = 1;

	/**
	 * 导出操作
	 */
	public static final int EXPORT = 2;

	/**
	 * 查看操作，主要用于敏感数据查询记录
	 */
	public static final int READ = 3;

	/**
	 * 新建操作
	 */
	public static final int CREATE = 4;

	/**
	 * 修改操作
	 */
	public static final int UPDATE = 5;

	/**
	 * 删除操作
	 */
	public static final int DELETE = 6;

}
