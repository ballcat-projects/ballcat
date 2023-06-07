/*
 * Copyright 2023 the original author or authors.
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

import org.ballcat.security.oauth2.constant.TokenAttributeNameConstants;
import org.ballcat.security.oauth2.constant.UserInfoFiledNameConstants;
import org.ballcat.security.oauth2.userdetails.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsSet;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.util.HashMap;
import java.util.Map;

/**
 * 自定义 OAuth2TokenCustomizer，处理 BallCat 提供的 User 属性存储，以便在自省时返回对应信息
 *
 * @see User
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
		if (principal instanceof User) {
			User user = (User) principal;
			Map<String, Object> attributes = user.getAttributes();
			claims.claim(TokenAttributeNameConstants.ATTRIBUTES, attributes);
			HashMap<String, Object> userInfoMap = getUserInfoMap(user);
			claims.claim(TokenAttributeNameConstants.INFO, userInfoMap);
			claims.claim(TokenAttributeNameConstants.IS_CLIENT, false);
		}
	}

	private static HashMap<String, Object> getUserInfoMap(User user) {
		HashMap<String, Object> userInfo = new HashMap<>(6);
		userInfo.put(UserInfoFiledNameConstants.USER_ID, user.getUserId());
		userInfo.put(UserInfoFiledNameConstants.TYPE, user.getType());
		userInfo.put(UserInfoFiledNameConstants.ORGANIZATION_ID, user.getOrganizationId());
		userInfo.put(UserInfoFiledNameConstants.USERNAME, user.getUsername());
		userInfo.put(UserInfoFiledNameConstants.NICKNAME, user.getNickname());
		userInfo.put(UserInfoFiledNameConstants.AVATAR, user.getAvatar());
		userInfo.put(UserInfoFiledNameConstants.EMAIL, user.getEmail());
		userInfo.put(UserInfoFiledNameConstants.GENDER, user.getGender());
		userInfo.put(UserInfoFiledNameConstants.PHONE_NUMBER, user.getPhoneNumber());
		userInfo.put(UserInfoFiledNameConstants.STATUS, user.getStatus());
		return userInfo;
	}

}
