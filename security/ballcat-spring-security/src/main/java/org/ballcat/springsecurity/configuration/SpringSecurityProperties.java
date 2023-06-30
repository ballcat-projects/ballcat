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
package org.ballcat.springsecurity.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

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
	 * 登录验证码开关
	 */
	private boolean loginCaptchaEnabled = false;

	/**
	 * 开启服务端登录页
	 */
	private boolean loginPageEnabled = false;

	/**
	 * 登录地址
	 * <p>
	 * - 不配置将使用 security 默认的登录页：/login <br>
	 * - 配置后则必须自己提供登录页面
	 */
	private String loginPage = null;

	/**
	 * 无状态登录
	 */
	private boolean stateless = false;

}
