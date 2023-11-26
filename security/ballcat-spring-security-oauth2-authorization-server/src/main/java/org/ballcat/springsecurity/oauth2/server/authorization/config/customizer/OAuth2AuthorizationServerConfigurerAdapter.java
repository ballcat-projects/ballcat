package org.ballcat.springsecurity.oauth2.server.authorization.config.customizer;

import org.springframework.core.Ordered;
import org.springframework.security.config.annotation.SecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;

public interface OAuth2AuthorizationServerConfigurerAdapter
		extends SecurityConfigurer<DefaultSecurityFilterChain, HttpSecurity>, Ordered {

}
