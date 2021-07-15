package com.hccake.ballcat.auth;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 授权服务器的配置文件
 *
 * @author hccake
 */
@Getter
@Setter
@ConfigurationProperties(prefix = OAuth2AuthorizationServerProperties.PREFIX)
public class OAuth2AuthorizationServerProperties {

	public static final String PREFIX = "ballcat.security.oauth2.authorizationserver";

	/**
	 * 登陆验证码开关
	 */
	private boolean loginCaptchaEnabled = true;

}
