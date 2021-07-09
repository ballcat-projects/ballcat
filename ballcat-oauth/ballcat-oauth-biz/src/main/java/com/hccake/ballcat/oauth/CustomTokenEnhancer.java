package com.hccake.ballcat.oauth;

import com.hccake.ballcat.common.security.constant.TokenAttributeNameConstants;
import com.hccake.ballcat.common.security.userdetails.User;
import com.hccake.ballcat.system.model.vo.SysUserInfo;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/9/27 21:42 自定义 Token 增强器
 */
public class CustomTokenEnhancer implements TokenEnhancer {

	/**
	 * 处理 token 增强
	 * @param accessToken token信息
	 * @param authentication 鉴权信息
	 * @return OAuth2AccessToken 增强后的token
	 */
	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		final Map<String, Object> additionalInfo = new HashMap<>(8);
		Object principal = authentication.getUserAuthentication().getPrincipal();

		User user = (User) principal;

		// 用户基本信息
		SysUserInfo sysUserInfo = getSysUserInfo(user);
		additionalInfo.put(TokenAttributeNameConstants.INFO, sysUserInfo);

		// 默认在登陆时只把角色和权限的信息返回
		Map<String, Object> resultAttributes = new HashMap<>(2);
		Map<String, Object> attributes = user.getAttributes();
		resultAttributes.put(TokenAttributeNameConstants.ROLES, attributes.get(TokenAttributeNameConstants.ROLES));
		resultAttributes.put(TokenAttributeNameConstants.PERMISSIONS,
				attributes.get(TokenAttributeNameConstants.PERMISSIONS));
		additionalInfo.put(TokenAttributeNameConstants.ATTRIBUTES, resultAttributes);

		((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);

		return accessToken;
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
