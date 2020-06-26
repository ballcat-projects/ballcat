package com.hccake.ballcat.admin.modules.sys.service.impl;

import com.hccake.ballcat.admin.modules.sys.config.AdminRuleConfig;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysUser;
import com.hccake.ballcat.admin.modules.sys.service.SysAdminConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 超级管理员账户规则配置
 *
 * @author lingting 2020-06-24 21:00:15
 */
@Service
@RequiredArgsConstructor
public class SysAdminConfigServiceImpl implements SysAdminConfigService {

	private final AdminRuleConfig ruleConfig;

	private List<Pattern> usernameRegList;

	private List<Pattern> emailRegList;

	private List<Pattern> phoneRegList;

	@Override
	public boolean verify(SysUser user) {
		if (ruleConfig.getId().contains(user.getUserId().toString())
				|| ruleConfig.getUsername().contains(user.getUsername())
				|| ruleConfig.getEmail().contains(user.getEmail()) || ruleConfig.getPhone().contains(user.getPhone())) {
			return true;
		}

		return verifyReg(usernameRegList, user.getUsername()) || verifyReg(emailRegList, user.getEmail())
				|| verifyReg(phoneRegList, user.getPhone());
	}

	private boolean verifyReg(List<Pattern> patterns, String value) {
		for (Pattern pattern : patterns) {
			if (pattern.matcher(value).matches()) {
				return true;
			}
		}
		return false;
	}

	@PostConstruct
	public void init() {
		usernameRegList = ruleConfig.getUsernameReg().stream().map(Pattern::compile).collect(Collectors.toList());

		emailRegList = ruleConfig.getEmailReg().stream().map(Pattern::compile).collect(Collectors.toList());

		phoneRegList = ruleConfig.getPhoneReg().stream().map(Pattern::compile).collect(Collectors.toList());
	}

}
