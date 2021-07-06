package com.hccake.ballcat.oauth;

import com.hccake.ballcat.common.security.userdetails.SysUserDetails;
import com.hccake.ballcat.common.security.userdetails.UserResources;
import com.hccake.ballcat.system.converter.SysUserConverter;
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

		SysUserDetails sysUserDetails = (SysUserDetails) principal;
		SysUserInfo sysUserInfo = SysUserConverter.INSTANCE.poToInfo(sysUserDetails.getSysUser());

		UserResources userResources = sysUserDetails.getUserResources();

		additionalInfo.put("info", sysUserInfo);
		additionalInfo.put("roles", userResources.getRoles());
		additionalInfo.put("permissions", userResources.getPermissions());

		((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);

		return accessToken;
	}

}
