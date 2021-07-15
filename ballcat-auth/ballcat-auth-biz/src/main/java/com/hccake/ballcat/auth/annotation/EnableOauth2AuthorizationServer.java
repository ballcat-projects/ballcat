package com.hccake.ballcat.auth.annotation;

import com.hccake.ballcat.auth.configuration.AuthorizationAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

import java.lang.annotation.*;

/**
 * 开启 Oauth2 授权服务器
 * @author hccake
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({ AuthorizationAutoConfiguration.class })
@EnableAuthorizationServer
public @interface EnableOauth2AuthorizationServer {

}
