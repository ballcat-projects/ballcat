package org.ballcat.springsecurity.oauth2.server.authorization.autoconfigure;

import org.ballcat.security.captcha.CaptchaValidator;
import org.ballcat.security.properties.SecurityProperties;
import org.ballcat.springsecurity.oauth2.server.authorization.config.configurer.OAuth2LoginCaptchaConfigurer;
import org.ballcat.springsecurity.oauth2.server.authorization.config.configurer.OAuth2LoginPasswordDecoderConfigurer;
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
public class OAuth2AuthorizationServerExtensionConfigurerConfiguration {

	/**
	 * 登录验证码配置
	 * @param captchaValidator 验证码验证器
	 * @return FilterRegistrationBean<LoginCaptchaFilter>
	 */
	@Bean
	@ConditionalOnProperty(prefix = OAuth2AuthorizationServerProperties.PREFIX, name = "login-captcha-enabled",
			havingValue = "true")
	public OAuth2LoginCaptchaConfigurer oAuth2LoginCaptchaConfigurer(CaptchaValidator captchaValidator) {
		return new OAuth2LoginCaptchaConfigurer(captchaValidator);
	}

	/**
	 * password 模式下，密码入参要求 AES 加密。 在进入令牌端点前，通过过滤器进行解密处理。
	 * @param securityProperties 安全配置相关
	 * @return FilterRegistrationBean<LoginPasswordDecoderFilter>
	 */
	@Bean
	@ConditionalOnProperty(prefix = SecurityProperties.PREFIX, name = "password-secret-key")
	public OAuth2LoginPasswordDecoderConfigurer oAuth2LoginPasswordDecoderConfigurer(
			SecurityProperties securityProperties) {
		return new OAuth2LoginPasswordDecoderConfigurer(securityProperties.getPasswordSecretKey());
	}

}
