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

/**
 * Boolean 类型常量
 *
 * @author Hccake 2020/4/6 21:52
 */
@AllArgsConstructor
public enum BooleanEnum {

	/**
	 * 是
	 */
	TRUE(true, 1),
	/**
	 * 否
	 */
	FALSE(false, 0);

	private final Boolean booleanValue;

	private final Integer intValue;

	public Boolean booleanValue() {
		return this.booleanValue;
	}

	public Integer intValue() {
		return this.intValue;
	}

}
