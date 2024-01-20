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

package org.ballcat.sms.properties.extra;

import lombok.Data;

/**
 * 用于需要 账号密码的 平台
 *
 * @author lingting 2020/4/26 19:51
 */
@Data
public class Account {

	/**
	 * 账号
	 */
	private String username;

	/**
	 * 密码
	 */
	private String password;

}
