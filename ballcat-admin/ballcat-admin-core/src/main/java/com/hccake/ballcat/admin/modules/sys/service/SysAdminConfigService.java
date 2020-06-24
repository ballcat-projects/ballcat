package com.hccake.ballcat.admin.modules.sys.service;

import com.hccake.ballcat.admin.modules.sys.model.entity.SysUser;

/**
 * 超级管理员账户规则配置
 *
 * @author lingting 2020-06-24 21:00:15
 */
public interface SysAdminConfigService {

	/**
	 * 校验用户是否为超级管理员
	 * @param user 用户信息
	 * @return boolean
	 * @author lingting 2020-06-24 21:07:33
	 */
	boolean verify(SysUser user);

}
