package com.hccake.ballcat.admin.oauth;

import cn.hutool.core.collection.CollectionUtil;
import com.hccake.ballcat.admin.modules.system.model.dto.UserInfoDTO;
import com.hccake.ballcat.admin.modules.system.model.entity.SysUser;
import com.hccake.ballcat.admin.modules.system.service.SysUserService;
import com.hccake.ballcat.admin.oauth.domain.UserAttributes;
import com.hccake.ballcat.admin.oauth.domain.UserResources;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/9/25 20:44
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysUserDetailsServiceImpl implements UserDetailsService {

	private final SysUserService sysUserService;

	private final UserInfoCoordinator userInfoCoordinator;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		SysUser sysUser = sysUserService.getByUsername(username);
		if (sysUser == null) {
			log.error("登陆：用户名错误，用户名：{}", username);
			throw new UsernameNotFoundException("username error!");
		}
		UserInfoDTO userInfoDTO = sysUserService.findUserInfo(sysUser);
		return getUserDetailsByUserInfo(userInfoDTO);
	}

	/**
	 * 根据UserInfo 获取 UserDetails
	 * @param userInfoDTO 用户信息DTO
	 * @return UserDetails
	 */
	public UserDetails getUserDetailsByUserInfo(UserInfoDTO userInfoDTO) {

		SysUser sysUser = userInfoDTO.getSysUser();
		List<String> roles = userInfoDTO.getRoles();
		List<String> permissions = userInfoDTO.getPermissions();

		Set<String> dbAuthsSet = new HashSet<>();
		if (CollectionUtil.isNotEmpty(roles)) {
			// 获取角色
			dbAuthsSet.addAll(roles);
			// 获取资源
			dbAuthsSet.addAll(permissions);

		}
		Collection<? extends GrantedAuthority> authorities = AuthorityUtils
				.createAuthorityList(dbAuthsSet.toArray(new String[0]));

		// 用户资源，角色和权限
		UserResources userResources = userInfoCoordinator.coordinateResource(sysUser, new HashSet<>(roles),
				new HashSet<>(permissions));
		// 用户额外属性
		UserAttributes userAttributes = userInfoCoordinator.coordinateAttribute(sysUser);

		return new SysUserDetails(sysUser, authorities, userResources, userAttributes);
	}

}
