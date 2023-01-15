package org.ballcat.springsecurity.oauth2.server.authorization.config.configurer;

import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

/**
 * 对 OAuth2 授权服务器的 SecurityConfigurer 进行扩展的配置类
 *
 * @author hccake
 */
public abstract class OAuth2AuthorizationServerExtensionConfigurer<C extends OAuth2AuthorizationServerExtensionConfigurer<C, H>, H extends HttpSecurityBuilder<H>>
		extends AbstractHttpConfigurer<C, H> {

}
