package com.hccake.ballcat.admin.oauth;

import com.hccake.ballcat.admin.constants.SysUserConst;
import com.hccake.ballcat.admin.modules.system.model.entity.SysUser;
import com.hccake.ballcat.admin.oauth.domain.UserAttributes;
import com.hccake.ballcat.admin.oauth.domain.UserResources;
import com.hccake.ballcat.common.core.constant.GlobalConstants;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/9/25 21:03
 */
public class SysUserDetails implements UserDetails {

	private final SysUser sysUser;

	private final Collection<? extends GrantedAuthority> authorities;

	/**
	 * 用户所有的资源 <br/>
	 * 默认有用户角色和权限的标识集合，用户可自己扩展
	 */
	private final UserResources userResources;

	/**
	 * 用户属性 <br/>
	 * 对于不同类型的用户，可能在业务上需要获取到不同的用户属性
	 */
	private final UserAttributes userAttributes;

	public SysUserDetails(SysUser sysUser, Collection<? extends GrantedAuthority> authorities,
			UserResources userResources, UserAttributes userAttributes) {
		this.sysUser = sysUser;
		this.authorities = authorities;
		this.userResources = userResources;
		this.userAttributes = userAttributes;
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

	public UserResources getUserResources() {
		return userResources;
	}

	public UserAttributes getUserAttributes() {
		return userAttributes;
	}

}
