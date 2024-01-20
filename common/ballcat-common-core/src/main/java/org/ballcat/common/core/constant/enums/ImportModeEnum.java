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

package org.ballcat.common.core.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 当数据以存在时的导入动作
 *
 * @author Hccake
 */
@Getter
@AllArgsConstructor
public enum ImportModeEnum {

	/**
	 * 跳过已存在的数据
	 */
	SKIP_EXISTING,

	/**
	 * 覆盖已存在的数据
	 */
	OVERWRITE_EXISTING

}
