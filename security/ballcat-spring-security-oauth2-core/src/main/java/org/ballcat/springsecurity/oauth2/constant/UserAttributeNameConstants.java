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

package org.ballcat.springsecurity.oauth2.constant;

/**
 * @author hccake
 */
public final class UserAttributeNameConstants {

	private UserAttributeNameConstants() {
	}

	/**
	 * 用户角色集合属性
	 */
	public static final String ROLE_CODES = "roleCodes";

	/**
	 * 用户权限集合属性
	 */
	public static final String PERMISSIONS = "permissions";

	/**
	 * 用户数据权限相关信息
	 */
	public static final String USER_DATA_SCOPE = "userDataScope";

}
