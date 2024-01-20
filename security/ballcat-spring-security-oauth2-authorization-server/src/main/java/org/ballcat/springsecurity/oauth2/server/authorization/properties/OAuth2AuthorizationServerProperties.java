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

package org.ballcat.springsecurity.oauth2.server.authorization.properties;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationEndpointConfigurer;

/**
 * 授权服务器的配置文件
 *
 * @author hccake
 */
@Getter
@Setter
@ConfigurationProperties(prefix = OAuth2AuthorizationServerProperties.PREFIX)
public class OAuth2AuthorizationServerProperties {

	public static final String PREFIX = "ballcat.springsecurity.oauth2.authorizationserver";

	/**
	 * 用户同意授权页面
	 *
	 * @see OAuth2AuthorizationEndpointConfigurer#consentPage
	 */
	private String consentPage;

	/**
	 * 密码授权模式
	 */
	private PasswordGrantType passwordGrantType;

	@Data
	public static class PasswordGrantType {

		/**
		 * 开启 Resource owner password grant type 类型的支持
		 */
		private boolean enabled;

		/**
		 * 登录验证码校验
		 */
		private boolean loginCaptcha;

	}

}
