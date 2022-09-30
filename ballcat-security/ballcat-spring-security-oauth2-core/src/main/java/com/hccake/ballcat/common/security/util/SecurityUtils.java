package com.hccake.ballcat.common.security.util;

import com.hccake.ballcat.common.security.userdetails.ClientPrincipal;
import com.hccake.ballcat.common.security.userdetails.User;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

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
	 * 获取客户端信息
	 */
	public ClientPrincipal getClientPrincipal() {
		Authentication authentication = getAuthentication();
		if (authentication == null) {
			return null;
		}
		Object principal = authentication.getPrincipal();
		if (principal instanceof ClientPrincipal) {
			return (ClientPrincipal) principal;
		}
		return null;
	}

}
