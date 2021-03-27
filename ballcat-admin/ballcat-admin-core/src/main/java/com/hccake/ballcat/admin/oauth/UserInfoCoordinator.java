package com.hccake.ballcat.admin.oauth;

import com.hccake.ballcat.admin.modules.sys.model.entity.SysUser;
import com.hccake.ballcat.admin.oauth.domain.DefaultUserAttributes;
import com.hccake.ballcat.admin.oauth.domain.DefaultUserResources;
import com.hccake.ballcat.admin.oauth.domain.UserAttributes;
import com.hccake.ballcat.admin.oauth.domain.UserResources;

import java.util.Set;

/**
 * 用户信息协调者 子类重写该类，用于对用户资源或者用户属性的自定义调整
 *
 * @author Hccake 2020/9/28
 * @version 1.0
 */
public class UserInfoCoordinator {

	/**
	 * 获取用户资源关联Map 用户资源协调，用于管理用户与其拥有资源的关系 子类重写此方法，进行用户资源的管理，删除或者添加一些业务资源控制
	 * @param user 用户信息
	 * @param roles 角色标识集合
	 * @param permissions 权限标识集合
	 * @return DefaultUserResources 默认用户资源类，拥有角色和权限标识属性
	 */
	public UserResources coordinateResource(SysUser user, Set<String> roles, Set<String> permissions) {
		// 用户资源，角色和权限
		DefaultUserResources userResources = new DefaultUserResources();
		userResources.setRoles(roles);
		userResources.setPermissions(permissions);
		return userResources;
	}

	/**
	 * 用户附属属性协调 对于不同类型的用户，可能在业务上需要获取到不同的用户属性 子类重写此方法，进行用户属性的增强
	 * @param sysUser 系统用户
	 * @return DefaultUserAttributes 无属性的用户属性类
	 */
	public UserAttributes coordinateAttribute(SysUser sysUser) {
		return new DefaultUserAttributes();
	}

}
