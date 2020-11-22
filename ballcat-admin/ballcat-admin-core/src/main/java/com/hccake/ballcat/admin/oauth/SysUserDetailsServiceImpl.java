package com.hccake.ballcat.admin.oauth;

import cn.hutool.core.collection.CollectionUtil;
import com.hccake.ballcat.admin.constants.UserResourceConstant;
import com.hccake.ballcat.admin.modules.sys.model.dto.UserInfoDTO;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysUser;
import com.hccake.ballcat.admin.modules.sys.service.SysUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

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
	private UserDetails getUserDetailsByUserInfo(UserInfoDTO userInfoDTO) {

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
		Map<String, Collection<?>> userResources = new HashMap<>();
		userResources.put(UserResourceConstant.RESOURCE_ROLE, roles);
		userResources.put(UserResourceConstant.RESOURCE_PERMISSION, permissions);
		userResources = userInfoCoordinator.coordinateResource(userResources, sysUser);

		// 用户额外属性
		Map<String, Object> userAttributes = new HashMap<>();
		userAttributes = userInfoCoordinator.coordinateAttribute(userAttributes, sysUser);

		return new SysUserDetails(sysUser, authorities, userResources, userAttributes);
	}

}
