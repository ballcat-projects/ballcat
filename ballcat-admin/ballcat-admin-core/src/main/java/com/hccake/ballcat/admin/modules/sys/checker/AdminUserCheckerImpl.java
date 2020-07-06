package com.hccake.ballcat.admin.modules.sys.checker;

import cn.hutool.core.util.StrUtil;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysUser;
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

}
