package com.hccake.ballcat.auth.annotation;

import com.hccake.ballcat.auth.configuration.AuthorizationAutoConfiguration;
import com.hccake.ballcat.auth.configuration.AuthorizationFilterConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 开启 Oauth2 授权服务器
 * @author hccake
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({ AuthorizationFilterConfiguration.class, AuthorizationAutoConfiguration.class })
@EnableAuthorizationServer
public @interface EnableOauth2AuthorizationServer {

}
