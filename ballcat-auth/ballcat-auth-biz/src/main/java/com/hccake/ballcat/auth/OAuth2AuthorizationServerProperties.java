package com.hccake.ballcat.auth;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 授权服务器的配置文件
 *
 * @author hccake
 */
@Deprecated
@Getter
@Setter
@ConfigurationProperties(prefix = OAuth2AuthorizationServerProperties.PREFIX)
public class OAuth2AuthorizationServerProperties {

	public static final String PREFIX = "ballcat.security.oauth2.authorizationserver";

	/**
	 * 登陆验证码开关
	 */
	private boolean loginCaptchaEnabled = false;

	/**
	 * 表单登录地址
	 * <p>
	 * - 不配置将使用 security 默认的登录页：/login <br>
	 * - 配置后则必须自己提供登录页面
	 */
	private String formLoginPage = null;

}
