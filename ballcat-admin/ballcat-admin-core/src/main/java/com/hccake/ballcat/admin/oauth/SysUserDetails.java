package com.hccake.ballcat.admin.oauth;

import com.hccake.ballcat.admin.constants.SysUserConst;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysUser;
import com.hccake.ballcat.common.core.constant.GlobalConstants;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/9/25 21:03
 */
public class SysUserDetails implements UserDetails {

    private final SysUser sysUser;

    private final Collection<? extends GrantedAuthority> authorities;

    /**
     * 权限标识集合
     */
    private final List<String> permissions;
    /**
     * 角色集合
     */
    private final List<String> roles;
    /**
     * 角色ID集合
     */
    private final List<Integer> roleIds;

	public SysUserDetails(SysUser sysUser, List<String> roles, List<Integer> roleIds, List<String> permissions, Collection<? extends GrantedAuthority> authorities) {
		this.sysUser = sysUser;
		this.authorities = authorities;
		this.roles = roles;
		this.roleIds = roleIds;
		this.permissions = permissions;
	}

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return sysUser.getPassword();
    }

    @Override
    public String getUsername() {
        return sysUser.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return GlobalConstants.NOT_DELETED_FLAG.equals(sysUser.getDeleted());
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return SysUserConst.Status.NORMAL.getValue().equals(sysUser.getStatus());
    }

    public SysUser getSysUser() {
        return sysUser;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public List<String> getRoles() {
        return roles;
    }

    public List<Integer> getRoleIds() {
        return roleIds;
    }
}
