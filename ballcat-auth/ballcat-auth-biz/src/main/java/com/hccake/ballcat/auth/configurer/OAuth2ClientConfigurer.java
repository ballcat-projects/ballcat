package com.hccake.ballcat.auth.configurer;

import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;

/**
 * 授权服务器的 OAuth2 Client 相关配置类
 *
 * @author hccake
 */
public interface OAuth2ClientConfigurer {

	/**
	 * 配置 clientDetailsService
	 * @param clientDetailsServiceConfigurer clientDetailsService 配置类
	 * @exception Exception 异常信息
	 */
	void configure(ClientDetailsServiceConfigurer clientDetailsServiceConfigurer) throws Exception;

}
