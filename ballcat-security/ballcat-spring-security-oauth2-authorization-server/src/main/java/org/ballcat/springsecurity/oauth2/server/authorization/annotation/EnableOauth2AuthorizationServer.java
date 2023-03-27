package org.ballcat.springsecurity.oauth2.server.authorization.annotation;

import org.ballcat.springsecurity.oauth2.server.authorization.autoconfigure.OAuth2AuthorizationServerAutoConfiguration;
import org.springframework.context.annotation.Import;

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
@Import(OAuth2AuthorizationServerAutoConfiguration.class)
public @interface EnableOauth2AuthorizationServer {

}
