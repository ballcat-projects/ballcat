package org.ballcat.springsecurity.oauth2.server.authorization.configurer;

import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

/**
 * @author hccake
 */
public abstract class OAuth2AuthorizationServerExtensionConfigurer<C extends OAuth2AuthorizationServerExtensionConfigurer<C, H>, H extends HttpSecurityBuilder<H>>
		extends AbstractHttpConfigurer<C, H> {

}
