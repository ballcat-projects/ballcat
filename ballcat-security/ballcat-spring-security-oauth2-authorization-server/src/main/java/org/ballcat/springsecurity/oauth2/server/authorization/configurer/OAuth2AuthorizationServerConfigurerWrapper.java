package org.ballcat.springsecurity.oauth2.server.authorization.configurer;

import cn.hutool.core.text.CharSequenceUtil;
import org.ballcat.security.captcha.CaptchaValidator;
import org.ballcat.security.properties.SecurityProperties;
import org.ballcat.springsecurity.oauth2.server.authorization.web.filter.LoginCaptchaFilter;
import org.ballcat.springsecurity.oauth2.server.authorization.web.filter.LoginPasswordDecoderFilter;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.web.OAuth2ClientAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * OAuth2AuthorizationServerConfigurer 的包装类，便于扩展配置
 *
 * @author hccake
 */
public class OAuth2AuthorizationServerConfigurerWrapper
		extends AbstractHttpConfigurer<OAuth2AuthorizationServerConfigurer, HttpSecurity> {

	private final OAuth2AuthorizationServerConfigurer oAuth2AuthorizationServerConfigurer;

	private final SecurityProperties securityProperties;

	private final CaptchaValidator captchaValidator;

	public OAuth2AuthorizationServerConfigurerWrapper(
			OAuth2AuthorizationServerConfigurer oAuth2AuthorizationServerConfigurer,
			SecurityProperties securityProperties, CaptchaValidator captchaValidator) {
		this.oAuth2AuthorizationServerConfigurer = oAuth2AuthorizationServerConfigurer;
		this.securityProperties = securityProperties;
		this.captchaValidator = captchaValidator;
	}

	@Override
	public void init(HttpSecurity httpSecurity) {
		oAuth2AuthorizationServerConfigurer.init(httpSecurity);
	}

	@Override
	public void configure(HttpSecurity httpSecurity) {
		oAuth2AuthorizationServerConfigurer.configure(httpSecurity);

		// 只处理登录接口
		AntPathRequestMatcher requestMatcher = new AntPathRequestMatcher("/oauth2/token", HttpMethod.POST.name());

		// 验证码
		if (captchaValidator != null) {
			httpSecurity.addFilterAfter(new LoginCaptchaFilter(requestMatcher, captchaValidator),
					OAuth2ClientAuthenticationFilter.class);
		}

		// 密码解密
		String passwordSecretKey = securityProperties.getPasswordSecretKey();
		if (CharSequenceUtil.isNotBlank(passwordSecretKey)) {
			httpSecurity.addFilterAfter(new LoginPasswordDecoderFilter(requestMatcher, passwordSecretKey),
					OAuth2ClientAuthenticationFilter.class);
		}

	}

	/**
	 * Returns a RequestMatcher for the authorization server endpoints.
	 * @return {@link RequestMatcher}
	 */
	public RequestMatcher getEndpointsMatcher() {
		return oAuth2AuthorizationServerConfigurer.getEndpointsMatcher();
	}

}
