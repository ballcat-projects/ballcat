package com.hccake.ballcat.admin.oauth.util;

import com.hccake.ballcat.admin.oauth.SysUserDetails;
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
     *
     * @param authentication
     * @return SysUser
     * <p>
     */
    public SysUserDetails getSysUserDetails(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof SysUserDetails) {
            return (SysUserDetails) principal;
        }
        return null;
    }

    /**
     * 获取用户
     */
    public SysUserDetails getSysUserDetails() {
        Authentication authentication = getAuthentication();
        return getSysUserDetails(authentication);
    }

}
