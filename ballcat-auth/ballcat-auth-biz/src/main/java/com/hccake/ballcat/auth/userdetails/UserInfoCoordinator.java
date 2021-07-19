package com.hccake.ballcat.auth.userdetails;

import com.hccake.ballcat.system.model.entity.SysUser;

import java.util.Map;

/**
 * 用户信息协调者 子类重写该类，用于对用户资源或者用户属性的自定义调整
 *
 * @author Hccake 2020/9/28
 * @version 1.0
 */
public class UserInfoCoordinator {

	/**
	 * 用户附属属性协调 对于不同类型的用户，可能在业务上需要获取到不同的用户属性 子类重写此方法，进行用户属性的增强
	 * @param sysUser 系统用户
	 * @param attribute 用户属性，默认添加了 roles 和 permissions 属性
	 * @return attribute
	 */
	public Map<String, Object> coordinateAttribute(SysUser sysUser, Map<String, Object> attribute) {
		return attribute;
	}

}
