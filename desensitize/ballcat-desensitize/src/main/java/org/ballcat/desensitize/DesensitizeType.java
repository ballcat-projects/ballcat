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

package org.ballcat.desensitize;

/**
 * 脱敏类型。
 *
 * @author Hccake
 * @since 2.0.0
 */
public enum DesensitizeType {

	/**
	 * 正则替换。
	 */
	REGEX_REPLACEMENT,

	/**
	 * 滑动脱敏。
	 */
	SLIDE_MASK,

	/**
	 * 索引脱敏。
	 */
	INDEX_MASK,

	/**
	 * 简单处理.
	 */
	SIMPLE_HANDLE;

}
