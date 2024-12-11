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

package org.ballcat.fastexcel.application;

import lombok.Data;
import org.ballcat.desensitize.annotation.RegexDesensitize;

// 实体对象
@Data
public class DemoData {

	private String username;

	/**
	 * 密码脱敏, 前3后2明文，中间无论多少位，都显示 4 个 *，已混淆位数
	 */
	@RegexDesensitize(regex = "(.{3}).*(.{2}$)", replacement = "$1****$2")
	private String password;

}
