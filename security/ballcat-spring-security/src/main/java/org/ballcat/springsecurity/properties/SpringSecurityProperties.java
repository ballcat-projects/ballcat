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

package org.ballcat.springsecurity.properties;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 * 定制 spring security 的配置文件
 *
 * @author hccake
 * @since 2.0.0
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "ballcat.springsecurity")
public class SpringSecurityProperties {

	/**
	 * 忽略鉴权的 url 列表
	 */
	private List<String> ignoreUrls = new ArrayList<>();

	/**
	 * 是否禁止嵌入iframe
	 */
	private boolean iframeDeny = true;

	/**
	 * 开启服务端登录页
	 */
	private FormLogin formLogin = new FormLogin();

	/**
	 * session 的创建策略
	 */
	private SessionCreationPolicy sessionCreationPolicy = SessionCreationPolicy.IF_REQUIRED;

	@Data
	public static class FormLogin {

		/**
		 * 开启表单登录支持，默认 false
		 */
		private boolean enabled = false;

		/**
		 * 是否前后端分离，默认 false
		 */
		private boolean separated = false;

		/**
		 * 使用登录验证码，默认 false
		 */
		private boolean loginCaptcha = false;

		/**
		 * 登录地址
		 * <p>
		 * - 不配置将使用 security 默认的登录页：/login <br>
		 * - 配置后则必须自己提供登录页面，前后端分离的时候可以提供全路径，如 "https://xx.com/login"
		 */
		private String loginPage = null;

		/**
		 * 默认登录处理地址等同于 loginPage, 如果前后端分离时配置了 loginPage, 则必须手动指定 loginProcessingUrl
		 */
		private String loginProcessingUrl = null;

		/**
		 * 使用 token 进行认证，默认 false, 使用 cookie JSESSIONID
		 * <p>
		 * 如果使用 token 进行认证，sessionCreationPolicy 尽量使用无状态管理 SessionCreationPolicy.STATELESS
		 */
		private boolean authenticationWithToken = false;

	}

}
