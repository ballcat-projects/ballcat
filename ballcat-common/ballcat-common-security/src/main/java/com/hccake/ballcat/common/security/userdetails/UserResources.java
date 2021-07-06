package com.hccake.ballcat.common.security.userdetails;

import java.io.Serializable;
import java.util.Collection;

/**
 * 用户资源类
 *
 * @author Hccake 2021/3/25
 * @version 1.0
 */
public interface UserResources extends Serializable {

	/**
	 * 获取当前用户所拥有的角色标识集合
	 * @return Collection<String>
	 */
	Collection<String> getRoles();

	/**
	 * 获取当前用户所拥有的权限标识集合
	 * @return Collection<String>
	 */
	Collection<String> getPermissions();

}
