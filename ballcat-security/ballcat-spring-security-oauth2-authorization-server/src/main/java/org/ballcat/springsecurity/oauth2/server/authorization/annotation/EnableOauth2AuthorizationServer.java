package org.ballcat.springsecurity.oauth2.server.authorization.annotation;

import org.ballcat.springsecurity.oauth2.server.authorization.autoconfigure.OAuth2AuthorizationServerAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;

import java.lang.annotation.*;

/**
 * 开启 Oauth2 授权服务器
 *
 * @author hccake
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@ImportAutoConfiguration(OAuth2AuthorizationServerAutoConfiguration.class)
public @interface EnableOauth2AuthorizationServer {

}
