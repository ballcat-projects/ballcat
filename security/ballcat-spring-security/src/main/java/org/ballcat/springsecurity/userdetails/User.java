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

package org.ballcat.springsecurity.userdetails;

import java.util.Collection;
import java.util.Map;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author Hccake 2019/9/25 21:03
 */
@ToString
@Getter
@Builder
public class User implements UserDetails {

	/**
	 * 用户ID
	 */
	private final Long userId;

	/**
	 * 登录账号
	 */
	private final String username;

	/**
	 * 密码
	 */
	private final String password;

	/**
	 * 昵称
	 */
	private final String nickname;

	/**
	 * 头像
	 */
	private final String avatar;

	/**
	 * 状态(1-正常,0-冻结)
	 */
	private final Integer status;

	/**
	 * 组织机构ID
	 */
	private final Long organizationId;

	/**
	 * 性别(0-默认未知,1-男,2-女)
	 */
	private final Integer gender;

	/**
	 * 电子邮件
	 */
	private final String email;

	/**
	 * 手机号
	 */
	private final String phoneNumber;

	/**
	 * 用户类型
	 */
	private final Integer type;

	/**
	 * 权限信息列表
	 */
	private final Collection<? extends GrantedAuthority> authorities;

	/**
	 * OAuth2User 必须有属性字段
	 */
	private final Map<String, Object> attributes;

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return this.status != null && this.status == 1;
	}

	@SuppressWarnings("unchecked")
	public <A> A getAttribute(String name) {
		return (A) getAttributes().get(name);
	}

	public Map<String, Object> getAttributes() {
		return this.attributes;
	}

	public String getName() {
		return this.username;
	}

}
