package com.hccake.ballcat.admin.oauth;

import com.hccake.ballcat.admin.constants.UserResourceConstant;
import com.hccake.ballcat.admin.modules.sys.converter.SysUserConverter;
import com.hccake.ballcat.admin.modules.sys.model.vo.SysUserPageVO;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.Collection;
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
		SysUserPageVO sysUserPageVO = SysUserConverter.INSTANCE.poToPageVo(sysUserDetails.getSysUser());

		Map<String, Collection<?>> userResources = sysUserDetails.getUserResources();

		additionalInfo.put("info", sysUserPageVO);
		additionalInfo.put("roles", userResources.get(UserResourceConstant.RESOURCE_ROLE));
		additionalInfo.put("permissions", userResources.get(UserResourceConstant.RESOURCE_PERMISSION));

		((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);

		return accessToken;
	}

}
