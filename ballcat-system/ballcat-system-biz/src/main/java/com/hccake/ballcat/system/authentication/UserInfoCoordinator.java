package com.hccake.ballcat.system.authentication;

import com.hccake.ballcat.system.model.dto.UserInfoDTO;

import java.util.Map;

/**
 * 用户附属属性协调
 *
 * @author author.zero
 * @version 1.0 2021/11/16 20:02
 */
public interface UserInfoCoordinator {

	/**
	 * 对于不同类型的用户，可能在业务上需要获取到不同的用户属性 实现此接口，进行用户属性的增强
	 * @param userInfoDTO 系统用户信息
	 * @param attribute 用户属性，默认添加了 roles 和 permissions 属性
	 * @return attribute
	 */
	Map<String, Object> coordinateAttribute(UserInfoDTO userInfoDTO, Map<String, Object> attribute);

}
