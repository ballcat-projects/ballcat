package org.ballcat.springsecurity.oauth2.server.resource.configurer;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * OAuth2 Resource Server 的 SecurityFilterChain 构造器
 *
 * @author hccake
 */
public interface Oauth2ResourceServerSecurityFilterChainBuilder {

	/**
	 * 构建 OAuth2 Resource Server 的 SecurityFilterChain
	 * @param http HttpSecurity
	 * @return SecurityFilterChain
	 * @throws Exception 构建异常
	 */
	SecurityFilterChain build(HttpSecurity http) throws Exception;

}
