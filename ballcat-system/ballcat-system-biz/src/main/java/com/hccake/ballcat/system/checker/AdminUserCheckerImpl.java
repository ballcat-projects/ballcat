package com.hccake.ballcat.system.checker;

import cn.hutool.core.text.CharSequenceUtil;
import com.hccake.ballcat.common.security.util.SecurityUtils;
import com.hccake.ballcat.system.model.entity.SysUser;
import com.hccake.ballcat.system.properties.SystemProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 超级管理员账户规则配置
 *
 * @author lingting 2020-06-24 21:00:15
 */
@Service
@RequiredArgsConstructor
public class AdminUserCheckerImpl implements AdminUserChecker {

	private final SystemProperties systemProperties;

	@Override
	public boolean isAdminUser(SysUser user) {
		SystemProperties.Administrator administrator = systemProperties.getAdministrator();
		if (administrator.getUserId() == user.getUserId()) {
			return true;
		}
		return CharSequenceUtil.isNotEmpty(administrator.getUsername())
				&& administrator.getUsername().equals(user.getUsername());
	}

	@Override
	public boolean hasModifyPermission(SysUser targetUser) {
		// 如果需要修改的用户是超级管理员，则只能本人修改
		if (this.isAdminUser(targetUser)) {
			return SecurityUtils.getUser().getUsername().equals(targetUser.getUsername());
		}
		return true;
	}

}
