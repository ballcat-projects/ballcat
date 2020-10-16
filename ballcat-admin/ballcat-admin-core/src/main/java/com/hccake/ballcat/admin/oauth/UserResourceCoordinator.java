package com.hccake.ballcat.admin.oauth;

import com.hccake.ballcat.admin.modules.sys.model.entity.SysUser;

import java.util.Collection;
import java.util.Map;

/**
 * 用户资源协调者，用于管理用户与其拥有资源的关系<br/>
 * 实现此类，进行用户资源的管理，删除或者添加一些业务资源控制
 *
 * @author Hccake 2020/9/28
 * @version 1.0
 */
public interface UserResourceCoordinator {

	/**
	 * 获取用户资源关联Map
	 * @param userResources 用户资源
	 * @param user 用户信息
	 * @return 用户资源关联Map => key: resource，value: 资源项
	 */
	Map<String, Collection<?>> coordinate(Map<String, Collection<?>> userResources, SysUser user);

}
