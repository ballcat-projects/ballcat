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

package org.ballcat.desensitize.json;

/**
 * 脱敏工具类 定义开启脱敏规则
 *
 * @author Yakir
 */
public interface DesensitizeStrategy {

	/**
	 * 判断是否忽略字段
	 * @param fieldName {@code 当前字段名称}
	 * @return @{code true 忽略 |false 不忽略}
	 */
	boolean ignoreField(String fieldName);

}
