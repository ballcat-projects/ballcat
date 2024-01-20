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

package org.ballcat.springsecurity.oauth2.server.authorization.autoconfigure;

import org.ballcat.security.captcha.CaptchaValidator;
import org.ballcat.security.properties.SecurityProperties;
import org.ballcat.springsecurity.oauth2.server.authorization.config.customizer.OAuth2LoginCaptchaConfigurerAdapter;
import org.ballcat.springsecurity.oauth2.server.authorization.config.customizer.OAuth2LoginPasswordDecoderConfigurerAdapter;
import org.ballcat.springsecurity.oauth2.server.authorization.properties.OAuth2AuthorizationServerProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OAuth2 授权服务器的 HttpSecurity 的扩展配置器
 *
 * @author Hccake
 */
@Configuration(proxyBeanMethods = false)
public class OAuth2AuthorizationServerConfigurerAdapterConfiguration {

	/**
	 * 登录验证码配置
	 * @param captchaValidator 验证码验证器
	 * @return FilterRegistrationBean<LoginCaptchaFilter>
	 */
	@Bean
	@ConditionalOnProperty(prefix = OAuth2AuthorizationServerProperties.PREFIX,
			name = "password-grant-type.login-captcha", havingValue = "true")
	public OAuth2LoginCaptchaConfigurerAdapter oAuth2LoginCaptchaConfigurer(CaptchaValidator captchaValidator) {
		return new OAuth2LoginCaptchaConfigurerAdapter(captchaValidator);
	}

	/**
	 * password 模式下，密码入参要求 AES 加密。 在进入令牌端点前，通过过滤器进行解密处理。
	 * @param securityProperties 安全配置相关
	 * @return FilterRegistrationBean<LoginPasswordDecoderFilter>
	 */
	@Bean
	@ConditionalOnProperty(prefix = SecurityProperties.PREFIX, name = "password-secret-key")
	public OAuth2LoginPasswordDecoderConfigurerAdapter oAuth2LoginPasswordDecoderConfigurer(
			SecurityProperties securityProperties) {
		return new OAuth2LoginPasswordDecoderConfigurerAdapter(securityProperties.getPasswordSecretKey());
	}

}
