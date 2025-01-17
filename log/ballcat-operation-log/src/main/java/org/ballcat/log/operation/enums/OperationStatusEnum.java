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

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 操作日志状态枚举。
 *
 * @author Hccake 2020/5/15 16:47
 * @since 2.0.0
 */
@Getter
@AllArgsConstructor
public enum OperationStatusEnum {

	/**
	 * 操作成功
	 */
	SUCCESS(1),

	/**
	 * 操作失败
	 */
	FAILURE(0),

	/**
	 * 执行出现错误
	 */
	EXECUTE_ERROR(-1);

	private final int value;

}
