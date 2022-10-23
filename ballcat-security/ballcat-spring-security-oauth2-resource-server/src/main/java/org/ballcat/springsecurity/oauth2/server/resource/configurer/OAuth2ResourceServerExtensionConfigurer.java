package org.ballcat.springsecurity.oauth2.server.resource.configurer;

import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

/**
 * OAuth2资源服务器的扩展配置
 *
 * @author hccake
 */
public abstract class OAuth2ResourceServerExtensionConfigurer<H extends HttpSecurityBuilder<H>>
		extends AbstractHttpConfigurer<OAuth2ResourceServerExtensionConfigurer<H>, H> {

}
