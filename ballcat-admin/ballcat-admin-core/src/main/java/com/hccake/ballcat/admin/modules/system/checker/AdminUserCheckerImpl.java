package com.hccake.ballcat.admin.modules.system.checker;

import cn.hutool.core.util.StrUtil;
import com.hccake.ballcat.admin.modules.system.model.entity.SysUser;
import com.hccake.ballcat.admin.oauth.util.SecurityUtils;
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

	private final AdminRuleProperties adminRuleProperties;

	@Override
	public boolean isAdminUser(SysUser user) {
		if (adminRuleProperties.getUserId() == user.getUserId()) {
			return true;
		}
		return StrUtil.isNotEmpty(adminRuleProperties.getUsername())
				&& adminRuleProperties.getUsername().equals(user.getUsername());
	}

	@Override
	public boolean hasModifyPermission(SysUser targetUser) {
		// 如果需要修改的用户是超级管理员，则只能本人修改
		if (this.isAdminUser(targetUser)) {
			return SecurityUtils.getSysUserDetails().getUsername().equals(targetUser.getUsername());
		}
		return true;
	}

}
