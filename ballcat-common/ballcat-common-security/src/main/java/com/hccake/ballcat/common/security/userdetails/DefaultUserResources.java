package com.hccake.ballcat.common.security.userdetails;

import lombok.*;

import java.util.Collection;
import java.util.Set;

/**
 * 默认的用户资源类
 *
 * @author Hccake 2021/3/25
 * @version 1.0
 */
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class DefaultUserResources implements UserResources {

	private Set<String> roles;

	private Set<String> permissions;

	/**
	 * 获取当前用户所拥有的角色标识集合
	 * @return List<String>
	 */
	@Override
	public Collection<String> getRoles() {
		return roles;
	}

	/**
	 * 获取当前用户所拥有的权限标识集合
	 * @return List<String>
	 */
	@Override
	public Collection<String> getPermissions() {
		return permissions;
	}

}
