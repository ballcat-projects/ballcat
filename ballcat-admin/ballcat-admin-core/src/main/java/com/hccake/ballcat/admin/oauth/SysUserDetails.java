package com.hccake.ballcat.admin.oauth;

import com.hccake.ballcat.admin.constants.SysUserConst;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysUser;
import com.hccake.ballcat.common.core.constant.GlobalConstants;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Map;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/9/25 21:03
 */
public class SysUserDetails implements UserDetails {

	private final SysUser sysUser;

	private final Collection<? extends GrantedAuthority> authorities;

	/**
	 * 用户所有的资源Map <br/>
	 * key: resource标识 value: resourceItem <br/>
	 * 以角色为例 => role: roleCodeList
	 */
	private final Map<String, Collection<?>> userResources;

	public SysUserDetails(SysUser sysUser, Collection<? extends GrantedAuthority> authorities,
			Map<String, Collection<?>> userResources) {
		this.sysUser = sysUser;
		this.authorities = authorities;
		this.userResources = userResources;
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

	public Map<String, Collection<?>> getUserResources() {
		return userResources;
	}

}
