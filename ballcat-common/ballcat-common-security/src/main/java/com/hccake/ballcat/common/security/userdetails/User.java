package com.hccake.ballcat.common.security.userdetails;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/9/25 21:03
 */
@ToString
@Getter
@Builder
public class User implements UserDetails, OAuth2User {

	/**
	 * 用户ID
	 */
	private final Integer userId;

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
	private final Integer organizationId;

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

	public User(Integer userId, String username, String password, String nickname, String avatar, Integer status,
			Integer organizationId, Integer type, Collection<? extends GrantedAuthority> authorities,
			Map<String, Object> attributes) {
		this.userId = userId;
		this.username = username;
		this.password = password;
		this.nickname = nickname;
		this.avatar = avatar;
		this.status = status;
		this.organizationId = organizationId;
		this.type = type;
		this.authorities = authorities;
		this.attributes = attributes;
	}

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

	@Override
	public Map<String, Object> getAttributes() {
		return this.attributes;
	}

	@Override
	public String getName() {
		return this.username;
	}

}
