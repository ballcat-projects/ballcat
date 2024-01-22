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

package org.ballcat.springsecurity.oauth2.server.resource.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 资源服务器的配置文件，用于配置 token 鉴定方式。由于目前 ballcat 授权服务器使用 不透明令牌，所以这里也暂时不做 jwt令牌支持的扩展
 *
 * @see org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties
 * @author hccake
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "ballcat.springsecurity.oauth2.resourceserver")
public class OAuth2ResourceServerProperties {

	/**
	 * 资源服务器是否和授权服务器共享 token 存储，共享时资源服务器可以直接从 tokenStore 中获取 token 信息
	 */
	private boolean sharedStoredToken = false;

	/**
	 * 当 sharedStoredToken 为 false 时生效。 主要用于配置远程端点
	 */
	private final Opaquetoken opaqueToken = new Opaquetoken();

	@Getter
	@Setter
	public static class Opaquetoken {

		/**
		 * Client id used to authenticate with the token introspection endpoint.
		 */
		private String clientId;

		/**
		 * Client secret used to authenticate with the token introspection endpoint.
		 */
		private String clientSecret;

		/**
		 * OAuth 2.0 endpoint through which token introspection is accomplished.
		 */
		private String introspectionUri;

	}

}
