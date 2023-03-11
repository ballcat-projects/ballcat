package org.ballcat.springsecurity.oauth2.server.authorization.properties;

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

	public static final String PREFIX = "ballcat.security.oauth2.authorizationserver";

	/**
	 * 登陆验证码开关
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
	 * 用户同意授权页面
	 *
	 * @see OAuth2AuthorizationEndpointConfigurer#consentPage
	 */
	private String consentPage;

	/**
	 * 无状态登陆
	 */
	private boolean stateless = false;

}
