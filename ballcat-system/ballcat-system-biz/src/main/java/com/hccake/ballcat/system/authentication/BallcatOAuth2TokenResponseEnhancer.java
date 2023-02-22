package com.hccake.ballcat.system.authentication;

import com.hccake.ballcat.common.security.constant.TokenAttributeNameConstants;
import com.hccake.ballcat.common.security.constant.UserAttributeNameConstants;
import com.hccake.ballcat.common.security.userdetails.User;
import com.hccake.ballcat.system.model.vo.SysUserInfo;
import org.ballcat.springsecurity.oauth2.server.authorization.web.authentication.OAuth2TokenResponseEnhancer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * token 响应增强
 *
 * @author Hccake
 */
public class BallcatOAuth2TokenResponseEnhancer implements OAuth2TokenResponseEnhancer {

	@Override
	public Map<String, Object> enhance(OAuth2AccessTokenAuthenticationToken accessTokenAuthentication) {
		Object principal = Optional.ofNullable(SecurityContextHolder.getContext())
			.map(SecurityContext::getAuthentication)
			.map(Authentication::getPrincipal)
			.orElse(null);

		// token 附属信息
		Map<String, Object> additionalParameters = accessTokenAuthentication.getAdditionalParameters();
		if (additionalParameters == null) {
			additionalParameters = new HashMap<>(8);
		}

		if (principal instanceof User) {
			User user = (User) principal;
			// 用户基本信息
			SysUserInfo sysUserInfo = getSysUserInfo(user);
			additionalParameters.put(TokenAttributeNameConstants.INFO, sysUserInfo);

			// 默认在登陆时只把角色和权限的信息返回
			Map<String, Object> resultAttributes = new HashMap<>(2);
			Map<String, Object> attributes = user.getAttributes();
			resultAttributes.put(UserAttributeNameConstants.ROLE_CODES,
					attributes.get(UserAttributeNameConstants.ROLE_CODES));
			resultAttributes.put(UserAttributeNameConstants.PERMISSIONS,
					attributes.get(UserAttributeNameConstants.PERMISSIONS));
			additionalParameters.put(TokenAttributeNameConstants.ATTRIBUTES, resultAttributes);
		}

		return additionalParameters;
	}

	/**
	 * 根据 User 对象获取 SysUserInfo
	 * @param user User
	 * @return SysUserInfo
	 */
	public SysUserInfo getSysUserInfo(User user) {
		SysUserInfo sysUserInfo = new SysUserInfo();
		sysUserInfo.setUserId(user.getUserId());
		sysUserInfo.setUsername(user.getUsername());
		sysUserInfo.setNickname(user.getNickname());
		sysUserInfo.setAvatar(user.getAvatar());
		sysUserInfo.setOrganizationId(user.getOrganizationId());
		sysUserInfo.setType(user.getType());
		return sysUserInfo;
	}

}
