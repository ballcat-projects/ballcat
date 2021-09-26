package com.hccake.ballcat.auth.configurer;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;

import javax.sql.DataSource;

/**
 * 启用 jdbc 方式获取客户端配置信息
 *
 * @author hccake
 */
@RequiredArgsConstructor
public class JdbcOAuth2ClientConfigurer implements OAuth2ClientConfigurer {

	private final DataSource dataSource;

	@Override
	public void configure(ClientDetailsServiceConfigurer configurer) throws Exception {
		configurer.jdbc(dataSource);
	}

}
