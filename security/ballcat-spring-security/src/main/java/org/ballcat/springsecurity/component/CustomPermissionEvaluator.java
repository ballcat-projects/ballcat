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

package org.ballcat.springsecurity.component;

import java.util.Collection;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.PatternMatchUtils;
import org.springframework.util.StringUtils;

/**
 * 自定义权限验证，支持简单的 * 通配符
 */
@Slf4j
public class CustomPermissionEvaluator {

	/**
	 * 判断接口是否有xxx:xxx权限
	 * @param permission 权限
	 * @return {boolean}
	 */
	public boolean hasPermission(String permission) {
		if (!StringUtils.hasText(permission)) {
			return false;
		}
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			return false;
		}
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		return authorities.stream()
			.map(GrantedAuthority::getAuthority)
			.filter(StringUtils::hasText)
			.anyMatch(x -> PatternMatchUtils.simpleMatch(permission, x));
	}

}
