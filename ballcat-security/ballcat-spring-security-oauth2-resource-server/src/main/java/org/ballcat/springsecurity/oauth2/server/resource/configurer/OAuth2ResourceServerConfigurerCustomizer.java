package org.ballcat.springsecurity.oauth2.server.resource.configurer;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

/**
 * 对 Ballcat 默认的配置的 OAuth2ResourceServerConfigurer 进行自定义处理
 *
 * @author hccake
 */
public interface OAuth2ResourceServerConfigurerCustomizer {

	/**
	 * 对资源服务器配置进行自定义
	 * @param httpSecurity security configuration
	 */
	void customize(HttpSecurity httpSecurity) throws Exception;

}
