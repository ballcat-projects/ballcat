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

package org.ballcat.springsecurity.oauth2.server.authorization.user;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.ballcat.springsecurity.oauth2.constant.UserAttributeNameConstants;
import org.ballcat.springsecurity.oauth2.userdetails.DefaultOAuth2User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * @author hccake
 */
public final class TestUsers {

	private TestUsers() {
	}

	public static DefaultOAuth2User.DefaultOAuth2UserBuilder user1() {
		List<String> roleCodes = Collections.singletonList("ROLE_admin");
		List<String> permissions = Collections.singletonList("user:read");

		List<SimpleGrantedAuthority> authorities = Stream.of(roleCodes, permissions)
			.flatMap(Collection::stream)
			.map(SimpleGrantedAuthority::new)
			.collect(Collectors.toList());

		// 默认将角色和权限放入属性中
		HashMap<String, Object> attributes = new HashMap<>(8);
		attributes.put(UserAttributeNameConstants.ROLE_CODES, roleCodes);
		attributes.put(UserAttributeNameConstants.PERMISSIONS, permissions);

		return DefaultOAuth2User.builder()
			.userId(1L)
			.type(1)
			.username("user1")
			.password("password1")
			.nickname("用户1")
			.status(1)
			.avatar("http://s.com")
			.organizationId(1L)
			.authorities(authorities)
			.attributes(attributes);
	}

}
