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

package org.ballcat.security.authorization;

/**
 * 安全校验器，用来对当前登录主体进行指定的权限校验
 *
 * @author Hccake
 * @see org.springframework.security.access.expression.SecurityExpressionOperations
 * @since 2.0.0
 */
public interface SecurityChecker {

	/**
	 * 确定当前登录主体是否具有指定的权限
	 * @param permission the permission to test (i.e. "book:read")
	 * @return true if the permission is found, else false
	 */
	boolean hasPermission(String permission);

	/**
	 * 确定当前登录主体是否具有指定的任何权限之一
	 * @param permissions the permissions to test (i.e. "book:read", "book:write")
	 * @return true if any of the permissions is found, else false
	 */
	boolean hasAnyPermission(String... permissions);

	/**
	 * <p>
	 * 确定当前登录主体是否具有指定的角色
	 * </p>
	 * <p>
	 * 这与 hasPermission 类似，但是这里传入的是 Role Code，注意这里区别于 spring security
	 * 的同名方法，这里必须传递完整的，包含前缀的 Role Code
	 * </p>
	 * @param role the authority to test (i.e. "ROLE_user")
	 * @return true if the role is found, else false
	 */
	boolean hasRole(String role);

	/**
	 * <p>
	 * 确定当前登录主体是否具有指定的任何角色之一。
	 * </p>
	 * <p>
	 * 这与 hasAnyPermission 类似，但是这里传入的是 Role Code，注意这里区别于 spring security
	 * 的同名方法，这里必须传递完整的，包含前缀的 Role Code
	 * </p>
	 * @param roles the authorities to test (i.e. "ROLE_user", "ROLE_admin")
	 * @return true if any of the role is found, else false
	 */
	boolean hasAnyRole(String... roles);

	/**
	 * 确认当前主体是否是匿名主体的
	 * @return 如果是匿名主体返回 true, 否则 false
	 */
	boolean isAnonymous();

	/**
	 * 确认当前主体是否是已认证的
	 * @return 如果已认证返回 true, 否则 false
	 */
	boolean isAuthenticated();

}
