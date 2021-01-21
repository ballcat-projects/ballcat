package com.hccake.ballcat.admin.oauth.util;

import com.hccake.ballcat.admin.constants.SecurityConst;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysUser;
import com.hccake.ballcat.admin.oauth.SysUserDetails;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.util.Optional;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/10/15 11:19
 */
@UtilityClass
public class SecurityUtils {

	/**
	 * 获取Authentication
	 */
	public Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	/**
	 * 获取系统用户Details
	 * @param authentication 令牌
	 * @return SysUser
	 * <p>
	 */
	public SysUserDetails getSysUserDetails(Authentication authentication) {
		if (authentication == null) {
			return null;
		}
		Object principal = authentication.getPrincipal();
		if (principal instanceof SysUserDetails) {
			return (SysUserDetails) principal;
		}
		return null;
	}

	/**
	 * 获取用户详情
	 */
	public SysUserDetails getSysUserDetails() {
		Authentication authentication = getAuthentication();
		return getSysUserDetails(authentication);
	}

	/**
	 * 获取系统用户
	 */
	public SysUser getSysUser() {
		SysUserDetails sysUserDetails = getSysUserDetails();
		return sysUserDetails == null ? null : sysUserDetails.getSysUser();
	}

	/**
	 * 判断当前是否是测试客户端
	 * @return boolean 是：true，否：false
	 */
	public boolean isTestClient() {
		// 测试客户端 跳过密码解密（swagger 或 postman测试时使用）
		Authentication authentication = SecurityUtils.getAuthentication();
		User user = (User) Optional.ofNullable(authentication).map(Authentication::getPrincipal).orElse(null);
		return user != null && SecurityConst.TEST_CLIENT_ID.equals(user.getUsername());
	}

}
