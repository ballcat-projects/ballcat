/*
 * Copyright 2023-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ballcat.springsecurity.oauth2.server.authorization.token;

import java.util.HashMap;
import java.util.Map;

import org.ballcat.springsecurity.oauth2.constant.TokenAttributeNameConstants;
import org.ballcat.springsecurity.oauth2.constant.UserInfoFiledNameConstants;
import org.ballcat.springsecurity.oauth2.userdetails.DefaultOAuth2User;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsSet;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

/**
 * 自定义 OAuth2TokenCustomizer，处理 BallCat 提供的 User 属性存储，以便在自省时返回对应信息
 *
 * @see DefaultOAuth2User
 * @author hccake
 */
public class BallcatOAuth2TokenCustomizer implements OAuth2TokenCustomizer<OAuth2TokenClaimsContext> {

	@Override
	public void customize(OAuth2TokenClaimsContext context) {
		OAuth2TokenClaimsSet.Builder claims = context.getClaims();
		Authentication authentication = context.getPrincipal();

		// client token
		if (authentication instanceof OAuth2ClientAuthenticationToken) {
			claims.claim(TokenAttributeNameConstants.IS_CLIENT, true);
			return;
		}

		Object principal = authentication.getPrincipal();
		if (principal instanceof DefaultOAuth2User) {
			DefaultOAuth2User defaultOAuth2User = (DefaultOAuth2User) principal;
			Map<String, Object> attributes = defaultOAuth2User.getAttributes();
			claims.claim(TokenAttributeNameConstants.ATTRIBUTES, attributes);
			HashMap<String, Object> userInfoMap = getUserInfoMap(defaultOAuth2User);
			claims.claim(TokenAttributeNameConstants.INFO, userInfoMap);
			claims.claim(TokenAttributeNameConstants.IS_CLIENT, false);
		}
	}

	private static HashMap<String, Object> getUserInfoMap(DefaultOAuth2User defaultOAuth2User) {
		HashMap<String, Object> userInfo = new HashMap<>(6);
		userInfo.put(UserInfoFiledNameConstants.USER_ID, defaultOAuth2User.getUserId());
		userInfo.put(UserInfoFiledNameConstants.TYPE, defaultOAuth2User.getType());
		userInfo.put(UserInfoFiledNameConstants.ORGANIZATION_ID, defaultOAuth2User.getOrganizationId());
		userInfo.put(UserInfoFiledNameConstants.USERNAME, defaultOAuth2User.getUsername());
		userInfo.put(UserInfoFiledNameConstants.NICKNAME, defaultOAuth2User.getNickname());
		userInfo.put(UserInfoFiledNameConstants.AVATAR, defaultOAuth2User.getAvatar());
		userInfo.put(UserInfoFiledNameConstants.EMAIL, defaultOAuth2User.getEmail());
		userInfo.put(UserInfoFiledNameConstants.GENDER, defaultOAuth2User.getGender());
		userInfo.put(UserInfoFiledNameConstants.PHONE_NUMBER, defaultOAuth2User.getPhoneNumber());
		userInfo.put(UserInfoFiledNameConstants.STATUS, defaultOAuth2User.getStatus());
		return userInfo;
	}

}
