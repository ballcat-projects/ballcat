package com.hccake.ballcat.common.security.util;

import com.hccake.ballcat.common.security.constant.SecurityConstants;
import com.hccake.ballcat.common.security.userdetails.User;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

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
	 * @return User
	 * <p>
	 */
	public User getUser(Authentication authentication) {
		if (authentication == null) {
			return null;
		}
		Object principal = authentication.getPrincipal();
		if (principal instanceof User) {
			return (User) principal;
		}
		return null;
	}

	/**
	 * 获取用户详情
	 */
	public User getUser() {
		Authentication authentication = getAuthentication();
		return getUser(authentication);
	}

	/**
	 * 判断当前是否是测试客户端
	 * @return boolean 是：true，否：false
	 */
	public boolean isTestClient() {
		// 测试客户端 跳过密码解密（swagger 或 postman测试时使用）
		Authentication authentication = SecurityUtils.getAuthentication();
		UserDetails userDetails = (UserDetails) Optional.ofNullable(authentication).map(Authentication::getPrincipal)
				.orElse(null);
		return userDetails != null && SecurityConstants.TEST_CLIENT_ID.equals(userDetails.getUsername());
	}

}
