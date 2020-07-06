package com.hccake.ballcat.admin.modules.sys.service.impl;

import cn.hutool.core.util.StrUtil;
import com.hccake.ballcat.admin.modules.sys.config.AdminRuleConfig;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysUser;
import com.hccake.ballcat.admin.modules.sys.service.SysAdminConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 超级管理员账户规则配置
 *
 * @author lingting 2020-06-24 21:00:15
 */
@Service
@RequiredArgsConstructor
public class SysAdminConfigServiceImpl implements SysAdminConfigService {
	private final AdminRuleConfig ruleConfig;

	@Override
	public boolean verify(SysUser user) {
		if (ruleConfig.getId() == user.getUserId()) {
			return true;
		}

		return StrUtil.isNotEmpty(ruleConfig.getUsername()) && ruleConfig.getUsername().equals(user.getUsername());
	}
}
