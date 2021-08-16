package com.hccake.ballcat.admin;

import com.hccake.ballcat.auth.annotation.EnableOauth2AuthorizationServer;
import com.hccake.ballcat.common.security.annotation.EnableOauth2ResourceServer;
import com.hccake.ballcat.common.security.properties.SecurityProperties;
import com.hccake.ballcat.system.properties.UpmsProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author Hccake
 * @version 1.0
 * @date 2020/5/25 21:01
 */
@EnableAsync
@MapperScan("com.hccake.ballcat.**.mapper")
@ComponentScan({ "com.hccake.ballcat.admin", "com.hccake.ballcat.auth", "com.hccake.ballcat.system",
		"com.hccake.ballcat.log", "com.hccake.ballcat.file", "com.hccake.ballcat.notify" })
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({ UpmsProperties.class, SecurityProperties.class })
@EnableOauth2AuthorizationServer
@EnableOauth2ResourceServer
public class UpmsAutoConfiguration {

}
