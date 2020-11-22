package com.hccake.ballcat.admin.oauth;

import com.hccake.ballcat.admin.modules.sys.model.entity.SysUser;

import java.util.Collection;
import java.util.Map;

/**
 * 用户信息协调者 子类重写该类，用于对用户资源或者用户属性的自定义调整
 *
 * @author Hccake 2020/9/28
 * @version 1.0
 */
public class UserInfoCoordinator {

	/**
	 * 获取用户资源关联Map 用户资源协调，用于管理用户与其拥有资源的关系 子类重写此方法，进行用户资源的管理，删除或者添加一些业务资源控制
	 * @param userResources 用户资源
	 * @param user 用户信息
	 * @return 用户资源关联Map => key: resource，value: 资源项
	 */
	public Map<String, Collection<?>> coordinateResource(Map<String, Collection<?>> userResources, SysUser user) {
		return userResources;
	}

	/**
	 * 用户附属属性协调 对于不同类型的用户，可能在业务上需要获取到不同的用户属性 子类重写此方法，进行用户属性的增强
	 * @param userAttributes 用户属性
	 * @param sysUser 系统用户
	 * @return Map<String, Object> 用户属性
	 */
	public Map<String, Object> coordinateAttribute(Map<String, Object> userAttributes, SysUser sysUser) {
		return userAttributes;
	}

}
